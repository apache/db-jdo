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
 * Annotation for the versioning of the class.
 * Maps across to the JDO2 element "version" of the "class" element.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface Version
{
    /**
     * Strategy for versioning of objects of this class
     * @return Strategy for versioning objects of this class
     */
    VersionStrategy strategy();

    /**
     * Name of the column for the version
     * @return Name of the column for the version
     */
    String column() default "";

    /**
     * Whether the version column(s) is indexed
     * @return Whether the version column(s) is indexed
     */
    String indexed() default "";

    /**
     * The column(s) making up the version.
     * @return The column(s) making up the version.
     */
    Column[] columns() default {};
}
