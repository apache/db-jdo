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

/*
 * JDOQueryException.java
 *
 * Created on August 28, 2001
 */

package org.apache.jdo.jdoql;

import javax.jdo.JDOUserException;

/** 
 * This class represents query user errors.
 * 
 * @author  Michael Bouschen
 */
public class JDOQueryException 
    extends JDOUserException
{
    /**
     * Creates a new <code>JDOQueryException</code> without detail message.
     */
    public JDOQueryException() 
    {
    }

    /**
     * Constructs a new <code>JDOQueryException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public JDOQueryException(String msg) 
    {
        super(msg);
    }
    
    /**
      * Constructs a new <code>JDOQueryException</code> with the specified detail message
      * and nested Exception.
      * @param msg the detail message.
      * @param nested the nested <code>Exception</code>.
      */
    public JDOQueryException(String msg, Throwable nested) 
    {
        super(msg, nested);
    }
}
