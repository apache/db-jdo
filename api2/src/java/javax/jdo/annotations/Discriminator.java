/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
 * Annotation for the discriminator of the class.
 * Maps across to the JDO2 element "discriminator" of the "inheritance" element.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Discriminator
{
    /**
     * Strategy to use for discriminator. The discriminator determines
     * the class associated with a row in the datastore.
     * @return Strategy to use for discriminator.
     */
    DiscriminatorStrategy strategy() 
        default DiscriminatorStrategy.UNKNOWN;

    /**
     * Whether the discriminator is indexed.
     * @return Whether the discriminator is indexed.
     */
    String indexed() default "";

    /**
     * Name of the column for the discriminator
     * @return Name of the column for the discriminator
     */
    String column() default "";

    /**
     * The value for the discriminator for objects of this class 
     * when using "value-map" strategy.
     * @return The value for the discriminator for objects of this class 
     * when using "value-map" strategy.
     */
    String value() default "";

    /**
     * The column(s) making up the discriminator.
     * @return The column(s) making up the discriminator.
     */
    Column[] columns() default {};
}
