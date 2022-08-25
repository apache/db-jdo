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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for a named query.
 * Corresponds to the xml element "query".
 * 
 * @version 3.2
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Queries.class)
public @interface Query
{
    /** Name of the query (mandatory)
     * @return the name of the query
     */
    String name();

    /** The query string (mandatory)
     * @return the query string
     */
    String value();

    /** The query language
     * @return the query language
     */
    String language() default "JDOQL";

    /** Whether the query is unmodifiable.
     * @return whether the query is unmodifiable
     */
    String unmodifiable() default "";

    /** Whether the query returns a single unique result.
     * @return whether the query returns a single unique result
     */
    String unique() default "";

    /** Result class into which to put the results.
     * @return the class of the result
     */
    Class<?> resultClass() default void.class;

    /** The name of the fetch plan used by this query
     * @return the fetch plan
     */
    String fetchPlan() default "";

    /** Vendor extensions.
     * @return the vendor extensions
     */
    Extension[] extensions() default {};
}
