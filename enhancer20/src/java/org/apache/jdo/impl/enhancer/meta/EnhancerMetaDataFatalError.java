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


package org.apache.jdo.impl.enhancer.meta;


/**
 * Thrown to indicate that an access to JDO meta-data failed due to a
 * serious error, which might have left the meta-data component in an
 * inconsistent state.
 */
public class EnhancerMetaDataFatalError
    //^olsen: provisional, convert to a checked exception
    extends RuntimeException
{
    /**
     * An optional nested exception.
     */
    public final Throwable nested;

    /**
     * Constructs an <code>EnhancerMetaDataFatalError</code> with no detail message.
     */
    public EnhancerMetaDataFatalError()
    {
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerMetaDataFatalError</code> with the specified
     * detail message.
     */
    public EnhancerMetaDataFatalError(String msg)
    {
        super(msg);
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerMetaDataFatalError</code> with an optional
     * nested exception.
     */
    public EnhancerMetaDataFatalError(Throwable nested)
    {
        super(nested.toString());
        this.nested = nested;
    }

    /**
     * Constructs an <code>EnhancerMetaDataFatalError</code> with the specified
     * detail message and an optional nested exception.
     */
    public EnhancerMetaDataFatalError(String msg, Throwable nested)
    {
        super(msg);
        this.nested = nested;
    }
}
