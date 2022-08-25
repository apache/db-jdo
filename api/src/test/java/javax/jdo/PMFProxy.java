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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/** */
public class PMFProxy implements InvocationHandler {

  private String connectionDriverName;

  public static PersistenceManagerFactory newInstance() {
    PersistenceManagerFactory pmf =
        (PersistenceManagerFactory)
            Proxy.newProxyInstance(
                PMFProxy.class.getClassLoader(),
                new Class[] {PersistenceManagerFactory.class},
                new PMFProxy());
    return pmf;
  }

  /*
   *
   */
  public Object invoke(Object proxy, Method m, Object[] args) throws Exception {
    Object result = null;

    if (m.getName().equals("getConnectionDriverName")) {
      result = connectionDriverName;
    } else if (m.getName().equals("setConnectionDriverName")) {
      connectionDriverName = (String) args[0];
    } else {
      throw new Exception("Unexpected invocation of method: " + m);
    }
    return result;
  }
}
