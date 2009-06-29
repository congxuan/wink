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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.05.24 at 01:47:17 PM IDT 
//


package org.apache.wink.common.internal.model.admin;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="method" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="uri" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element ref="{http://hp.org/symphony/model/admin}accept-media-types" minOccurs="0"/>
 *         &lt;element ref="{http://hp.org/symphony/model/admin}produced-media-types" minOccurs="0"/>
 *         &lt;element ref="{http://hp.org/symphony/model/admin}query-parameters" minOccurs="0"/>
 *         &lt;element ref="{http://hp.org/symphony/model/admin}matrix-parameters" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "type",
    "method",
    "uri",
    "acceptMediaTypes",
    "producedMediaTypes",
    "queryParameters",
    "matrixParameters"
})
@XmlRootElement(name = "sub-resource")
public class SubResource {

    @XmlElement(required = true)
    protected String type;
    protected String method;
    @XmlElement(required = true)
    protected String uri;
    @XmlElement(name = "accept-media-types")
    protected AcceptMediaTypes acceptMediaTypes;
    @XmlElement(name = "produced-media-types")
    protected ProducedMediaTypes producedMediaTypes;
    @XmlElement(name = "query-parameters")
    protected QueryParameters queryParameters;
    @XmlElement(name = "matrix-parameters")
    protected MatrixParameters matrixParameters;

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the method property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMethod() {
        return method;
    }

    /**
     * Sets the value of the method property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMethod(String value) {
        this.method = value;
    }

    /**
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUri() {
        return uri;
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUri(String value) {
        this.uri = value;
    }

    /**
     * Gets the value of the acceptMediaTypes property.
     * 
     * @return
     *     possible object is
     *     {@link AcceptMediaTypes }
     *     
     */
    public AcceptMediaTypes getAcceptMediaTypes() {
        return acceptMediaTypes;
    }

    /**
     * Sets the value of the acceptMediaTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link AcceptMediaTypes }
     *     
     */
    public void setAcceptMediaTypes(AcceptMediaTypes value) {
        this.acceptMediaTypes = value;
    }

    /**
     * Gets the value of the producedMediaTypes property.
     * 
     * @return
     *     possible object is
     *     {@link ProducedMediaTypes }
     *     
     */
    public ProducedMediaTypes getProducedMediaTypes() {
        return producedMediaTypes;
    }

    /**
     * Sets the value of the producedMediaTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProducedMediaTypes }
     *     
     */
    public void setProducedMediaTypes(ProducedMediaTypes value) {
        this.producedMediaTypes = value;
    }

    /**
     * Gets the value of the queryParameters property.
     * 
     * @return
     *     possible object is
     *     {@link QueryParameters }
     *     
     */
    public QueryParameters getQueryParameters() {
        return queryParameters;
    }

    /**
     * Sets the value of the queryParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryParameters }
     *     
     */
    public void setQueryParameters(QueryParameters value) {
        this.queryParameters = value;
    }

    /**
     * Gets the value of the matrixParameters property.
     * 
     * @return
     *     possible object is
     *     {@link MatrixParameters }
     *     
     */
    public MatrixParameters getMatrixParameters() {
        return matrixParameters;
    }

    /**
     * Sets the value of the matrixParameters property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatrixParameters }
     *     
     */
    public void setMatrixParameters(MatrixParameters value) {
        this.matrixParameters = value;
    }

}