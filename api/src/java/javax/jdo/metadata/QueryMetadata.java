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
package javax.jdo.metadata;

/**
 * Represents a named query.
 * @since 3.0
 */
public interface QueryMetadata extends Metadata {
    /**
     * Accessor for the name of the query (set on construction).
     * 
     * @return The name
     */
    String getName();

    /**
     * Method to set the language of the query.
     * 
     * @param lang Query language
     */
    QueryMetadata setLanguage(String lang);

    /**
     * Accessor for the query language.
     * 
     * @return The language
     */
    String getLanguage();

    /**
     * Method to set the single-string query.
     * 
     * @param query The query
     */
    QueryMetadata setQuery(String query);

    /**
     * Accessor for the single-string query.
     * 
     * @return The query
     */
    String getQuery();

    /**
     * Method to set the result class name for the query
     * 
     * @param clsName Result class name
     */
    QueryMetadata setResultClass(String clsName);

    /**
     * Accessor for the result class name for the query.
     * 
     * @return The result class name
     */
    String getResultClass();

    /**
     * Method to set if the query results are unique
     * 
     * @param unique Whether they are unique
     */
    QueryMetadata setUnique(boolean unique);

    /**
     * Accessor for whether results from the query are unique
     * 
     * @return Results are unique?
     */
    Boolean getUnique();

    /**
     * Method to set the query as not being modifiable from now.
     */
    QueryMetadata setUnmodifiable();

    /**
     * Accessor for whether the query is unmodifiable.
     * 
     * @return Can't be changed?
     */
    boolean getUnmodifiable();

    /**
     * Method to set the FetchPlan to use for this named query.
     * 
     * @param fetchPlanName name of the FetchPlan
     */
    QueryMetadata setFetchPlan(String fetchPlanName);

    /**
     * Accessor for the name of a fetch plan to use (if any).
     * 
     * @return The fetch plan name
     */
    String getFetchPlan();
}
