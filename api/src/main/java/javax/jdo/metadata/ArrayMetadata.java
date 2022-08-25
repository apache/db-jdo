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
package javax.jdo.metadata;

/**
 * Represents details of an array in a field/property in a class.
 *
 * @since 3.0
 */
public interface ArrayMetadata extends Metadata {
  /**
   * Method to set the name of the element type.
   *
   * @param type Name of the element type
   * @return This metadata object
   */
  ArrayMetadata setElementType(String type);

  /**
   * Accessor for the element type.
   *
   * @return The element type
   */
  String getElementType();

  /**
   * Method to set whether the element is embedded.
   *
   * @param val Whether it is embedded
   * @return This metadata object
   */
  ArrayMetadata setEmbeddedElement(boolean val);

  /**
   * Accessor for whether the element is embedded.
   *
   * @return whether the element is embedded
   */
  Boolean getEmbeddedElement();

  /**
   * Method to set whether the element is serialised.
   *
   * @param val Whether it is serialised
   * @return This metadata object
   */
  ArrayMetadata setSerializedElement(boolean val);

  /**
   * Accessor for whether the element is serialised.
   *
   * @return whether the element is serialised
   */
  Boolean getSerializedElement();

  /**
   * Method to set whether the element is dependent.
   *
   * @param val Whether it is dependent
   * @return This metadata object
   */
  ArrayMetadata setDependentElement(boolean val);

  /**
   * Accessor for whether the element is dependent.
   *
   * @return whether the element is dependent
   */
  Boolean getDependentElement();
}
