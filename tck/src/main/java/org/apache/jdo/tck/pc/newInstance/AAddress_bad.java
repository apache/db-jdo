/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jdo.tck.pc.newInstance;

/**
 * This interface represents the persistent state of Address. Javadoc was deliberately omitted
 * because it would distract from the purpose of the interface.
 */
public abstract class AAddress_bad implements IAddress {

  public abstract long getAddrid();

  public abstract String getStreet();

  public abstract String getCity();

  public abstract String getState();

  public abstract String getZipcode();

  public abstract String getCountry();

  // Lacks setter, so not a persistent property
  //    Expect JDOUserException on pm.newInstance(this)
  public abstract String getAString();

  public abstract void setAddrid(long addrid);

  public abstract void setStreet(String street);

  public abstract void setCity(String city);

  public abstract void setState(String state);

  public abstract void setZipcode(String zipcode);

  public abstract void setCountry(String country);
}
