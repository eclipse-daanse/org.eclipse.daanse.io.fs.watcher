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
package org.eclipse.daanse.io.fs.watcher.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherWhiteboardConstants;
import org.osgi.annotation.bundle.Requirement;
import org.osgi.namespace.implementation.ImplementationNamespace;

/**
 * This annotation can be used to require the FileSystem Watcher Services
 * Whiteboard implementation. It can be used directly, or as a meta-annotation.
 * <p>
 * This annotation is applied to several of the FileSystem Watcher Services
 * Whiteboard component property annotations meaning that it does not normally
 * need to be applied to Declarative Services components which use the
 * FileSystem Watcher Services Whiteboard.
 *
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Requirement(namespace = ImplementationNamespace.IMPLEMENTATION_NAMESPACE, //
        name = FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_WHITEBOARD_IMPLEMENTATION, //
        version = FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_WHITEBOARD_SPECIFICATION_VERSION)
public @interface RequireFileSystemWatcherWhiteboard {
    // This is a marker annotation.
}
