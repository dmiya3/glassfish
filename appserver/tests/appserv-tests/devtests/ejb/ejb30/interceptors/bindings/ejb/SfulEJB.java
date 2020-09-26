/*
 * Copyright (c) 2002, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.s1asdev.ejb.ejb30.interceptors.bindings;


import jakarta.ejb.Stateful;
import jakarta.interceptor.Interceptors;
import jakarta.interceptor.ExcludeDefaultInterceptors;
import jakarta.interceptor.ExcludeClassInterceptors;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import jakarta.ejb.EJBException;

@Stateful
@Interceptors({InterceptorC.class, InterceptorD.class})
public class SfulEJB implements Sful
{

    private boolean aroundInvokeCalled = false;
    private boolean postConstructCalled = false;

    // postConstruct declared in ejb-jar.xml
    private void postConstruct() {
        System.out.println("In SfulEJB:postConstruct");
        postConstructCalled = true;
    }

    @ExcludeDefaultInterceptors
    public void cd() {
        System.out.println("in SfulEJB:cd().  postConstruct = " + 
                           postConstructCalled);

        if( !postConstructCalled ) {
            throw new EJBException("postConstruct wasn't called");
        }

        // a little extra checking to make sure aroundInvoke is invoked...
        if( !aroundInvokeCalled ) {
            throw new EJBException("bean class aroundInvoke not called");
        }
        aroundInvokeCalled = false;
    }

    @ExcludeClassInterceptors
    public void ab() {}

    public void abcd() {}

    @ExcludeClassInterceptors
    @Interceptors({InterceptorE.class, InterceptorF.class})
    public void abef() {}

    @ExcludeDefaultInterceptors
    @ExcludeClassInterceptors
    @Interceptors({InterceptorE.class, InterceptorF.class})
    public void ef() {}

    @ExcludeDefaultInterceptors
    @ExcludeClassInterceptors
    @Interceptors({InterceptorC.class, InterceptorE.class, InterceptorF.class})
    public void cef() {}

    @ExcludeDefaultInterceptors
    @ExcludeClassInterceptors
    @Interceptors({InterceptorC.class, InterceptorE.class, InterceptorF.class, InterceptorA.class})
    public void cefa() {}

    @ExcludeDefaultInterceptors
    @Interceptors({InterceptorE.class, InterceptorF.class})
    public void cdef() {}
    
    @ExcludeDefaultInterceptors
    @ExcludeClassInterceptors
    public void nothing() {}

    @Interceptors({InterceptorE.class, InterceptorF.class})    
    public void abcdef() {}

    // total ordering overridden in deployment descriptor
    @Interceptors({InterceptorE.class, InterceptorF.class})    
    public void acbdfe() {}

    // declared in ejb-jar.xml
    private Object aroundInvoke(InvocationContext ctx)
    {
        System.out.println("In SfulEJB:aroundInvoke");
        aroundInvokeCalled = true;
        Common.checkResults(ctx);
        try {
            return ctx.proceed();
        } catch(Exception e) {
            throw new EJBException(e);
        }
    }
    
}
