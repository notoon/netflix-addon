/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.internal.guice;

import org.seedstack.netflix.hystrix.internal.annotation.HystrixCommand;
import org.seedstack.shed.reflect.StandardAnnotationResolver;

import java.lang.reflect.Method;

class HystrixCommandAnnotationResolver extends StandardAnnotationResolver<Method, HystrixCommand> {
    static HystrixCommandAnnotationResolver INSTANCE = new HystrixCommandAnnotationResolver();

    private HystrixCommandAnnotationResolver() {
        // no external instantiation allowed
    }
}
