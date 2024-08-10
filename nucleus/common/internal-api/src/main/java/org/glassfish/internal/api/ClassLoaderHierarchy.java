/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.internal.api;

import com.sun.enterprise.module.ResolveError;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

import org.glassfish.api.deployment.DeploymentContext;
import org.jvnet.hk2.annotations.Contract;

/**
 * This class is responsible for creation of class loader hierarchy
 * of an application.
 *
 * @author Sanjeeb.Sahoo@Sun.COM
 */
@Contract
public interface ClassLoaderHierarchy {
    /**
     * Returns a ClassLoader that can load classes exported by any OSGi bundle
     * in the system for public use. Such classes include Jakarta EE API, AMX API,
     * appserv-ext API, etc. CommonClassLoader delegates to this class loader.
     * @return a ClassLoader that can load classes exported by any bundles
     */
    ClassLoader getAPIClassLoader();

    /**
     * Returns a class loader that is common to all deployed applications.
     * Common Class Loader is responsible for loading classes from
     * following URLs (the order is strictly maintained):
     * lib/*.jar:domain_dir/lib/classes:domain_dir/lib/*.jar.
     * Please note that domain_dir/lib/classes comes before domain_dir/lib/*.jar,
     * just like WEB-INF/classes is searched first before WEB-INF/lib/*.jar.
     * It delegates to APIClassLoader.
     * @see #getAPIClassLoader()
     * @return ClassLoader common to all deployed applications.
     */
    ClassLoader getCommonClassLoader();

    /**
     * Returns the classpath equiavalent to what is used by classloader
     * returned by {@link #getCommonClassLoader()}. Classpath entries are
     * separated by {@link java.io.File#separatorChar}, but don't assume
     * there will be any leading or trailing separator char. It returns
     * an empty string if there are no libraries installed.
     * @return ClassPath separated by {@link java.io.File#pathSeparatorChar}
     */
    String getCommonClassPath();

    /**
     * Returns the class loader which has visibility to appropriate list of
     * standalone RARs deployed in the server. Depending on a policy,
     * this can either return a singleton classloader for all applications or
     * a class loader specific to an application. When a singleton class loader
     * is returned, such a class loader will have visibility to all the
     * standalone RARs deployed in the system. When a class loader specific
     * to an application is returned, such a class loader will have visibility
     * to only standalone RARs that the application depends on.
     *
     * @param application Application whose class loader hierarchy is being set
     * @return class loader which has visibility to appropriate list of
     *         standalone RARs.
     */
    DelegatingClassLoader getConnectorClassLoader(String application);

    /**
     * Returns AppLibClassLoader. As the name suggests, this class loader
     * has visibility to deploy time libraries (--libraries and EXTENSION_LIST of MANIFEST.MF, provided the library is
     * available in 'applibs' directory) for an application.
     * It is different from CommonClassLoader in a sense that the libraries that
     * are part of common class loader are shared by all applications,
     * where as this class loader adds a scope to a library.
     * @param application Application for which this class loader is created
     * @param libURIs list of URIs, where each URI represent a library
     * @return class loader that has visibility to appropriate
     * application specific libraries.
     * @throws MalformedURLException
     * @see #getAppLibClassFinder(List<URI>)
     */
    ClassLoader getAppLibClassLoader(String application, List<URI> libURIs)
            throws MalformedURLException;

    /**
     * Returns ApplibClassFinder. As the name suggests, this class finder
     * has visibility to deploy time libraries (--libraries and EXTENSION_LIST of MANIFEST.MF,
     * provided the library is available in 'applibs' directory) for an application.
     * It is different from CommonClassLoader in a sense that the libraries that
     * are part of common class loader are shared by all applications,
     * where as this class loader adds a scope to a library.    <br>
     * <b>NOTE :</b> Difference between this API and getAppLibClassLoader(String, List&lt;URI&gt;) is the latter
     * will be used by all applications (for its parent classloader) whereas this API will be used only by connector
     * classloader. All other application classloaders will have AppLibClassLoader as parent where as connector classloader
     * will be above AppLibClassLoader and hence simple delegation is not feasible.
     * @param libURIs list of URIs, where each URI represent a library
     * @return class loader that has visibility to appropriate
     * application specific libraries.
     * @throws MalformedURLException
     * @see #getAppLibClassLoader(String, List<URI>)
     */
    DelegatingClassLoader.ClassFinder getAppLibClassFinder(List<URI> libURIs)
            throws MalformedURLException;


    /**
        * Sets up the parent class loader for the application class loader.
        * Application class loader are under the control of the ArchiveHandler since
        * a special archive file format will require a specific class loader.
        *
        * However GlassFish needs to be able to add capabilities to the application
        * like adding APIs accessibility, this is done through its parent class loader
        * which we create and maintain.
        *
        * @param parent the parent class loader
        * @param context deployment context
        * @return class loader capable of loading public APIs identified by the deployers
        * @throws com.sun.enterprise.module.ResolveError if one of the deployer's public API module is not found.
        */
       ClassLoader createApplicationParentCL(ClassLoader parent, DeploymentContext context)
           throws ResolveError;

}
