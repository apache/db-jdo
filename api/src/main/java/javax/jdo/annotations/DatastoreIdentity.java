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
package javax.jdo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the datastore identity of the class. Corresponds to the xml element
 * "datastore-identity" of the "class" element.
 *
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DatastoreIdentity {
  /**
   * Strategy to use when generating datastore identities
   *
   * @return Strategy to use when generating datastore identities
   */
  IdGeneratorStrategy strategy() default IdGeneratorStrategy.UNSPECIFIED;

  /**
   * Custom strategy to use to generate the value for the identity. If customStrategy is non-empty,
   * then strategy must be UNSPECIFIED.
   *
   * @return the custom strategy
   */
  String customStrategy() default "";

  /**
   * Name of sequence to use when the strategy involves sequences
   *
   * @return Name of sequence to use when the strategy involves sequences
   */
  String sequence() default "";

  /**
   * Name of the column for the datastore identity
   *
   * @return Name of the column for the datastore identity
   */
  String column() default "";

  /**
   * The column(s) making up the datastore identity.
   *
   * @return The column(s) making up the datastore identity.
   */
  Column[] columns() default {};

  /**
   * Vendor extensions.
   *
   * @return the vendor extensions
   */
  Extension[] extensions() default {};
}
