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
 * Annotation for the fetch group of a class.
 * Corresponds to the xml element "fetch-group".
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FetchGroups.class)
public @interface FetchGroup
{
    /**
     * Name of the fetch group.
     * @return the name of the fetch group
     */
    String name() default "";

    /**
     * Whether we should load this group as part of the post load process.
     * @return whether we should load this group as part of the post load 
     * process.
     */
    String postLoad() default "";

    /**
     * Members (fields and properties) of the fetch group. The members
     * should contain only name and recursionDepth.
     * @return members for the fetch group
     */
    Persistent[] members();

    /** 
     * Fetch groups to be nested (included) in this fetch group.
     * @return The nested fetch groups in this fetch group
     */
    String[] fetchGroups() default {};
}
