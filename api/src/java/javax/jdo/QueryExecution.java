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
package javax.jdo;

import java.util.List;

/**
 * Interface defining the methods for executing a query.
 * TODO Do parameters get passed in via these methods ? in which case we have to cater for named params (Map) and numbered params (array? or Map?)
 * @param T Candidate class
 */
public interface QueryExecution<T> {
    /**
     * Method to execute the query where there are (potentially) multiple rows and we are returning the candidate type.
     * @return The List of candidate objects
     */
    List<T> executeList();

    /**
     * Method to execute the query where there is a single row and we are returning the candidate type.
     * @return The candidate object returned by the query (or null)
     */
    T executeUnique();

    /**
     * Method to execute the query where there are (potentially) multiple rows and we are returning a result type for the specified result.
     * @param resultCls The result class
     * @return List of result objects
     * @param <R> The result type
     */
    <R> List<R> executeResultList(Class<R> resultCls);

    /**
     * Method to execute the query where there is a single row and we are returning a result type for the specified result.
     * @param resultCls The result class
     * @return The result object (or null)
     * @param <R> The result type
     */
    <R> R executeResultUnique(Class<R> resultCls);

    /**
     * Method to execute the query where there are (potentially) multiple rows and we have a result defined but no result class.
     * @return The list of query results
     */
    List<Object> executeResultList();

    /**
     * Method to execute the query where there is a single row and we have a result defined but no result class.
     * @return The query result (or null)
     */
    Object executeResultUnique();
}
