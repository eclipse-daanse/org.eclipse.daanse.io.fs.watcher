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
package org.eclipse.daanse.io.fs.watcher.api.propertytypes;

import org.eclipse.daanse.io.fs.watcher.api.EventKind;
import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherWhiteboardConstants;
import org.eclipse.daanse.io.fs.watcher.api.annotations.RequireFileSystemWatcherWhiteboard;
import org.osgi.service.component.annotations.ComponentPropertyType;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition
@ComponentPropertyType
@RequireFileSystemWatcherWhiteboard
public @interface FileSystemWatcherListenerProperties {

    public static final String PREFIX_ = FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PREFIX;

    /**
     *
     * @see FileSystemWatcherWhiteboardConstants#FILESYSTEM_WATCHER_PATH
     */
    @AttributeDefinition(required = true)
    String path() default "";

    /**
     *
     * @see FileSystemWatcherWhiteboardConstants#FILESYSTEM_WATCHER_PATTERN
     */
    @AttributeDefinition
    String pattern() default FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_PATTERN_DEFAULT;

    /**
     *
     * @see FileSystemWatcherWhiteboardConstants#FILESYSTEM_WATCHER_KINDS
     */
    @AttributeDefinition()
    EventKind[] kinds() default { EventKind.ENTRY_CREATE, EventKind.ENTRY_DELETE, EventKind.ENTRY_MODIFY };

    /**
     *
     * @see FileSystemWatcherWhiteboardConstants#FILESYSTEM_WATCHER_RECURSIVE
     */
    @AttributeDefinition()
    boolean recursive() default false;

}
