/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
 


package org.apache.jdo.tck.pc.fieldtypes;

import java.util.Date;
import java.io.Serializable;

public class SimpleClass implements Serializable, SimpleInterface, Comparable {

    private static long counter = new Date().getTime();
    private static long newId() {
        synchronized (SimpleClass.class) {
            return counter++;
        }
    }
    private long id = newId();
  private int intField=0;
  private String stringField = new String("Test");
  public SimpleClass(){};
  public SimpleClass(int intField, String stringField)
  {
    this.intField = intField;
    this.stringField = stringField;
  }

  public void setIntValue(int intField)
  {
    this.intField = intField;
  }

  public int getIntValue()
  {
    return intField;
  }

  public void setStringValue(String stringField)
  {
    this.stringField = stringField;
  }

  public String getStringValue()
  {
    return stringField;
  }

  public boolean equals(Object obj)
  {
    if(!(obj instanceof SimpleClass))
      return false;

    if (intField == ((SimpleClass)obj).getIntValue()
    && stringField.equals(((SimpleClass)obj).getStringValue()))
      return true;
    else
      return false;
  }

  public int hashCode()
  {
    return intField ^ stringField.hashCode();
  }

  public int compareTo(Object p)
  {
      SimpleClass sc = (SimpleClass) p;
      if( intField < sc.intField ) return -1;
      if( intField > sc.intField ) return 1;
      return stringField.compareTo(sc.stringField);
  }
  
    public static class Oid implements Serializable, Comparable {
        public long id;

        public Oid() {
        }

        public Oid(String s) { id = Integer.parseInt(justTheId(s)); }

        public String toString() { return this.getClass().getName() + ": "  + id;}

        public int hashCode() { return (int)id ; }

        public boolean equals(Object other) {
            if (other != null && (other instanceof Oid)) {
                Oid k = (Oid)other;
                return k.id == this.id;
            }
            return false;
        }
        
        protected static String justTheId(String str) {
            return str.substring(str.indexOf(':') + 1);
        }

        public int compareTo(Object p){
            // may throw ClassCastException which the user must handle
            Oid other = (Oid) p;
            if( id < other.id ) return -1;
            if( id > other.id ) return 1;
            return 0;
        }
          
    }   
}
