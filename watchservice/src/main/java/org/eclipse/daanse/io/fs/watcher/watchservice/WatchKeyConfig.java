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
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.eclipse.daanse.io.fs.watcher.api.FileSystemWatcherListener;

record WatchKeyConfig(FileSystemWatcherListener listener, Path path, List<Kind<?>> kinds, Optional<Pattern> oPattern,
        boolean recursive) {
}
