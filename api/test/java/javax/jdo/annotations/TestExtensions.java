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

/*
 * TestExtensions verifies that annotations can include vendor extensions.
 * This class is not used except to verify that it compiles.
 * https://issues.apache.org/jira/browse/JDO-728
 * Add extensions element to @Index, @ForeignKey, @Unique, @PrimaryKey annotations.
 */
public abstract class TestExtensions {
    
    /** Creates a new instance of TestExtensions */
    public TestExtensions() {
    }

    @PrimaryKey(extensions= {
            @Extension(vendorName = "vendor 1", key = "key 1", value = "value 1"),
            @Extension(vendorName = "vendor 2", key = "key 2", value = "value 2")}
    )
    long pk;

    @Index(extensions= {
            @Extension(vendorName = "vendor 1", key = "key 1", value = "value 1"),
            @Extension(vendorName = "vendor 2", key = "key 2", value = "value 2")}
    )
    @Unique(extensions= {
            @Extension(vendorName = "vendor 1", key = "key 1", value = "value 1"),
            @Extension(vendorName = "vendor 2", key = "key 2", value = "value 2")}
    )
    @ForeignKey(extensions= {
            @Extension(vendorName = "vendor 1", key = "key 1", value = "value 1"),
            @Extension(vendorName = "vendor 2", key = "key 2", value = "value 2")}
    )
    long fk;
}
