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

package org.apache.jdo.impl.fostore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;

import javax.jdo.JDOException;

import org.apache.jdo.store.Transcriber;
import org.apache.jdo.store.TranscriberFactory;


/**
* Provides Transcriber instances for FOStore.  Note that only one instance of
* any kind of transcriber is ever created.
*
* @author Dave Bristor
*/
class FOStoreTranscriberFactory implements TranscriberFactory {
    // Singleton
    private static FOStoreTranscriberFactory instance;

    private static HashMap transcribers = new HashMap(10);

    private FOStoreTranscriberFactory() {
        transcribers.put(boolean.class,
            new Transcriber[] { BooleanTranscriber.getInstance()});
        transcribers.put(char.class,
            new Transcriber[] { CharTranscriber.getInstance()});
        transcribers.put(byte.class,
            new Transcriber[] { ByteTranscriber.getInstance()});
        transcribers.put(short.class,
            new Transcriber[] { ShortTranscriber.getInstance()});
        transcribers.put(int.class,
            new Transcriber[] { IntTranscriber.getInstance()});
        transcribers.put(long.class,
            new Transcriber[] { LongTranscriber.getInstance()});
        transcribers.put(float.class,
            new Transcriber[] { FloatTranscriber.getInstance()});
        transcribers.put(double.class,
            new Transcriber[] { DoubleTranscriber.getInstance()});
        transcribers.put(Object.class,
            new Transcriber[] { ObjectTranscriber.getInstance()});
    }
    
    static FOStoreTranscriberFactory getInstance() {
        if (null == instance) {
            instance = new FOStoreTranscriberFactory();
        }
        return instance;
    }

    /**
    * Provides a Transcriber for the given class.  The result is an array, as
    * defined by the interface, but the return here always contains only one
    * Transcriber.
    * @param cls Class for which a Transcriber is needed.
    * @return An array of transcribers; array will contain only one
    * Transcriber.
    */
    public Transcriber[] getTranscriber(Class cls) {
        Transcriber rc[] = null;
            if (cls != null) {
                rc = (Transcriber[])transcribers.get(cls);
                if (null == rc) {	// cls not of a primitive
                    rc = (Transcriber[])transcribers.get(Object.class);
                }
            }
        return rc;
    }
}

    
