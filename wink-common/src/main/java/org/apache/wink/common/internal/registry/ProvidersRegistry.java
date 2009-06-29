/*******************************************************************************
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
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/
package org.apache.wink.common.internal.registry;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wink.common.SymphonyApplication;
import org.apache.wink.common.internal.application.ApplicationValidator;
import org.apache.wink.common.internal.factory.OFFactoryRegistry;
import org.apache.wink.common.internal.factory.ObjectFactory;
import org.apache.wink.common.internal.runtime.RuntimeContext;
import org.apache.wink.common.internal.utils.GenericsUtils;


/**
 * <p>
 * The order of the providers is important. The later provider was added, the
 * higher priority it has. Thus, the default providers should be always added
 * before the custom or with lower priority.
 */
public class ProvidersRegistry  {

    private static final Log                                 logger             = LogFactory.getLog(ProvidersRegistry.class);
    
    private final ProducesMediaTypeMap<ContextResolver<?>>   contextResolvers   = new ProducesMediaTypeMap<ContextResolver<?>>(
                                                                                    ContextResolver.class);
    private final Set<ObjectFactory<ExceptionMapper<?>>>     exceptionMappers   = new TreeSet<ObjectFactory<ExceptionMapper<?>>>(
                                                                                    Collections.reverseOrder());
    private final ConsumesMediaTypeMap<MessageBodyReader<?>> messageBodyReaders = new ConsumesMediaTypeMap<MessageBodyReader<?>>(
                                                                                    MessageBodyReader.class);
    private final ProducesMediaTypeMap<MessageBodyWriter<?>> messageBodyWriters = new ProducesMediaTypeMap<MessageBodyWriter<?>>(
                                                                                    MessageBodyWriter.class);
    private final ApplicationValidator                       applicationValidator;
    private final OFFactoryRegistry                          factoryFactoryRegistry;
    private final Lock                                       readersLock;
    private final Lock                                       writersLock;

    public ProvidersRegistry(OFFactoryRegistry factoryRegistry,
        ApplicationValidator applicationValidator) {
        this.factoryFactoryRegistry = factoryRegistry;
        this.applicationValidator = applicationValidator;
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readersLock = readWriteLock.readLock();
        writersLock = readWriteLock.writeLock();
    }

    @SuppressWarnings("unchecked")
    public boolean addProvider(Class<?> cls, double priority) {
        writersLock.lock();
        try {
            ObjectFactory<?> objectFactory = factoryFactoryRegistry.getObjectFactory(cls);
            return addProvider(new PriorityObjectFactory(objectFactory, priority));
        } finally {
            writersLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public boolean addProvider(Object provider, double priority) {
        writersLock.lock();
        try {
            ObjectFactory<?> objectFactory = factoryFactoryRegistry.getObjectFactory(provider);
            return addProvider(new PriorityObjectFactory(objectFactory, priority));
        } finally {
            writersLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    private boolean addProvider(ObjectFactory<?> objectFactory) {
        Class<? extends Object> cls = objectFactory.getInstanceClass();

        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Processing provider of type %s", String.valueOf(cls)));
        }

        boolean retValue = false;

        if (!applicationValidator.isValidProvider(cls)) {
            return retValue;
        }

        if (ContextResolver.class.isAssignableFrom(cls)) {
            contextResolvers.putProvider((ObjectFactory<ContextResolver<?>>) objectFactory);
            retValue = true;
        }
        if (ExceptionMapper.class.isAssignableFrom(cls)) {
            exceptionMappers.add((ObjectFactory<ExceptionMapper<?>>) objectFactory);
            retValue = true;
        }
        if (MessageBodyReader.class.isAssignableFrom(cls)) {
            messageBodyReaders.putProvider((ObjectFactory<MessageBodyReader<?>>) objectFactory);
            retValue = true;
        }
        if (MessageBodyWriter.class.isAssignableFrom(cls)) {
            messageBodyWriters.putProvider((ObjectFactory<MessageBodyWriter<?>>) objectFactory);
            retValue = true;
        }
        if (retValue == false) {
            logger.warn(String.format("Unknown provider: %s", String.valueOf(cls)));
        }
        return retValue;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hp.symphony.internal.server.jaxrs.core.ProvidersRegistry#addProvider(
     * java.lang.Class)
     */
    public boolean addProvider(Class<?> cls) {
        return addProvider(cls, SymphonyApplication.DEFAULT_PRIORITY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.hp.symphony.internal.server.jaxrs.core.ProvidersRegistry#addProvider(
     * java.lang.Object)
     */
    public boolean addProvider(Object provider) {
        return addProvider(provider, SymphonyApplication.DEFAULT_PRIORITY);
    }

    @SuppressWarnings("unchecked")
    public <T> ContextResolver<T> getContextResolver(final Class<T> contextType,
        MediaType mediaType, RuntimeContext runtimeContext) {
        readersLock.lock();
        try {
            final List<ObjectFactory<ContextResolver<?>>> factories = contextResolvers.getProvidersByMediaType(
                mediaType, contextType);

            if (factories.isEmpty()) {
                return null;
            }

            if (factories.size() == 1) {
                return (ContextResolver<T>) factories.get(0).getInstance(runtimeContext);
            }

            // creates list of providers that is used by the proxy
            // this solution can be improved by creating providers inside the proxy
            // one-by-one and keeping them on the proxy
            // so a new provider will be created only when all the old providers
            // will return null
            final List<ContextResolver<?>> providers = new ArrayList<ContextResolver<?>>(
                factories.size());
            for (ObjectFactory<ContextResolver<?>> factory : factories) {
                providers.add(factory.getInstance(runtimeContext));
            }

            return (ContextResolver<T>) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[] { ContextResolver.class }, new InvocationHandler() {

                    public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable {
                        if (method.getName().equals("getContext") && args != null
                            && args.length == 1
                            && (args[0] == null || args[0].getClass().equals(Class.class))) {
                            for (ContextResolver<?> resolver : providers) {
                                Object context = resolver.getContext((Class<?>) args[0]);
                                if (context != null) {
                                    return context;
                                }
                            }
                            return null;
                        } else {
                            return method.invoke(proxy, args);
                        }
                    }
                });
        } finally {
            readersLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Throwable> ExceptionMapper<T> getExceptionMapper(Class<T> type,
        RuntimeContext runtimeContext) {
        readersLock.lock();
        try {
            List<ExceptionMapper<?>> matchingMappers = new ArrayList<ExceptionMapper<?>>();

            for (ObjectFactory<ExceptionMapper<?>> factory : exceptionMappers) {
                ExceptionMapper<?> exceptionMapper = factory.getInstance(runtimeContext);
                Type genericType = GenericsUtils.getGenericInterfaceParamType(
                    exceptionMapper.getClass(), ExceptionMapper.class);
                Class<?> classType = GenericsUtils.getClassType(genericType);
                if (classType.isAssignableFrom(type)) {
                    matchingMappers.add(exceptionMapper);
                }
            }

            if (matchingMappers.isEmpty()) {
                return null;
            }

            while (matchingMappers.size() > 1) {
                Type first = GenericsUtils.getGenericInterfaceParamType(
                    matchingMappers.get(0).getClass(), ExceptionMapper.class);
                Type second = GenericsUtils.getGenericInterfaceParamType(
                    matchingMappers.get(1).getClass(), ExceptionMapper.class);
                Class<?> firstClass = GenericsUtils.getClassType(first);
                Class<?> secondClass = GenericsUtils.getClassType(second);
                if (firstClass == secondClass) {
                    // the first one has higher priority, so remove the second one for the same classes!
                    matchingMappers.remove(1);
                } else if (firstClass.isAssignableFrom(secondClass)) {
                    matchingMappers.remove(0);
                } else {
                    matchingMappers.remove(1);
                }
            }

            return (ExceptionMapper<T>) matchingMappers.get(0);
        } finally {
            readersLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> MessageBodyReader<T> getMessageBodyReader(Class<T> type, Type genericType,
        Annotation[] annotations, MediaType mediaType, RuntimeContext runtimeContext) {
        readersLock.lock();
        try {
            List<ObjectFactory<MessageBodyReader<?>>> factories = messageBodyReaders.getProvidersByMediaType(
                mediaType, type);
            for (ObjectFactory<MessageBodyReader<?>> factory : factories) {
                MessageBodyReader<?> reader = factory.getInstance(runtimeContext);
                if (reader.isReadable(type, genericType, annotations, mediaType)) {
                    return (MessageBodyReader<T>) reader;
                }
            }
            return null;
        } finally {
            readersLock.unlock();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<T> type, Type genericType,
        Annotation[] annotations, MediaType mediaType, RuntimeContext runtimeContext) {
        readersLock.lock();
        try {
            List<ObjectFactory<MessageBodyWriter<?>>> writersFactories = messageBodyWriters.getProvidersByMediaType(
                mediaType, type);
            for (ObjectFactory<MessageBodyWriter<?>> factory : writersFactories) {
                MessageBodyWriter<?> writer = factory.getInstance(runtimeContext);
                if (writer.isWriteable(type, genericType, annotations, mediaType)) {
                    return (MessageBodyWriter<T>) writer;
                }
            }
            return null;
        } finally {
            readersLock.unlock();
        }
    }

    public Set<MediaType> getMessageBodyWriterMediaTypes(Class<?> type) {
        Set<MediaType> mediaTypes = messageBodyWriters.getProvidersMediaTypes(type);
        return mediaTypes;
    }

    private class ProducesMediaTypeMap<T> extends MediaTypeMap<T> {

        public ProducesMediaTypeMap(Class<?> rawType) {
            super(rawType);
        }

        public void putProvider(ObjectFactory<T> objectFactory) {
            Produces produces = objectFactory.getInstanceClass().getAnnotation(Produces.class);
            if (produces == null) {
                put(MediaType.WILDCARD_TYPE, objectFactory);
            } else {
                String[] values = produces.value();
                for (String val : values) {
                    put(MediaType.valueOf(val), objectFactory);
                }
            }
        }
    }

    private class ConsumesMediaTypeMap<T> extends MediaTypeMap<T> {

        public ConsumesMediaTypeMap(Class<?> rawType) {
            super(rawType);
        }

        public void putProvider(ObjectFactory<T> objectFactory) {
            Consumes consumes = objectFactory.getInstanceClass().getAnnotation(Consumes.class);
            if (consumes == null) {
                put(MediaType.WILDCARD_TYPE, objectFactory);
            } else {
                String[] values = consumes.value();
                for (String val : values) {
                    put(MediaType.valueOf(val), objectFactory);
                }
            }
        }
    }

    private abstract class MediaTypeMap<T> {

        private final Map<MediaType, Set<ObjectFactory<T>>> data = new LinkedHashMap<MediaType, Set<ObjectFactory<T>>>();
        private final Class<?>                              rawType;

        public MediaTypeMap(Class<?> rawType) {
            super();
            this.rawType = rawType;
        }

        /**
         * returns providers by mediaType and by
         * 
         * @param mediaType
         * @param type
         * @return
         */
        public List<ObjectFactory<T>> getProvidersByMediaType(MediaType mediaType, Class<?> type) {
            if (!mediaType.getParameters().isEmpty()) {
                mediaType = new MediaType(mediaType.getType(), mediaType.getSubtype());
            }
            List<ObjectFactory<T>> list = new ArrayList<ObjectFactory<T>>();
            Set<ObjectFactory<T>> set = data.get(mediaType);
            limitByType(list, set, type);
            if (!mediaType.getSubtype().equals("*")) {
                set = data.get(new MediaType(mediaType.getType(), "*"));
                limitByType(list, set, type);
                if (!mediaType.getType().equals("*")) {
                    set = data.get(MediaType.WILDCARD_TYPE);
                    limitByType(list, set, type);
                }
            }
            return list;
        }

        public Set<MediaType> getProvidersMediaTypes(Class<?> type) {
            Set<MediaType> mediaTypes = new HashSet<MediaType>();

            l1: for (Entry<MediaType, Set<ObjectFactory<T>>> entry : data.entrySet()) {
                MediaType mediaType = entry.getKey();
                Set<ObjectFactory<T>> set = entry.getValue();
                for (ObjectFactory<T> t : set) {
                    if (GenericsUtils.isGenericInterfaceAssignableFrom(type, t.getInstanceClass(),
                        rawType)) {
                        mediaTypes.add(mediaType);
                        continue l1;
                    }
                }
            }
            return mediaTypes;
        }

        private void limitByType(List<ObjectFactory<T>> list, Set<ObjectFactory<T>> set,
            Class<?> type) {
            if (set != null) {
                for (ObjectFactory<T> t : set) {
                    if (GenericsUtils.isGenericInterfaceAssignableFrom(type, t.getInstanceClass(),
                        rawType)) {
                        list.add(t);
                    }
                }
            }
        }

        void put(MediaType key, ObjectFactory<T> objectFactory) {
            if (!key.getParameters().isEmpty()) {
                key = new MediaType(key.getType(), key.getSubtype());
            }
            Set<ObjectFactory<T>> set = data.get(key);
            if (set == null) {
                set = new TreeSet<ObjectFactory<T>>(Collections.reverseOrder());
                data.put(key, set);
            }
            if (!set.add(objectFactory)) {
                logger.warn(String.format("The set already contains %s. Skipping...",
                    String.valueOf(objectFactory)));
            }
        }

        @Override
        public String toString() {
            return String.format("RawType: %s, Data: %s", String.valueOf(rawType), data.toString());
        }

    }

    private static class PriorityObjectFactory<T> implements ObjectFactory<T>,
        Comparable<PriorityObjectFactory<T>> {

        private final ObjectFactory<T> of;
        private final double           priority;

        public PriorityObjectFactory(ObjectFactory<T> of, double priority) {
            super();
            this.of = of;
            this.priority = priority;
        }

        public T getInstance(RuntimeContext context) {
            return of.getInstance(context);
        }

        public Class<T> getInstanceClass() {
            return of.getInstanceClass();
        }

        public int compareTo(PriorityObjectFactory<T> o) {
            int compare = Double.compare(priority, o.priority);
            // if the compare equals, the latest has the priority
            return compare == 0 ? -1 : compare;
        }

        @Override
        public String toString() {
            return String.format("Priority: %f, ObjectFactory: %s", priority, String.valueOf(of));
        }
    }

}