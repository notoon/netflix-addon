/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal.command;

import rx.Observable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Future;

class GenericCommand extends com.netflix.hystrix.HystrixCommand<Object> {

    private CommandParameters parameters;

    GenericCommand(Setter setter) {
        super(setter);
    }

    void setParameters(CommandParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    protected Object run() throws Exception {
        try {
            Object result = parameters.getInvocation().proceed();
            Class<?> returnType = parameters.getMethod().getReturnType();
            if (Future.class.isAssignableFrom(returnType)) {
                return ((Future) result).get();
            } else if (Observable.class.isAssignableFrom(returnType)) {
                // FIXME : works only for Observable that return a single value
                return ((Observable) result).toBlocking().toFuture().get();
            } else {
                return result;
            }
        } catch (Throwable throwable) {
            // propagate the Throwable for Hystrix to catch and execute the fallback
            throw new Exception(throwable);
        }
    }

    @Override
    protected Object getFallback() {
        Method fallbackMethod = parameters.getFallbackMethod();
        fallbackMethod.setAccessible(true);
        try {
            return fallbackMethod.invoke(parameters.getProxy(), parameters.getArgs());
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Fallback method not accessible: " + fallbackMethod.getName() + "(" + Arrays.toString(fallbackMethod.getParameterTypes()) + ")", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Fallback method in error: " + fallbackMethod.getName() + "(" + Arrays.toString(fallbackMethod.getParameterTypes()) + ")", e);
        }
    }
}
