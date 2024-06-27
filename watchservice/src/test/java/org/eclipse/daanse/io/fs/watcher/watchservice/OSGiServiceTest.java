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
package org.eclipse.daanse.io.fs.watcher.watchservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.osgi.test.common.dictionary.Dictionaries.asDictionary;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.time.Duration;
import java.util.Map;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherListener;
import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherWhiteboardConstants;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.annotations.RequireConfigurationAdmin;
import org.osgi.service.component.annotations.RequireServiceComponentRuntime;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;

import aQute.bnd.annotation.spi.ServiceProvider;

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

}
