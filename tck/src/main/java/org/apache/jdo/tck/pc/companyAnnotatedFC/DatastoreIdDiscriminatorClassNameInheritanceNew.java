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

package org.apache.jdo.tck.pc.companyAnnotatedFC;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@javax.jdo.annotations.PersistenceCapable(
    detachable = "true",
    identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
    strategy = IdGeneratorStrategy.IDENTITY,
    column = "DATASTORE_IDENTITY")
@javax.jdo.annotations.Discriminator(
    strategy = DiscriminatorStrategy.CLASS_NAME,
    column = "DISCRIMINATOR",
    indexed = "true")
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public @interface DatastoreIdDiscriminatorClassNameInheritanceNew {}
