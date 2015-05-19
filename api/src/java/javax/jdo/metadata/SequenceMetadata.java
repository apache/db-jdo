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

import javax.jdo.annotations.SequenceStrategy;

/**
 * Represents a sequence.
 * @since 3.0
 */
public interface SequenceMetadata extends Metadata {
    /**
     * Accessor for the name of the sequence (set on construction).
     * @return The name
     */
    String getName();

    /**
     * Accessor for the sequence strategy (set on construction).
     * @return Sequence strategy
     */
    SequenceStrategy getSequenceStrategy();

    /**
     * Method to set the name of the datastore sequence that this maps to.
     * @param seq Datastore sequence name
     * @return This metadata object
     */
    SequenceMetadata setDatastoreSequence(String seq);

    /**
     * Accessor for the name of the datastore sequence that this maps to
     * @return The datastore sequence name
     */
    String getDatastoreSequence();

    /**
     * Method to set the result class name for the query.
     * @param clsName Result class name
     * @return This metadata object
     */
    SequenceMetadata setFactoryClass(String clsName);

    /**
     * Accessor for the factory class for this sequence.
     * @return The factory class
     */
    String getFactoryClass();

    /**
     * Method to set the initial value for the sequence.
     * @param val Initial value to use
     * @return This metadata object
     * @since 3.1
     */
    SequenceMetadata setInitialValue(int val);

    /**
     * Accessor for the initial value of the sequence (if any).
     * @return initial value
     * @since 3.1
     */
    Integer getInitialValue();

    /**
     * Method to set the allocation size for the sequence.
     * @param size Allocation size to use
     * @return This metadata object
     * @since 3.1
     */
    SequenceMetadata setAllocationSize(int size);

    /**
     * Accessor for the allocation size of the sequence (if any).
     * @return allocation size
     * @since 3.1
     */
    Integer getAllocationSize();
}
