/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
=======
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
>>>>>>> Stashed changes
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.lifecycle;

import java.io.Serializable;

public class StateTransitionObj implements Serializable {

  private static final long serialVersionUID = 1L;

  private static int counter = 0;

  private int id;
  private int int_field;

  private transient int nonmanaged_field;

  public StateTransitionObj() {
    id = ++counter;
    int_field = 0;
  }

  public StateTransitionObj(int v) {
    this();
    int_field = v;
  }

  public int readField() {
    int value = int_field;
    return value;
  }

  public void writeField(int value) {
    int_field = value;
  }

  public int readNonmanagedField() {
    return nonmanaged_field;
  }

  public void writeNonmanagedField(int value) {
    nonmanaged_field = value;
  }
  /**
   * @return Returns the id.
   */
  public int getId() {
    return id;
  }
  /**
   * @param id The id to set.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * The class to be used as the application identifier for the <code>StateTransitionObj</code>
   * class.
   */
  public static class Oid implements Serializable, Comparable<Oid> {

    private static final long serialVersionUID = 1L;

    /**
     * This field is part of the identifier and should match in name and type with a field in the
     * <code>StateTransitionObj</code> class.
     */
    public int id;

    /** The required public no-arg constructor. */
    public Oid() {}

    /**
     * Initialize the identifier.
     *
     * @param id the id.
     */
    public Oid(int id) {
      this.id = id;
    }

    public Oid(String s) {
      id = Integer.parseInt(justTheId(s));
    }

    public String toString() {
      return this.getClass().getName() + ": " + id;
    }

    /** */
    public boolean equals(Object obj) {
      if (obj == null || !this.getClass().equals(obj.getClass())) return false;
      Oid o = (Oid) obj;
      if (this.id != o.id) return false;
      return true;
    }

    /** */
    public int hashCode() {
      return id;
    }

    protected static String justTheId(String str) {
      return str.substring(str.indexOf(':') + 1);
    }

    /** */
    public int compareTo(Oid obj) {
      return id - obj.id;
    }
  }
}
