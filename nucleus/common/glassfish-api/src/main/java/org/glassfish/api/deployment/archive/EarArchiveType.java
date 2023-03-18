/*
 * Copyright (c) 2023 Eclipse Foundation and/or its affiliates. All rights reserved.
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

package org.glassfish.api.deployment.archive;

/**
 * EAR Archive Type.
 *
 * @author David Matejcek
 */
public class EarArchiveType extends ArchiveType {

    public static final String ARCHIVE_TYPE = "ear";
    public static final String ARCHIVE_EXTENSION = ".ear";
    public static final EarArchiveType EAR_ARCHIVE = new EarArchiveType();

    public EarArchiveType() {
        super(ARCHIVE_TYPE, ARCHIVE_EXTENSION);
    }
}
