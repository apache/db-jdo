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
package javax.jdo.query;

/**
 * Representation of a character expression.
 */
public interface CharacterExpression extends ComparableExpression<Character>
{
    /**
     * Method to return a CharacterExpression representing this CharacterExpression in lower case.
     * @return The lower case expression
     */
    CharacterExpression toLowerCase();

    /**
     * Method to return a CharacterExpression representing this CharacterExpression in upper case.
     * @return The upper case expression
     */
    CharacterExpression toUpperCase();

    /**
     * Method to return an expression that is the current expression negated.
     * @return The negated expression
     */
    CharacterExpression neg();

    /**
     * Method to return an expression that is the complement of the current expression.
     * @return The complement expression
     */
    CharacterExpression com();
}