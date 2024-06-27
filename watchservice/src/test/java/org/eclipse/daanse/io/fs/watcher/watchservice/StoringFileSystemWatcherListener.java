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

import java.nio.file.Path;
import java.nio.file.WatchEvent.Kind;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherListener;

import java.util.Queue;

class StoringFileSystemWatcherListener implements FileSystemWatcherListener {

    StoringFileSystemWatcherListener() {
        clear();
    }

    private Queue<Path> initialPaths;
    private Queue<Entry<Path, Kind<Path>>> events;

    void clear() {
        initialPaths = new ArrayDeque<>();
        events = new ArrayDeque<>();
    }

    @Override
    public void handleInitialPaths(List<Path> initialPaths) {
        this.initialPaths.addAll(initialPaths);

    }

    @Override
    public void handlePathEvent(Path path, Kind<Path> kind) {
        events.add(new AbstractMap.SimpleEntry<>(path, kind));
    }

    public Queue<Path> getInitialPaths() {
        return initialPaths;

    }

    public Queue<Entry<Path, Kind<Path>>> getEvents() {
        return events;
    }

    @Override
    public void handleBasePath(Path basePath) {

    }

}
