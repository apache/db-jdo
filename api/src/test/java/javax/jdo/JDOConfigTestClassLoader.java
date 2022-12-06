/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import java.net.URL;
import java.net.URLClassLoader;

/** A class loader that allows the user to add classpath entries. */
public class JDOConfigTestClassLoader extends URLClassLoader {

  /** Uses the CTCCL as the parent and adds the given path to this loader's classpath. */
  public JDOConfigTestClassLoader(String... additionalPath) throws IOException {
    this(Thread.currentThread().getContextClassLoader(), additionalPath);
  }

  /** Uses the given ClassLoader as the parent & adds the given paths to this loader's classpath. */
  public JDOConfigTestClassLoader(ClassLoader parent, String... additionalPaths)
      throws IOException {
    super(new URL[] {}, parent);

    for (String path : additionalPaths) {
      addFile(path);
    }
  }

  public void addFile(String s) throws IOException {
    addFile(new File(s));
  }

  public void addFile(File f) throws IOException {
    addURL(f.toURI().toURL());
  }

  @Override
  public void addURL(URL url) {
    super.addURL(url);
  }
}
