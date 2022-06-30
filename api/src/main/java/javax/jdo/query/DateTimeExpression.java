/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package javax.jdo.query;

/**
 * Representation of a date-time type in a query.
 */
public interface DateTimeExpression extends ComparableExpression<java.util.Date>
{
    /**
     * Accessor for the year of this date-time.
     * @return Expression for the year
     */
    NumericExpression<Integer> getYear();

    /**
     * Accessor for the month of this date-time.
     * @return Expression for the month
     */
    NumericExpression<Integer> getMonth();

    /**
     * Accessor for the day (of the month) of this date-time.
     * @return Expression for the day of the month
     */
    NumericExpression<Integer> getDay();

    /**
     * Accessor for the hour of this date-time.
     * @return Expression for the hour
     */
    NumericExpression<Integer> getHour();

    /**
     * Accessor for the minute of this date-time.
     * @return Expression for the minute
     */
    NumericExpression<Integer> getMinute();

    /**
     * Accessor for the second of this date-time.
     * @return Expression for the second
     */
    NumericExpression<Integer> getSecond();
}