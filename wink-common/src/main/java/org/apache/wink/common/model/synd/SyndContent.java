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
 

package org.apache.wink.common.model.synd;

public class SyndContent extends SyndSimpleContent {

    private String type;
    private String src;
    
    public SyndContent() {}
    
    /**
     * Creates a new SyndContent with the specified value and 'text' type attribute. 
     * @param value the value of the text construct
     */
    public SyndContent(String value) {
        this(value, SyndTextType.text.name(), false);
    }
    
    /**
     * Creates a new SyndContent with the specified value and type attribute. 
     * @param value the value of the text construct
     * @param type the type attribute
     */
    // TODO: Michael: uncomment this constructor   
//    public SyndContent(String value, String type) {
//        this(value, type, false);
//    }
    
    /**
     * Creates a new SyndContent with the specified value or src, and type attribute. 
     * @param value the value of the text construct, or the value of the src attribute. 
     * @param type the type attribute
     * @param isSrc true indicates that the value parameter is the value of the src attribute
     */
    public SyndContent(String value, String type, boolean isSrc) {
        super(isSrc ? null : value);
        this.type = type;
        this.src = isSrc ? value : null;
    }

    public SyndContent(SyndContent other) {
        super(other);
        this.type = other.type;
        this.src = other.src;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getSrc() {
        return src;
    }
    
    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((src == null) ? 0 : src.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SyndContent other = (SyndContent) obj;
        if (src == null) {
            if (other.src != null)
                return false;
        } else if (!src.equals(other.src))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }
    


}