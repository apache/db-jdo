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
 * Corresponds to the xml element "version" of the "class" and "property" 
 * elements.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface Version
{
    /**
     * Strategy for versioning of objects of this class.
     * @return the strategy for versioning objects of this class
     */
    VersionStrategy strategy() default VersionStrategy.UNSPECIFIED;

    /**
     * Custom strategy for versioning of objects of this class.
     * If customStrategy is non-empty, strategy must be UNSPECIFIED.
     * @return the custom strategy for versioning objects of this class
     */
    String customStrategy() default "";

    /**
     * Name of the column for the version.
     * @return the name of the column for the version
     */
    String column() default "";

    /**
     * Whether the version column(s) is(are) indexed.
     * @return whether the version column(s) is(are) indexed
     */
    String indexed() default "";

    /**
     * The column(s) making up the version.
     * @return the column(s) making up the version
     */
    Column[] columns() default {};

    /** Vendor extensions. 
     * @return the vendor extensions
     */
    Extension[] extensions() default {};

}
