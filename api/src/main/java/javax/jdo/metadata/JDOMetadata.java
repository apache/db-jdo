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
package javax.jdo.metadata;

/**
 * Represents the top-level JDO metadata.
 *
 * @since 3.0
 */
public interface JDOMetadata extends Metadata {
  /**
   * Method to set the catalog (ORM) to apply to all classes in this JDO Metadata.
   *
   * @param catalog Catalog name
   * @return This metadata object
   */
  JDOMetadata setCatalog(String catalog);

  /**
   * Accessor for the catalog (ORM) that all classes in this JDO Metadata default to.
   *
   * @return The catalog
   */
  String getCatalog();

  /**
   * Method to set the schema (ORM) to apply to all classes in this JDO Metadata.
   *
   * @param schema Schema name
   * @return This metadata object
   */
  JDOMetadata setSchema(String schema);

  /**
   * Accessor for the schema (ORM) that all classes in this JDO Metadata default to.
   *
   * @return The schema
   */
  String getSchema();

  /**
   * Accessor for all packages defined on the JDO Metadata.
   *
   * @return The packages
   */
  PackageMetadata[] getPackages();

  /**
   * Add a new package to this JDO Metadata.
   *
   * @param pkgName Name of the package
   * @return The PackageMetadata
   */
  PackageMetadata newPackageMetadata(String pkgName);

  /**
   * Add a new package to this JDO Metadata.
   *
   * @param pkg The package
   * @return The PackageMetadata
   */
  PackageMetadata newPackageMetadata(Package pkg);

  /**
   * Accessor for the number of packages defined in this JDO Metadata.
   *
   * @return The number of packages.
   */
  int getNumberOfPackages();

  /**
   * Add a new class to this JDO Metadata. Adds its package also if not yet existing.
   *
   * @param cls Class to add
   * @return The ClassMetadata
   */
  ClassMetadata newClassMetadata(Class<?> cls);

  /**
   * Add a new interface to this JDO Metadata. Adds its package also if not yet existing.
   *
   * @param cls Class to add
   * @return The InterfaceMetadata
   */
  InterfaceMetadata newInterfaceMetadata(Class<?> cls);

  /**
   * Accessor for any named queries defined on the JDO Metadata.
   *
   * @return The queries
   */
  QueryMetadata[] getQueries();

  /**
   * Add a new named query to this JDO Metadata.
   *
   * @param name Name of the query
   * @return The QueryMetadata
   */
  QueryMetadata newQueryMetadata(String name);

  /**
   * Accessor for the number of named queries defined in this JDO Metadata.
   *
   * @return The number of queries.
   */
  int getNumberOfQueries();

  /**
   * Accessor for any fetch plans defined on the JDO Metadata.
   *
   * @return The fetch plans
   */
  FetchPlanMetadata[] getFetchPlans();

  /**
   * Add a new fetch plan to this JDO Metadata.
   *
   * @param name Name of the query
   * @return The FetchPlanMetadata
   */
  FetchPlanMetadata newFetchPlanMetadata(String name);

  /**
   * Accessor for the number of fetch plans defined in this JDO Metadata.
   *
   * @return The number of fetch plans.
   */
  int getNumberOfFetchPlans();
}
