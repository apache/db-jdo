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

import javax.jdo.annotations.InheritanceStrategy;

/**
 * Represents the inheritance of a class.
 * @since 2.3
 */
public interface InheritanceMetadata extends Metadata {
    /**
     * Method to set the inheritance strategy.
     * 
     * @param strategy The strategy
     */
    InheritanceMetadata setStrategy(InheritanceStrategy strategy);

    /**
     * Accessor for the inheritance strategy.
     * 
     * @return The strategy
     */
    InheritanceStrategy getStrategy();

    /**
     * Method to set the custom inheritance strategy.
     * 
     * @param strategy The strategy
     */
    InheritanceMetadata setCustomStrategy(String strategy);

    /**
     * Accessor for the custom inheritance (overriding "strategy").
     * 
     * @return The strategy
     */
    String getCustomStrategy();

    /**
     * Method to define the new discriminator metadata.
     * 
     * @return The DiscriminatorMetadata
     */
    DiscriminatorMetadata newDiscriminatorMetadata();

    /**
     * Accessor for the discriminator (if any).
     * 
     * @return Discriminator
     */
    DiscriminatorMetadata getDiscriminatorMetadata();

    /**
     * Method to define the new join information
     * 
     * @return The JoinMetadata
     */
    JoinMetadata newJoinMetadata();

    /**
     * Accessor for the join (if any).
     * 
     * @return Join information
     */
    JoinMetadata getJoinMetadata();
}