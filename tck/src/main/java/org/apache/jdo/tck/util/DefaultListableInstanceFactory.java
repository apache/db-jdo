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

package org.apache.jdo.tck.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DefaultListableInstanceFactory {

  private final HashMap<String, Object> rootMap = new HashMap<>();
  private final List<Object> rootList = new ArrayList<>();

  public final synchronized Object getBean(String name) {
    if ("root".equals(name)) {
      return getRootList();
    }
    return rootMap.get(name);
  }

  public final synchronized <T> T getBean(String name, Class<T> clazz) {
    return clazz.cast(getBean(name));
  }

  public synchronized void register(String name, Object obj) {
    rootMap.put(name, obj);
    rootList.add(obj);
  }

  /**
   * Returns a list of root objects. The method expects to find a bean called "root" of type list in
   * the xml and returns it.
   *
   * @return a list of root instances
   */
  public final synchronized List<Object> getRootList() {
    return Collections.unmodifiableList(rootList);
  }

  @SuppressWarnings("unchecked")
  public static <F> DataSource<F> getDataSource(String resourceName) {
    try {
      Class<DataSource<F>> cls = (Class<DataSource<F>>) Class.forName(resourceName);
      Constructor<DataSource<F>> cstr = cls.getConstructor();
      return cstr.newInstance();
    } catch (ClassNotFoundException
        | InvocationTargetException
        | NoSuchMethodException
        | InstantiationException
        | IllegalAccessException
        | NullPointerException e) {
      throw new IllegalArgumentException("Error executing test data class: " + resourceName, e);
    }
  }
}
