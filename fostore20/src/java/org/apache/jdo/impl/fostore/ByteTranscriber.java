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

package org.apache.jdo.impl.fostore;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
* Transcribes byte values.
*
* @author Dave Bristor
*/
class ByteTranscriber extends FOStoreTranscriber {
    private static ByteTranscriber instance = new ByteTranscriber();

    private ByteTranscriber() {}

    static ByteTranscriber getInstance() {
        return instance;
    }
    
    void storeByte(byte value, DataOutput out) throws IOException {
        out.writeByte(value);
    }

    byte fetchByte(DataInput in) throws IOException {
        return in.readByte();
    }

    void skip(DataInput in) throws IOException { 
        in.readByte(); 
    }
}
