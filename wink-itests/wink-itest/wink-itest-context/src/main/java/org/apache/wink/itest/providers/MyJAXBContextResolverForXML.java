/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.wink.itest.providers;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.wink.itest.providers.xml.RootElement;

@Provider
@Produces("text/xml")
public class MyJAXBContextResolverForXML implements ContextResolver<JAXBContext> {

    public JAXBContext getContext(Class<?> arg0) {
        if (RootElement.class.isAssignableFrom(arg0)) {
            try {
                return JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
