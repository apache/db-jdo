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
 * Represents an element in a collection/array.
 * @since 2.3
 */
public interface ColumnMetadata extends Metadata {
    /**
     * Method to set the column name.
     * 
     * @param name Column name
     */
    void setName(String name);

    /**
     * Accessor for the name of the column.
     * 
     * @return The name
     */
    String getName();

    /**
     * Method to set the target column (at the other side of the relation).
     * 
     * @param target Target column
     */
    void setTarget(String target);

    /**
     * Accessor for the name of the target column.
     * 
     * @return Target column name
     */
    String getTarget();

    /**
     * Method to set the target field (at the other side of the relation).
     * 
     * @param target Target field
     */
    void setTargetField(String target);

    /**
     * Accessor for the name of the target field.
     * 
     * @return Target field name
     */
    String getTargetField();

    /**
     * Method to set the JDBC type.
     * 
     * @param type JDBC Type
     */
    void setJDBCType(String type);

    /**
     * Accessor for the JDBC Type
     * 
     * @return JDBC Type
     */
    String getJDBCType();

    /**
     * Method to set the SQL type.
     * 
     * @param type SQL Type
     */
    void setSQLType(String type);

    /**
     * Accessor for the SQL Type
     * 
     * @return SQL Type
     */
    String getSQLType();

    /**
     * Method to set the length
     * 
     * @param len Length
     */
    void setLength(int len);

    /**
     * Accessor for the length
     * 
     * @return length
     */
    Integer getLength();

    /**
     * Method to set the scale
     * 
     * @param scale scale
     */
    void setScale(int scale);

    /**
     * Accessor for the scale
     * 
     * @return scale
     */
    Integer getScale();

    /**
     * Method to set whether it allows null.
     * 
     * @param nulls Allows null?
     */
    void setAllowsNull(boolean nulls);

    /**
     * Accessor for whether the column allows null.
     * 
     * @return Allows null?
     */
    Boolean getAllowsNull();

    /**
     * Method to set the default value.
     * 
     * @param val Default value
     */
    void setDefaultValue(String val);

    /**
     * Accessor for the default value
     * 
     * @return Default value
     */
    String getDefaultValue();

    /**
     * Method to set the insert value (for columns with no field/property).
     * 
     * @param val Insert value
     */
    void setInsertValue(String val);

    /**
     * Accessor for the insert value (for columns with no field/property)
     * 
     * @return Insert value
     */
    String getInsertValue();
}