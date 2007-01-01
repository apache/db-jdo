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
 * Thrown to indicate that an access to JDO meta-data failed; the
 * meta-data component is assured to remain in consistent state.
 */
public class EnhancerMetaDataUserException
    //^olsen: provisional, convert to a checked exception
    extends RuntimeException
{
    /**
     * An optional nested exception.
     */
    public final Throwable nested;

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with no detail
     * message.
     */
    public EnhancerMetaDataUserException()
    {
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with the specified
     * detail message.
     */
    public EnhancerMetaDataUserException(String msg)
    {
        super(msg);
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with an optional
     * nested exception.
     */
    public EnhancerMetaDataUserException(Throwable nested)
    {
        super("nested exception: " + nested);
        this.nested = nested;
    }

    /**
     * Constructs an <code>EnhancerMetaDataUserException</code> with the specified
     * detail message and an optional nested exception.
     */
    public EnhancerMetaDataUserException(String msg, Throwable nested)
    {
        super(msg);
        this.nested = nested;
    }
}
