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

/*
 * PCObjectKey.java
 *
 * Created on April 15, 2003, 5:34 PM
 */

package org.apache.jdo.pc.appid;

/**
 *
 * @author  Craig Russell
 */
public class PCObjectKey {
    
    Integer key;
    
    /** Creates a new instance of PCObjectKey */
    public PCObjectKey(Integer key) {
        this.key = key;
    }
    
    public PCObjectKey(String s) {
        if (s != null) {
            String number = s.substring(s.indexOf(":") + 1);
            if (number != null) {
                try {
                    key = new Integer(number);
                } catch (NumberFormatException ex) {
                }
            }
        }
    }
    
    public PCObjectKey() {
        this.key = null;
    }
    
    public String toString() {
        return "PCObjectKey; key value is:" + (key==null?"null":key.toString());
    }
    
    public static class Oid {
        public Integer key;

        public Oid() {
        }

        public Oid(String s) { 
            if (s != null) {
                String number = s.substring(s.indexOf(":") + 1);
                if (number != null) {
                    try {
                        key = new Integer(number);
                    } catch (NumberFormatException ex) {
                    }
                }
            }
        }

        public String toString() { 
            return "PCObjectKey$Oid; key value is:" + (key==null?"null":key.toString());
        }

        public int hashCode() { return key.hashCode() ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return this.key.equals(k.key);
            }
            return false;
        }

    }

        
}
