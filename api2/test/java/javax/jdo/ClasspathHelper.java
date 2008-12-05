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

package javax.jdo;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClasspathHelper {

    private static URLClassLoader SYSTEM_CLASSLOADER = (URLClassLoader) ClassLoader.getSystemClassLoader();
    private static Method METHOD;
    static {
        try {
            METHOD = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
            METHOD.setAccessible(true);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static void addFile(String s) throws IOException {
        addFile(s, null);
    }

    public static void addFile(File f) throws IOException {
        addFile(f, null);
    }

    public static void addURL(URL u) throws IOException {
        addURL(u, null);
    }

    public static void addFile(String s, URLClassLoader loader) throws IOException {
        addFile(new File(s), loader);
    }

    public static void addFile(File f, URLClassLoader loader) throws IOException {
        addURL(f.toURL(), loader);
    }

    public static void addURL(URL u, URLClassLoader loader) throws IOException {
        if (loader == null) {
            loader = SYSTEM_CLASSLOADER;
        }
        try {
            METHOD.invoke(loader, new Object[] { u });
        }
        catch (Throwable t) {
            throw new IOException("Could not add URL to system classloader: " + t.getMessage());
        }
    }
}


