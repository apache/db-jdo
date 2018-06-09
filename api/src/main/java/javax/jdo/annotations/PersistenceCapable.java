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
 * Annotation for whether the class or interface is persistence-capable.
 * 
 * @version 2.1
 * @since 2.1
 */
@Target(ElementType.TYPE) 
@Retention(RetentionPolicy.RUNTIME)
public @interface PersistenceCapable
{
    /** Member declarations. Annotations for persistent members of this
     * class or interface can be specifed either here or on each member.
     * Annotations for inherited members can only be specified here.
     * @return member declarations
     */
    Persistent[] members() default {};

    /**
     * Table to use for persisting this class or interface. 
     * @return The table
     */
    String table() default "";

    /**
     * Catalog to use for persisting this class or interface. 
     * @return The catalog
     */
    String catalog() default "";

    /**
     * Schema to use for persisting this class or interface. 
     * @return The schema
     */
    String schema() default "";

    /**
     * Whether this class or interface manages an extent. 
     * @return Whether an extent is required
     */
    String requiresExtent() default "";

    /**
     * Whether objects of this class or interface can only be embedded.
     * @return Whether this is embedded only
     */
    String embeddedOnly() default "";

    /**
     * Whether this class or interface is detachable. 
     * @return Whether this is detachable
     */
    String detachable() default "";

    /**
     * Type of identity for this class or interface. 
     * @return The identity type for this class
     */
    IdentityType identityType() default IdentityType.UNSPECIFIED;

    /**
     * Primary key class when using application identity and using own PK. 
     * @return Object-id class (if specified)
     */
    Class objectIdClass() default void.class;

    /**
     * Whether this class is cacheable in a Level2 cache.
     * @return Whether the class is L2 cacheable
     * @since 2.2
     */
    String cacheable() default "true";

    /**
     * Whether objects of this type should, by default, be locked when read.
     * @return Whether we should lock this type of object by default
     * @since 3.0
     */
    String serializeRead() default "false";

    /**
     * Any vendor extensions.
     * @return The extensions defined for this class
     */
    Extension[] extensions() default {};
}
