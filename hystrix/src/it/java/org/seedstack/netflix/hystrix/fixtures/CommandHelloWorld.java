/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.hystrix.fixtures;

import org.seedstack.netflix.hystrix.internal.annotation.HystrixCommand;

public class CommandHelloWorld {

    @HystrixCommand
    public String helloWorld(String name) {
        return "Hello " + name + " !";
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String failure(String name) {
        throw new RuntimeException("This method always fails !");
    }

    public String fallback(String name) {
        return "Fallback : Hello " + name + " !";
    }

    @HystrixCommand(fallbackMethod = "nestedFallback1")
    public String nestedCommand(String name) {
        throw new RuntimeException("Fail !");
    }

    @HystrixCommand(fallbackMethod = "nestedFallback2")
    public String nestedFallback1(String name) {
        throw new RuntimeException("Fallback fail !");
    }

    public String nestedFallback2(String name) {
        return "nestedFallback2: Hello " + name + " !";
    }
}
