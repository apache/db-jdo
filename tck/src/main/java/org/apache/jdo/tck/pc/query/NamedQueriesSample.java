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
package org.apache.jdo.tck.pc.query;

import java.io.Serializable;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Query;

/**
 * Test for use of annotations, where we are relying on the Repeatable nature of the @Query
 * annotation (java 8).
 */
@PersistenceCapable
@Query(
    name = "NameIsJohn",
    value = "SELECT FROM org.apache.jdo.tck.pc.query.NamedQueriesSample WHERE name == 'John'")
@Query(
    name = "NameIsFred",
    value = "SELECT FROM org.apache.jdo.tck.pc.query.NamedQueriesSample WHERE name == 'Fred'")
public class NamedQueriesSample implements Serializable {

  private static final long serialVersionUID = 1L;

  @PrimaryKey long id;

  String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
