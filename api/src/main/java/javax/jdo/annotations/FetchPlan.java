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
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for the fetch plan of a persistence manager, query, or extent.
 * Corresponds to the xml element "fetch-plan".
 * 
 * @version 3.2
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FetchPlans.class)
public @interface FetchPlan
{
    /**
     * Name of the fetch plan.
     * @return the name of the fetch plan
     */
    String name() default "";

    /**
     * The fetch groups in this fetch plan.
     * @return the fetch groups 
     */
    String[] fetchGroups() default {};

    /**
     * The depth of references to instantiate, starting with the root object.
     * @return the maxium fetch depth
     */
    int maxFetchDepth() default 1;

    /** 
     * The number of instances of multi-valued fields retrieved by queries.
     * @return the fetch size
     */
    int fetchSize() default 0;
}
