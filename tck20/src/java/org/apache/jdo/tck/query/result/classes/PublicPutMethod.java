/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 
package org.apache.jdo.tck.query.result.classes;

import java.util.HashMap;
import java.util.Map;

/**
 * JDOQL result class having a public lonf field.
 */
public class PublicPutMethod {

    private Map map = new HashMap();
    
    public PublicPutMethod() {}
    
    public PublicPutMethod(Map map) {
        this.map = map;
    }
    
    public void put(Object key, Object value) {
        this.map.put(key, value);
    }
    
    /**
     * @see Object#hashCode()
     */
    public int hashCode() {
        return this.map.hashCode();
    }
    
    /**
     * @see Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        if (!(o instanceof PublicPutMethod))
            return false;
        PublicPutMethod other = (PublicPutMethod) o;
        return this.map.equals(other.map);
    }
    
    /**
     * @see Object#toString()
     */
    public String toString() {
        return getClass().getName() + '(' + this.map + ')';
    }

}
