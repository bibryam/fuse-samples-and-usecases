/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.fuse.boosters.rest.http;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class RouteTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("greetingsService", new GreetingsService() {
            @Override
            public Greetings getGreetings(String name) {
                return new Greetings("OK");
            }
        });
        return jndi;
    }

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception {
        return new CamelRouter();
    }

    @Test
    public void testGreetingRoute() throws Exception {
        Greetings result = template.requestBody("direct:greetingsImpl", "Donkeys111", Greetings.class);
        assertEquals("OK", result.getGreetings());
    }
}