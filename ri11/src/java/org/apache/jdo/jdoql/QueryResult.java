/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

/*
 * QueryResult.java
 *
 * Created on March 18, 2001, 12:33 PM
 */

package org.apache.jdo.jdoql;

/** An instance of this interface is returned as the result of
 * Query.execute(...).
 * @author Craig Russell
 * @version 0.9
 */
public interface QueryResult extends java.util.Collection {
    
    /** Close this query result and invalidate all iterators
     * that have been opened on it.  After this method completes,
     * no more iterators can be opened; and
     * all iterators currently open on it will return false to 
     * hasNext(), and will throw NoSuchElementException to next().
     *
     */
    void close();

}

