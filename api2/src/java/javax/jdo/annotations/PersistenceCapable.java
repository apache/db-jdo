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

    /** Table to use for persisting this class or interface. 
     */
    String table() default "";

    /** Catalog to use for persisting this class or interface. 
     */
    String catalog() default "";

    /** Schema to use for persisting this class or interface. 
     */
    String schema() default "";

    /** Whether this class or interface manages an extent. 
     */
    String requiresExtent() default "";

    /** Whether objects of this class or interface can only be embedded. 
     */
    String embeddedOnly() default "";

    /** Whether this class or interface is detachable. 
     */
    String detachable() default "";

    /** Type of identity for this class or interface. 
     */
    IdentityType identityType() default IdentityType.UNKNOWN;

    /** Primary key class when using application identity and using own PK. 
     */
    Class objectIdClass() default void.class;

    /** Any vendor extensions. 
     */
    Extension[] extensions() default {};
}
