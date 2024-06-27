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

import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.test.assertj.bundle.BundleAssert;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;

@ExtendWith(BundleContextExtension.class)
class BundleActivationTest {

    @org.junit.jupiter.api.Test
    void isBundleActive(@InjectBundleContext BundleContext bundleContext) throws Exception {
        BundleAssert.assertThat(bundleContext.getBundle()).isInState(Bundle.ACTIVE)
                .hasSymbolicName("org.eclipse.daanse.io.fs.watcher.api");
    }
}
