/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
<<<<<<< Updated upstream
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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

/*
 * ConcreteTestIdentity.java
 *
 */

package javax.jdo.identity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author clr
 */
public class ConcreteTestIdentity extends SingleFieldIdentity<ConcreteTestIdentity> {

  private static final long serialVersionUID = 1L;

  ConcreteTestIdentity(Class<?> cls) {
    super(cls);
  }

  /** This constructor is only for Serialization */
  public ConcreteTestIdentity() {
    super();
  }

  /**
   * Determine the ordering of identity objects.
   *
   * @param o Other identity
   * @return The relative ordering between the objects
   * @since 2.2
   */
  public int compareTo(ConcreteTestIdentity o) {
    throw new UnsupportedOperationException("Not implemented");
  }

  public void writeExternal(ObjectOutput out) throws IOException {
    super.writeExternal(out);
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    super.readExternal(in);
  }
}
