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
package org.apache.jdo.tck.pc.building;

import java.io.Serializable;

/** Representation of an oven in a fitted kitchen. */
public class Oven implements Serializable {

  private static final long serialVersionUID = 1L;

  /** Manufacturer of the appliance. */
  protected String make;

  /** Model of this appliance. */
  protected String model;

  public Oven(String make, String model) {
    setMake(make);
    setModel(model);
  }

  public void setMake(String make) {
    this.make = make;
  }

  public String getMake() {
    return make;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getModel() {
    return model;
  }

  public boolean equals(Object that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }
    if (!(that instanceof Oven)) {
      return false;
    }
    return equals((Oven) that);
  }

  public boolean equals(Oven that) {
    if (this == that) {
      return true;
    }
    if (that == null) {
      return false;
    }

    if ((make == null && that.make != null) || (make != null && that.make == null)) {
      return false;
    }
    if ((model == null && that.model != null) || (model != null && that.model == null)) {
      return false;
    }
    return this.make.equals(that.make) && this.model.equals(that.make);
  }

  public int hashCode() {
    return (make != null ? make.hashCode() : 0) ^ (model != null ? model.hashCode() : 0);
  }
}
