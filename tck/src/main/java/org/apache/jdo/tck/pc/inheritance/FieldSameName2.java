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
public class FieldSameName2 extends FieldSameName {
  char n1; // not managed
  boolean n2; // not managed
  float n3; // transactional

  public FieldSameName2() {
    n1 = '3';
    n2 = false;
    n3 = -4.4f;
  }

  public FieldSameName2(
      int intA, double doubleB, int intB, char charC, boolean booleanVal, float floatE) {
    super(intA, doubleB, intB);
    n1 = charC;
    n2 = booleanVal;
    n3 = floatE;
  }

  public void setCharC(char charC) {
    n1 = charC;
  }

  public char getCharC() {
    return n1;
  }

  public void setBooleanD(boolean booleanD) {
    n2 = booleanD;
  }

  public boolean getBooleanD() {
    return n2;
  }

  public void setFloatE(float floatE) {
    n3 = floatE;
  }

  public float getFloatE() {
    return n3;
  }
}
