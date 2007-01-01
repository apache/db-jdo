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

package org.apache.jdo.impl.enhancer.meta.prop;


/**
 * Some utility methods for classname conversion.
 */
final class NameHelper
{
    /**
     *  Converts a classname given in a given VM-similar notation(with slashes)
     *  into a canonical notation (with dots).
     *
     *  @param  classname The VM-similar notation of the classname.
     *  @return  The canonical classname.
     *  @see  #fromCanonicalClassName
     */
    static String toCanonicalClassName(String classname)
    {
        return classname.replace('/', '.');
    }

    /**
     *  Converts a classname given in a canonical form(with dots) into
     *  a VM-similar notation (with slashes)
     *
     *  @param  classname  The canonical classname.
     *  @return  The VM-similar classname notation.
     *  @see  #toCanonicalClassName
     */
    static String fromCanonicalClassName(String classname)
    {
        return classname.replace('.', '/');
    }
}
