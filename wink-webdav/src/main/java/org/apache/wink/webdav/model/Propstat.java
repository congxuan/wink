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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.wink.common.http.HttpStatus;


/**
 * The <code>propstat</code> XML element per the WebDAV specification [RFC 4918]
 * 
 * <pre>
 *    Name:       propstat
 *    Namespace:  DAV:
 *    Purpose:    Groups together a prop and status element that is
 *    associated with a particular href element.
 *    Description: The propstat XML element MUST contain one prop XML
 *    element and one status XML element.  The contents of the prop XML
 *    element MUST only list the names of properties to which the result in
 *    the status element applies.
 * 
 *    &lt;!ELEMENT propstat (prop, status, responsedescription?) &gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prop", "status", "error", "responsedescription"})
@XmlRootElement(name = "propstat")
public class Propstat {

    @XmlElement(required = true)
    protected Prop prop;
    @XmlElement(required = true)
    protected String status;
    protected Error error;
    protected String responsedescription;

    /**
     * Gets the value of the prop property.
     * 
     * @return possible object is {@link Prop }
     * 
     */
    public Prop getProp() {
        return prop;
    }

    /**
     * Sets the value of the prop property.
     * 
     * @param value
     *            allowed object is {@link Prop }
     * 
     */
    public void setProp(Prop value) {
        this.prop = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStatus() {
        return status;
    }
    
    public int getStatusCode() {
        if (status == null) {
            return -1;
        }
        return HttpStatus.valueOfStatusLine(status).getCode();
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return possible object is {@link Error }
     * 
     */
    public Error getError() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *            allowed object is {@link Error }
     * 
     */
    public void setError(Error value) {
        this.error = value;
    }

    /**
     * Gets the value of the responsedescription property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getResponsedescription() {
        return responsedescription;
    }

    /**
     * Sets the value of the responsedescription property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setResponsedescription(String value) {
        this.responsedescription = value;
    }
    
}