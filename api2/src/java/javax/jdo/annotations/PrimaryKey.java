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
 * Annotation for the primary key of a class.
 * Maps across to the JDO2 element "primary-key".
 * Also used to define a field as being (part of) the primary key.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD}) 
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKey
{
    /**
     * Name of the primary key constraint
     * @return the name of the primary key constraint
     */
    String name() default "";

    /**
     * Name of the column to use for the primary key
     * @return the name of the column to use for the primary key
     */
    String column() default "";

    /**
     * The column(s) for the primary key
     * @return the column(s) for the primary key
     */
    Column[] columns() default {};
}