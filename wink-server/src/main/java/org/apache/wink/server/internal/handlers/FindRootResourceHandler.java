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
 

package org.apache.wink.server.internal.handlers;

import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wink.server.handlers.HandlersChain;
import org.apache.wink.server.handlers.MessageContext;
import org.apache.wink.server.handlers.RequestHandler;
import org.apache.wink.server.internal.registry.ResourceInstance;
import org.apache.wink.server.internal.registry.ResourceRegistry;


public class FindRootResourceHandler implements RequestHandler {
    
    public static final String SEARCH_POLICY_CONTINUED_SEARCH_KEY = "symphony.searchPolicyContinuedSearch";
    private static final Log logger = LogFactory.getLog(FindRootResourceHandler.class);
    
    private boolean isContinuedSearchPolicy;

    public void handleRequest(MessageContext context, HandlersChain chain) throws Throwable {
        ResourceRegistry registry = context.getAttribute(ResourceRegistry.class);
        
        // create a path stripped from all matrix parameters to use for matching
        List<PathSegment> segments = context.getUriInfo().getPathSegments(false);
        String strippedPath = buildPathForMatching(segments);
        
        // get a list of root resources that can handle the request
        List<ResourceInstance> matchedResources = registry.getMatchingRootResources(strippedPath);
        if (matchedResources.size() == 0) {
            logger.warn("No resource found matching " + context.getUriInfo().getPath(false));
            SearchResult result = new SearchResult(new WebApplicationException(Response.Status.NOT_FOUND));
            context.setAttribute(SearchResult.class, result);
            return;
        }

        // JAX-RS specification requires to search only the first matching resource,
        // but the continued search behavior is to continue searching in all matching resources
        List<ResourceInstance> searchableResources = new LinkedList<ResourceInstance>();
        if (!isContinuedSearchPolicy) {
            // strict behavior - search only in the first matched root resource, as per the JAX-RS
            // specification
            searchableResources.add(matchedResources.get(0));
        } else {
            // continued search behavior - search in all matched resources
            searchableResources.addAll(matchedResources);
        }

        // search through all the matched resources (or just the first one)
        for (ResourceInstance resource : searchableResources) {
            // save the matched variables, resource and uri
            SearchResult result = new SearchResult(resource, context.getUriInfo());
            context.setAttribute(SearchResult.class, result);
            resource.getMatcher().storeVariables(result.getData().getMatchedVariables(), false);
            int headSegmentsCount = result.getData().addMatchedURI(resource.getMatcher().getHead(false));
            resource.getMatcher().storeVariablesPathSegments(segments, 0, headSegmentsCount, result.getData().getMatchedVariablesPathSegments());
                      
            // continue that chain to find the actual resource that will handle the request.
            // it may be the current resource or a sub-resource of the current resource. 
            chain.doChain(context);
            
            // check the result to see if we found a match
            if (result.isFound()) {
                break;
            }
        }
    }

    private String buildPathForMatching(List<PathSegment> segments) {
        StringBuilder strippedPathBuilder = new StringBuilder();
        String delim = "";
        for (PathSegment segment : segments) {
            strippedPathBuilder.append(delim);
            strippedPathBuilder.append(segment.getPath());
            delim = "/";
        }
        String strippedPath = strippedPathBuilder.toString();
        return strippedPath;
    }
    
    public void init(Properties props) {
        isContinuedSearchPolicy = Boolean.valueOf(props.getProperty(SEARCH_POLICY_CONTINUED_SEARCH_KEY));
    }

}