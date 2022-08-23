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

import javax.jdo.AttributeConverter;

/**
 * Specifies that a given type should be converted before being stored to, and after being retrieved
 * from the datastore using the given {@link AttributeConverter}.
 *
 * <p>If this annotation is placed on a type, then the conversion applies to all fields or
 * properties whose types match the entity type of the given {@link AttributeConverter}. Any {@link
 * Convert} annotations placed on members overrides any type-level conversion specifications.
 *
 * <p>If this annotation is placed on a field or property, the annotated attribute's type must be
 * assignment-compatible with the {@link AttributeConverter}'s entity type argument.
 *
 * @version 3.2
 * @since 3.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Convert {

  /**
   * The {@link AttributeConverter} to use for conversion.
   *
   * @return Converter class to use
   */
  @SuppressWarnings("rawtypes")
  Class<? extends AttributeConverter> value();

  /**
   * Whether this conversion is enabled. True by default. Setting this to false allows disabling
   * conversion that was specified at PMF level.
   *
   * @return Whether the PMF default converter is enabled
   */
  boolean enabled() default true;
}
