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

package org.apache.jdo.tck.pc.inheritance;

/** */
public class FieldSameName3 extends FieldSameName2 {
  short n1; // persistent
  FieldSameName4 n2; // transactional
  FieldSameName4 n3; // persistent

  public FieldSameName3() {
    n1 = 1238;
  }

  public FieldSameName3(
      int intA,
      double doubleB,
      int intB,
      char charC,
      boolean booleanD,
      float floatE,
      short shortVal) {
    super(intA, doubleB, intB, charC, booleanD, floatE);
    n1 = shortVal;
  }

  public void setShortF(short shortF) {
    n1 = shortF;
  }

  public short getShortF() {
    return n1;
  }

  public void setSecondObj(FieldSameName4 secondObj) {
    n2 = secondObj;
  }

  public FieldSameName4 getSecondObj() {
    return n2;
  }

  public void setThirdObj(FieldSameName4 thirdObj) {
    n3 = thirdObj;
  }

  public FieldSameName4 getThirdObj() {
    return n3;
  }
}
