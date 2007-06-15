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
 * Annotation for whether the interface is a persistent interface
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
public @interface PersistentInterface
{
    /** Whether we require the ability to have extents of this interface. */
    String requiresExtent() default "";

    /** Whether objects of implementations of this interface can only be embedded. */
    String embeddedOnly() default "";

    /** Whether this objects of implementations of this interface are detachable. */
    String detachable() default "";

    /** Type of identity for this persistent interface. */
    IdentityTypeValue identityType() default IdentityTypeValue.DATASTORE;

    /** Primary key class when using application identity and using own PK. */
    Class objectIdClass() default void.class;

    /** Any vendor extensions. */
    Extension[] extensions() default {};
}
