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

package org.apache.jdo.tck.pc.singlefieldidentity;

import java.io.Serializable;
import java.util.Date;

import javax.jdo.identity.SingleFieldIdentity;

/**
 * This is the super class for all PCPoint single indentity classes. It defines an abstract method
 * <code>equals(SingleFieldIdentity)</code>. That method ensures that all single identity test cases
 * have a common implementation.
 *
 * @author Michael Watzek
 */
public abstract class AbstractPCPointSingleField implements Serializable {

  /** This field is used by subclasses to compute the value of the primary key field. */
  static long counter = new Date().getTime();

  /**
   * Returns <code>true</code> if the given the key of the given <code>SingleFieldIdentity</code>
   * instance equals the key in the subclass of this class.
   *
   * @param singleFieldIdentity the single field identity to check.
   * @return returns <code>true</code> if the given the key of the given <code>SingleFieldIdentity
   *     </code> instance equals the key in the subclass of this class.
   */
  public abstract boolean equalsPKField(SingleFieldIdentity singleFieldIdentity);

  public String toString() {
    return '(' + getClass().getName() + ')';
  }
}
