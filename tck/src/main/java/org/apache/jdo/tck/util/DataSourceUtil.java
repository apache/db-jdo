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

import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface DataSourceUtil {

  static Date date(int y, int m, int d) {
    return new GregorianCalendar(y, m - 1, d, 0, 0, 0).getTime();
  }

  @SafeVarargs
  static <T> List<T> toList(T... objs) {
    return Arrays.stream(objs).collect(Collectors.toList());
  }

  static Map<String, String> toMap(String... objs) {
    Map<String, String> map = new HashMap<>();
    for (int i = 0; i < objs.length; i += 2) {
      map.put(objs[i], objs[i + 1]);
    }
    return map;
  }

  static <K, V> Map<K, V> toMap(Function<V, K> keyFn, V... objs) {
    Map<K, V> map = new HashMap<>();
    for (int i = 0; i < objs.length; i++) {
      map.put(keyFn.apply(objs[i]), objs[i]);
    }
    return map;
  }

  @SafeVarargs
  static <T> Set<T> toSet(T... objs) {
    return Arrays.stream(objs).collect(Collectors.toSet());
  }
}
