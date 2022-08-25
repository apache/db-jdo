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

package org.apache.jdo.tck.util;

import java.util.Collection;
import java.util.Iterator;
import javax.jdo.PersistenceManagerFactory;

public class GetSupportedOptions {

  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      System.err.println("Must pass name of PMF as an argument");
      System.exit(-1);
    }
    String PMFclassname = args[0];
    Class PMFclass = Class.forName(PMFclassname);
    PersistenceManagerFactory pmf = (PersistenceManagerFactory) PMFclass.newInstance();
    Collection options = pmf.supportedOptions();
    System.out.println("Supported options are:");
    Iterator iter = options.iterator();
    while (iter.hasNext()) {
      String val = (String) iter.next();
      System.out.println(val);
    }
  }
}
