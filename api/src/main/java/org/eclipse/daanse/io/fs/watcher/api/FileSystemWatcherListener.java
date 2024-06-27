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
package org.eclipse.daanse.io.fs.watcher.api;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

import org.osgi.annotation.versioning.ConsumerType;

/**
 * The listener interface for receiving path events. The class that is
 * interested in processing a path event implements this interface.
 */
@ConsumerType
public interface FileSystemWatcherListener {

    /**
     * Handle base path.
     *
     * Base Paths are the root Directory that are observed. Would be called once
     * before activating the Listener. Even when observing sub-directories, this
     * method is called only once with the root directory.
     *
     * @param basePath the base path
     */
    void handleBasePath(Path basePath);

    /**
     * Handle initial paths.
     *
     * Initial Paths are all Paths that exist before the Listener is activated.
     *
     * @param paths the paths
     */
    void handleInitialPaths(List<Path> paths);

    /**
     * Handle path event.
     *
     * @param path the path
     * @param kind the kind
     */
    void handlePathEvent(Path path, WatchEvent.Kind<Path> kind);

}
