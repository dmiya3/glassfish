/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation.
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.enterprise.admin.launcher;

import com.sun.enterprise.util.OS;

/**
 *
 * @author bnevins
 */
class GFLauncherConstants {
    static final String JAVA_NATIVE_SYSPROP_NAME = "java.library.path";
    static final String NEWLINE = System.getProperty("line.separator");
    static final String LIBDIR = "lib";
    static final String SPARC = "sparc";
    static final String SPARCV9 = "sparcv9";
    static final String X86 = "x86";
    static final String AMD64 = "amd64";
    static final String NATIVE_LIB_PREFIX = "native-library-path-prefix";
    static final String NATIVE_LIB_SUFFIX = "native-library-path-suffix";
    static final String LIBMON_NAME = "lib/monitor";
    static final String FLASHLIGHT_AGENT_NAME = "flashlight-agent.jar";
    static final String DEFAULT_LOGFILE = "logs/server.log";
    static final boolean OS_SUPPORTS_BTRACE = !OS.isAix();
}
