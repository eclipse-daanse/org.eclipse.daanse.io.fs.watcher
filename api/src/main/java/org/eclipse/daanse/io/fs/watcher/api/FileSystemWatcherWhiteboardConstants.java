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

/**
 * Defines standard constants for the FileSystem Watcher Services Whiteboard
 * services.
 */
public final class FileSystemWatcherWhiteboardConstants {
    private FileSystemWatcherWhiteboardConstants() {
        // non-instantiable
    }

    /**
     * Prefix for all Service property of FileSystem Watchers
     * {@link FileSystemWatcherListener}.
     */
    public static final String FILESYSTEM_WATCHER_PREFIX = "io.fs.watcher";

    /**
     * Default value for
     * {@link FileSystemWatcherWhiteboardConstants#FILESYSTEM_WATCHER_PATTERN}.
     */
    public static final String FILESYSTEM_WATCHER_PATTERN_DEFAULT = ".*";

    /**
     * Service property specifying that a FileSystem Watchers
     * {@link FileSystemWatcherListener} should be processed by the whiteboard.
     * <p>
     * The value of this service property must be of type {@code String}.
     */
    public static final String FILESYSTEM_WATCHER_PATH = FILESYSTEM_WATCHER_PREFIX + ".path";

    /**
     * Service property specifying that a FileSystem Watchers
     * {@link FileSystemWatcherListener} should be processed by the whiteboard.
     * <p>
     * The value of this service property must be of type {@code String}.
     */
    public static final String FILESYSTEM_WATCHER_PATTERN = FILESYSTEM_WATCHER_PREFIX + ".pattern";

    /**
     * Service property specifying that a FileSystem Watchers
     * {@link FileSystemWatcherListener} should be processed by the whiteboard.
     * <p>
     * The value of this service property must be of type {@code String} or array of
     * type {@code String}.
     */
    public static final String FILESYSTEM_WATCHER_KINDS = FILESYSTEM_WATCHER_PREFIX + ".kinds";

    /**
     * Service property specifying that a FileSystem Watchers
     * {@link FileSystemWatcherListener} should be processed by the whiteboard.
     * <p>
     * The value of this service property must be of type {@code String} or
     * {@link Boolean} and set to &quot;true&quot; or <code>true</code>.
     */
    public static final String FILESYSTEM_WATCHER_RECURSIVE = FILESYSTEM_WATCHER_PREFIX + ".recursive";

    /**
     * The name of the implementation capability for the Whiteboard Specification
     * for FileSystem Watcher Services.
     */
    public static final String FILESYSTEM_WATCHER_WHITEBOARD_IMPLEMENTATION = "io.fs.watcher";

    /**
     * The version of the implementation capability for the Whiteboard Specification
     * for FileSystem Watcher Services.
     */
    public static final String FILESYSTEM_WATCHER_WHITEBOARD_SPECIFICATION_VERSION = "1.0";

}
