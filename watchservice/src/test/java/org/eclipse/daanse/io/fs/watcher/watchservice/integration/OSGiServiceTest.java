/*
* Copyright (c) 2024 Contributors to the Eclipse Foundation.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors:
*   SmartCity Jena - initial
*   Stefan Bischof (bipolis.org) - initial
*/
package org.eclipse.daanse.io.fs.watcher.watchservice.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.osgi.test.common.dictionary.Dictionaries.asDictionary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherListener;
import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherWhiteboardConstants;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.annotations.RequireConfigurationAdmin;
import org.osgi.service.component.annotations.RequireServiceComponentRuntime;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;

import aQute.bnd.annotation.spi.ServiceProvider;
import org.eclipse.daanse.io.fs.watcher.watchservice.StoringFileSystemWatcherListener;

@RequireServiceComponentRuntime
@RequireConfigurationAdmin
@ExtendWith(BundleContextExtension.class)
@ServiceProvider(value = FileSystemWatcherListener.class)
class OSGiServiceTest {

    private static Duration WAIT_MOST = Duration.ofSeconds(10);
    @InjectBundleContext
    BundleContext bc;

    @TempDir
    Path path;

    @RepeatedTest(value = 200)
    void testFileSystemListener() throws Exception {

        StoringFileSystemWatcherListener listener = new StoringFileSystemWatcherListener();

        Path file_preexist = Files.createTempFile(path, "pre_exist1", ".txt");
        Files.writeString(file_preexist, "1");

        Map<String, Object> map = Map.of(FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATH,
                path.toAbsolutePath().toString(), FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_RECURSIVE,
                "true");

        ServiceRegistration<FileSystemWatcherListener> sreg = bc.registerService(FileSystemWatcherListener.class,
                listener, asDictionary(map));

        await().atMost(WAIT_MOST).until(() -> listener.getInitialPaths().size() == 1);

        assertThat(listener.getInitialPaths()).hasSize(1);
        assertThat(listener.getInitialPaths().poll()).isEqualTo(file_preexist);

        Path file_created = Files.createTempFile(path, "created1", ".txt");// create
        Files.delete(file_preexist);// delete
        Files.writeString(file_created, "2");// modify

        await().atMost(WAIT_MOST).until(() -> listener.getEvents().size() >= 3);// sometime multiple modify

        assertThat(listener.getEvents()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(listener.getEvents().peek().getKey()).isEqualTo(file_created);
        assertThat(listener.getEvents().poll().getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_CREATE);

        assertThat(listener.getEvents().peek().getKey()).isEqualTo(file_preexist);
        assertThat(listener.getEvents().poll().getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_DELETE);

        assertThat(listener.getEvents().peek().getKey()).isEqualTo(file_created);
        assertThat(listener.getEvents().poll().getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_MODIFY);

        // maybe more modify existing, then clear
        listener.getEvents().clear();

        Path dir1 = Files.createDirectory(path.resolve("dir1"));// create dir
        await().atMost(WAIT_MOST).pollDelay(Duration.ofMillis(100)).pollInterval(Duration.ofMillis(100))
                .until(() -> listener.getEvents().size() == 1);

        Path f1InDir1 = Files.createTempFile(dir1, "af1", ".txt");// create

        await().atMost(WAIT_MOST).until(() -> listener.getEvents().size() == 2);

        assertThat(listener.getEvents()).hasSize(2);
        assertThat(listener.getEvents().peek().getKey()).isEqualTo(dir1);
        assertThat(listener.getEvents().poll().getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_CREATE);

        assertThat(listener.getEvents().peek().getKey()).isEqualTo(f1InDir1);
        assertThat(listener.getEvents().poll().getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_CREATE);

        sreg.unregister();
        Files.createTempFile(dir1, "wf2", ".txt");// create
        await().pollDelay(Duration.ofMillis(200)).atMost(WAIT_MOST).until(() -> listener.getEvents().isEmpty());

    }

    @Test
    void testMultipleListenersWithDifferentPatterns() throws Exception {
        StoringFileSystemWatcherListener listenerCsv = new StoringFileSystemWatcherListener();
        StoringFileSystemWatcherListener listenerTxt = new StoringFileSystemWatcherListener();

        Map<String, Object> mapCsv = Map.of(
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATH, path.toAbsolutePath().toString(),
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATTERN, ".*\\.csv");
        Map<String, Object> mapTxt = Map.of(
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATH, path.toAbsolutePath().toString(),
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATTERN, ".*\\.txt");

        ServiceRegistration<FileSystemWatcherListener> sregCsv = bc.registerService(FileSystemWatcherListener.class,
                listenerCsv, asDictionary(mapCsv));
        ServiceRegistration<FileSystemWatcherListener> sregTxt = bc.registerService(FileSystemWatcherListener.class,
                listenerTxt, asDictionary(mapTxt));

        // wait for both listeners to have their base path set (i.e. registered with the watcher)
        await().atMost(WAIT_MOST)
                .until(() -> listenerCsv.getBasePath() != null && listenerTxt.getBasePath() != null);

        Path csvFile = path.resolve("data.csv");
        Path txtFile = path.resolve("notes.txt");
        Files.createFile(csvFile);
        Files.createFile(txtFile);

        // both listeners must each receive at least one event for their own file type
        await().atMost(WAIT_MOST).until(() -> !listenerCsv.getEvents().isEmpty());
        await().atMost(WAIT_MOST).until(() -> !listenerTxt.getEvents().isEmpty());

        assertThat(listenerCsv.getEvents())
                .allSatisfy(e -> assertThat(e.getKey().toString()).endsWith(".csv"));
        assertThat(listenerTxt.getEvents())
                .allSatisfy(e -> assertThat(e.getKey().toString()).endsWith(".txt"));

        sregCsv.unregister();
        sregTxt.unregister();
    }

    @Test
    void testEventsDuringSlowInitialPathsAreNotLost() throws Exception {
        StoringFileSystemWatcherListener listener = new StoringFileSystemWatcherListener() {
            @Override
            public void handleInitialPaths(List<Path> initialPaths) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                super.handleInitialPaths(initialPaths);
            }
        };

        Map<String, Object> map = Map.of(
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATH, path.toAbsolutePath().toString());

        Path file = path.resolve("race.txt");

        // Run file ops on a separate thread, so they execute regardless of whether SCR
        // dispatches bind synchronously (registerService would block for ~3s) or async.
        // The thread waits until the bind thread has set the base path, then gives it a
        // short head-start to enter the slow handleInitialPaths before creating+deleting.
        Thread fileOps = new Thread(() -> {
            try {
                await().atMost(WAIT_MOST).until(() -> listener.getBasePath() != null);
                Thread.sleep(500);
                Files.createFile(file);
                Files.delete(file);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        fileOps.start();

        ServiceRegistration<FileSystemWatcherListener> sreg = bc.registerService(FileSystemWatcherListener.class,
                listener, asDictionary(map));

        fileOps.join();

        await().atMost(WAIT_MOST).until(() -> listener.getEvents().stream()
                .anyMatch(e -> e.getKey().equals(file)
                        && e.getValue().equals(StandardWatchEventKinds.ENTRY_CREATE))
                && listener.getEvents().stream()
                        .anyMatch(e -> e.getKey().equals(file)
                                && e.getValue().equals(StandardWatchEventKinds.ENTRY_DELETE)));

        assertThat(listener.getEvents())
                .as("create and delete happening while handleInitialPaths is still running must both be reported")
                .anySatisfy(e -> {
                    assertThat(e.getKey()).isEqualTo(file);
                    assertThat(e.getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_CREATE);
                })
                .anySatisfy(e -> {
                    assertThat(e.getKey()).isEqualTo(file);
                    assertThat(e.getValue()).isEqualTo(StandardWatchEventKinds.ENTRY_DELETE);
                });

        sreg.unregister();
    }

    @Test
    void testInitialPathsAreFilteredByPattern() throws Exception {
        Path csvFile = Files.createFile(path.resolve("preexisting.csv"));
        Path txtFile = Files.createFile(path.resolve("preexisting.txt"));

        StoringFileSystemWatcherListener listener = new StoringFileSystemWatcherListener();

        Map<String, Object> map = Map.of(
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATH, path.toAbsolutePath().toString(),
                FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATTERN, ".*\\.csv");

        ServiceRegistration<FileSystemWatcherListener> sreg = bc.registerService(FileSystemWatcherListener.class,
                listener, asDictionary(map));

        await().atMost(WAIT_MOST).until(() -> listener.getBasePath() != null);

        assertThat(listener.getInitialPaths())
                .as("initial paths should only contain files matching the pattern")
                .containsExactly(csvFile)
                .doesNotContain(txtFile);

        sreg.unregister();
    }

}
