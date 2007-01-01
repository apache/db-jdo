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

package org.apache.jdo.impl.enhancer;


/**
 * Thrown to indicate that the class-file enhancer failed to perform an
 * operation due to a serious error.  The enhancer is not guaranteed to
 * be in a consistent state anymore.
 */
public class EnhancerFatalError
    extends Exception
{
    /**
     * An optional nested exception.
     */
    public final Throwable nested;

    /**
     * Constructs an <code>EnhancerFatalError</code> with no detail message.
     */
    public EnhancerFatalError()
    {
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerFatalError</code> with the specified
     * detail message.
     */
    public EnhancerFatalError(String msg)
    {
        super(msg);
        this.nested = null;
    }

    /**
     * Constructs an <code>EnhancerFatalError</code> with an optional
     * nested exception.
     */
    public EnhancerFatalError(Throwable nested)
    {
        super(nested.toString());
        this.nested = nested;
    }

    /**
     * Constructs an <code>EnhancerFatalError</code> with the specified
     * detail message and an optional nested exception.
     */
    public EnhancerFatalError(String msg, Throwable nested)
    {
        super(msg);
        this.nested = nested;
    }
}
