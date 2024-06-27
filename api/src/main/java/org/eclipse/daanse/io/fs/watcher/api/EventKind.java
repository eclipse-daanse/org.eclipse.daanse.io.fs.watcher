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
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;

public enum EventKind {
    /**
     * {@link StandardWatchEventKinds#ENTRY_CREATE}
     */
    ENTRY_CREATE(StandardWatchEventKinds.ENTRY_CREATE),
    /**
     * {@link StandardWatchEventKinds#ENTRY_MODIFY}
     */
    ENTRY_MODIFY(StandardWatchEventKinds.ENTRY_MODIFY),
    /**
     * {@link StandardWatchEventKinds#ENTRY_DELETE}
     */
    ENTRY_DELETE(StandardWatchEventKinds.ENTRY_DELETE);

    private final WatchEvent.Kind<Path> kind;

    EventKind(WatchEvent.Kind<Path> kind) {
        this.kind = kind;
    }

    public WatchEvent.Kind<Path> getKind() {
        return kind;
    }

}
