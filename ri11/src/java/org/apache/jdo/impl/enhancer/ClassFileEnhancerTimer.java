/*
 * Copyright 2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
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

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.jdo.impl.enhancer.util.Support;



/**
 * Timer-wrapper for ClassFileEnhancer instances.
 *
 * @author Martin Zaun
 */
public final class ClassFileEnhancerTimer
    extends Support
    implements ClassFileEnhancer
{
    // delegate
    final protected ClassFileEnhancer delegate;

    /**
     * Creates an instance.
     */
    public ClassFileEnhancerTimer(ClassFileEnhancer delegate)
    {
        affirm(delegate);
        this.delegate = delegate;
    }

    public boolean enhanceClassFile(InputStream inClassFile,
                                    OutputStream outClassFile)
        throws EnhancerUserException, EnhancerFatalError
    {
        try {
            timer.push("ClassFileEnhancer.enhanceClassFile(InputStream,OutputStream)");
            return delegate.enhanceClassFile(inClassFile, outClassFile);
        } finally {
            timer.pop();
        }
    }

    public boolean enhanceClassFile(InputStream inClassFile,
                                    OutputStreamWrapper outClassFile)
        throws EnhancerUserException, EnhancerFatalError
    {
        try {
            timer.push("ClassFileEnhancer.enhanceClassFile(InputStream,OutputStreamWrapper)");
            return delegate.enhanceClassFile(inClassFile, outClassFile);
        } finally {
            timer.pop();
        }
    }
}
