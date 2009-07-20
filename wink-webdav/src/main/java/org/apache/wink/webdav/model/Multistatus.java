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
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.1-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.12.04 at 02:20:17 PM IST 
//

package org.apache.wink.webdav.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>multistatus</code> XML element per the WebDAV specification [RFC
 * 4918]
 * 
 * <pre>
 *    Name:       multistatus
 *    Namespace:  DAV:
 *    Purpose:    Contains multiple response messages.
 *    Description: The responsedescription at the top level is used to
 *    provide a general message describing the overarching nature of the
 *    response.  If this value is available an application may use it
 *    instead of presenting the individual response descriptions contained
 *    within the responses.
 * 
 *    &lt;!ELEMENT multistatus (response+, responsedescription?) &gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"response", "responsedescription"})
@XmlRootElement(name = "multistatus")
public class Multistatus {

    @XmlElement(required = true)
    protected List<Response> response;
    protected String         responsedescription;

    /**
     * Gets the value of the response property.
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the response property.
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getResponse().add(newItem);
     * </pre>
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Response }
     */
    public List<Response> getResponse() {
        if (response == null) {
            response = new ArrayList<Response>();
        }
        return this.response;
    }

    /**
     * Get a map of responses where the key is the response href
     * 
     * @return a map of responses
     */
    public Map<String, Response> getResponsesAsMapByHref() {
        Map<String, Response> map = new HashMap<String, Response>();
        for (Response response : getResponse()) {
            for (String responseHref : response.getHref()) {
                map.put(responseHref, response);
            }
        }
        return map;
    }

    /**
     * Get a the first response available that matches the provided href
     * 
     * @param href the href of the response to get
     * @return thr first response that has the provided href
     */
    public Response getResponseByHref(String href) {
        for (Response response : getResponse()) {
            for (String responseHref : response.getHref()) {
                if (responseHref.equals(href))
                    return response;
            }
        }
        return null;
    }

    /**
     * Gets the value of the responsedescription property.
     * 
     * @return possible object is {@link String }
     */
    public String getResponsedescription() {
        return responsedescription;
    }

    /**
     * Sets the value of the responsedescription property.
     * 
     * @param value allowed object is {@link String }
     */
    public void setResponsedescription(String value) {
        this.responsedescription = value;
    }

    /**
     * Unmarshal a Multistatus object from the provided input stream
     * 
     * @param is the input stream
     * @return an instance of a Multistatus object
     * @throws IOException
     */
    public static Multistatus unmarshal(InputStream is) throws IOException {
        return unmarshal(new InputStreamReader(is));
    }

    /**
     * Marshal a Multistatus object to the provided output stream
     * 
     * @param instance the Multistatus instance to marshal
     * @param os the output stream
     * @throws IOException
     */
    public static void marshal(Multistatus instance, OutputStream os) throws IOException {
        marshal(instance, new OutputStreamWriter(os));
    }

    /**
     * Unmarshal a Multistatus object from the provided reader
     * 
     * @param reader the input reader
     * @return an instance of a Multistatus object
     * @throws IOException
     */
    public static Multistatus unmarshal(Reader reader) throws IOException {
        Unmarshaller unmarshaller = WebDAVModelHelper.createUnmarshaller();
        Multistatus instance =
            WebDAVModelHelper.unmarshal(unmarshaller, reader, Multistatus.class, "multistatus");
        return instance;
    }

    /**
     * Marshal a Multistatus object to the provided writer
     * 
     * @param instance the Multistatus instance to marshal
     * @param writer the output writer
     * @throws IOException
     */
    public static void marshal(Multistatus instance, Writer writer) throws IOException {
        Marshaller marshaller = WebDAVModelHelper.createMarshaller();
        WebDAVModelHelper.marshal(marshaller, instance, writer, "multistatus");
    }

}
