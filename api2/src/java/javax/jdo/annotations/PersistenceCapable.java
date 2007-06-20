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
 * Annotation for whether the class is persistence-capable.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface PersistenceCapable
{
    /** Whether we require the ability to have extents of this class. */
    String requiresExtent() default "";

    /** Whether objects of this class can only be embedded. */
    String embeddedOnly() default "";

    /** Whether this class is detachable. */
    String detachable() default "";

    /** Type of identity for this class. */
    IdentityType identityType() default IdentityType.DATASTORE;

    /** Primary key class when using application identity and using own PK. */
    Class objectIdClass() default void.class;

    /** Any vendor extensions. */
    Extension[] extensions() default {};
}
