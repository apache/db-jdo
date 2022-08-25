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

import java.util.Map;

/*
 * TestEmbeddedMap.java
 *
 * Created on July 18, 2007, 10:16 AM
 *
 */
public abstract class TestEmbeddedMap {
    
    /** Creates a new instance of TestEmbeddedMap */
    public TestEmbeddedMap() {
    }

    @Persistent(table="INTEGER_LINES", embeddedKey="true", embeddedValue="true")
    @Join(column="OWNER_FK")
    @Key(column="INTEGER")
    @Value(
        embeddedMapping=@Embedded(
            members={
                @Persistent(name="point1.x", column="POINT1_X"),
                @Persistent(name="point1.y", column="POINT2_Y"),
                @Persistent(name="point2.x", column="POINT2_X"),
                @Persistent(name="point2.y", column="POINT2_Y")
    }))
    Map<Integer, Line> integerLines;

    @Persistent(table="INTEGER_LINES", embeddedKey="true", embeddedValue="true")
    @Join(column="OWNER_FK")
    @Key(column="INTEGER")
    @Value(
        embeddedMapping=@Embedded(
            members={
                @Persistent(name="point1.x", column="POINT1_X"),
                @Persistent(name="point1.y", column="POINT2_Y"),
                @Persistent(name="point2.x", column="POINT2_X"),
                @Persistent(name="point2.y", column="POINT2_Y")
    }))
    abstract Map<Integer, Line> getIntegerLines();

}
