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
 * Annotation for the table where a one-to-many, many-to-many relation can be stored (join table).
 * 
 * @version 2.1
 * @since 2.1
 */
@Target({ElementType.FIELD, ElementType.METHOD}) @Retention(RetentionPolicy.RUNTIME)
public @interface JoinTable
{
    /** Table to use for persisting the join information. */
    String table() default "";

    /** Catalog to use for persisting the join information. */
    String catalog() default "";

    /** Schema to use for persisting the join information. */
    String schema() default "";
}