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
@org.osgi.annotation.versioning.Version("0.0.1")
@org.osgi.annotation.bundle.Capability(namespace = ImplementationNamespace.IMPLEMENTATION_NAMESPACE, //
        name = FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_WHITEBOARD_IMPLEMENTATION, //
        version = FileSystemWatcherWhiteboardConstants.FILESYSTEM_WATCHER_WHITEBOARD_SPECIFICATION_VERSION)
package org.eclipse.daanse.io.fs.watcher.watchservice;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherWhiteboardConstants;
import org.osgi.namespace.implementation.ImplementationNamespace;
