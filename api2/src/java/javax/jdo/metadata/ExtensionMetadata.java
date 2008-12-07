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
 * A vendor extension defined in Metadata.
 *
 * @version 2.3
 */
public interface ExtensionMetadata {
    /**
     * Accessor for the vendor name (set at construction).
     * @return The vendor
     */
    String getVendorName();

    /**
     * Accessor for the key (set at construction).
     * @return The key
     */
    String getKey();

    /**
     * Accessor for the value (set at construction).
     * @return The value
     */
    String getValue();
}