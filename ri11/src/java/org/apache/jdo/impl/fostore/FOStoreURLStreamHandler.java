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

/*
 * FOStoreURLStreamHandler.java
 *
 * Created on June 7, 2001, 7:11 PM
 */

package org.apache.jdo.impl.fostore;

import java.net.URLStreamHandler;
import java.net.URLConnection;
import java.io.IOException;

/**
 * Creates connections to databases.
 * @author  Craig Russell
 * @version 1.0
 */
class FOStoreURLStreamHandler extends URLStreamHandler {
    
    static FOStoreURLStreamHandler sh = new FOStoreURLStreamHandler();
    
    static FOStoreURLStreamHandler getInstance() {
        return sh;
    }

    /** Creates new FOStoreURLStreamHandler */
    public FOStoreURLStreamHandler() {
    }
    
    protected URLConnection openConnection(java.net.URL url) 
            throws IOException {
        if (url.getHost().length() == 0) {
            // local connection
            return new FOStoreLocalConnection(url);
        } else {
            // remote connection
            return new FOStoreRemoteConnection(url);
        }
    }

}
