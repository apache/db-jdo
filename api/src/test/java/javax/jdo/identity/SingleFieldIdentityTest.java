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
 * SingleFieldIdentityTest.java
 *
 */

package javax.jdo.identity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.jdo.util.AbstractTest;
import javax.jdo.util.BatchTestRunner;

/** */
public class SingleFieldIdentityTest extends AbstractTest {

  ConcreteTestIdentity cti1;
  ConcreteTestIdentity cti2;
  ConcreteTestIdentity cti3;

  Object scti1;
  Object scti2;
  Object scti3;

  /** Creates a new instance of SingleFieldIdentityTest */
  public SingleFieldIdentityTest() {}

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    BatchTestRunner.run(SingleFieldIdentityTest.class);
  }

  public void testConstructor() {
    cti1 = new ConcreteTestIdentity(Object.class);
    cti2 = new ConcreteTestIdentity(Object.class);
    cti3 = new ConcreteTestIdentity(Class.class);

    assertEquals("Equal identity instances compare not equal.", cti1, cti2);
    if (cti1.equals(cti3)) fail("Not equal identity instances compare equal.");
  }

  public void testSerialized() {
    cti1 = new ConcreteTestIdentity(Object.class);
    cti2 = new ConcreteTestIdentity(Object.class);
    cti3 = new ConcreteTestIdentity(Class.class);
    Object[] sctis = writeReadSerialized(new Object[] {cti1, cti2, cti3});
    scti1 = sctis[0];
    scti2 = sctis[1];
    scti3 = sctis[2];
    assertEquals("Deserialized instance compare not equal.", cti1, scti1);
    assertEquals("Deserialized instance compare not equal.", cti2, scti2);
    assertEquals("Deserialized instance compare not equal.", cti3, scti3);
    assertEquals("Deserialized instance compare not equal.", scti1, cti1);
    assertEquals("Deserialized instance compare not equal.", scti2, cti2);
    assertEquals("Deserialized instance compare not equal.", scti3, cti3);
    if (scti1.equals(scti3)) fail("Not equal identity instances compare equal.");
  }

  protected Object[] writeReadSerialized(Object[] in) {
    int length = in.length;
    Object[] result = new Object[length];
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      for (int i = 0; i < length; ++i) {
        oos.writeObject(in[i]);
      }
      byte[] ba = baos.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(ba);
      ObjectInputStream ois = new ObjectInputStream(bais);
      for (int i = 0; i < length; ++i) {
        result[i] = ois.readObject();
      }
    } catch (Exception e) {
      fail(e.toString());
    }
    return result;
  }
}
