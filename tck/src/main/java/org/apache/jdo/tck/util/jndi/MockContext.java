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

package org.apache.jdo.tck.util.jndi;

import java.util.Hashtable;
import java.util.Map;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

public class MockContext implements Context {
  private final Map<String, Object> map;
  private boolean isActive = true;

  public MockContext(Map<String, Object> environment) {
    map = environment;
  }

  @Override
  public Object lookup(Name name) throws NamingException {
    throw new UnsupportedOperationException("lookup(name)");
  }

  @Override
  public Object lookup(String name) throws NamingException {
    assertActive();
    if (!map.containsKey(name)) {
      throw new NamingException("lookup(\"" + name + "\")");
    }
    return map.get(name);
  }

  @Override
  public void bind(Name name, Object obj) throws NamingException {
    throw new UnsupportedOperationException("bind(name, obj)");
  }

  @Override
  public void bind(String name, Object obj) throws NamingException {
    if (map.containsKey(name)) {
      throw new NamingException("bind(\"" + name + "\")");
    }
    map.put(name, obj);
  }

  @Override
  public void rebind(Name name, Object obj) throws NamingException {
    throw new UnsupportedOperationException("rebind(name, obj)");
  }

  @Override
  public void rebind(String name, Object obj) throws NamingException {
    if (!map.containsKey(name)) {
      throw new IllegalStateException("rebind(\"" + name + "\")");
    }
    map.put(name, obj);
  }

  @Override
  public void unbind(Name name) throws NamingException {
    throw new UnsupportedOperationException("unbind(name)");
  }

  @Override
  public void unbind(String name) throws NamingException {
    map.remove(name);
  }

  @Override
  public void rename(Name oldName, Name newName) throws NamingException {
    throw new UnsupportedOperationException("rename(oldName, newName)");
  }

  @Override
  public void rename(String oldName, String newName) throws NamingException {
    throw new UnsupportedOperationException("rename(oldName, newName)");
  }

  @Override
  public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
    throw new UnsupportedOperationException("list(name)");
  }

  @Override
  public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
    throw new UnsupportedOperationException("list(name)");
  }

  @Override
  public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
    throw new UnsupportedOperationException("listBindings(name)");
  }

  @Override
  public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
    throw new UnsupportedOperationException("listBindings(name)");
  }

  @Override
  public void destroySubcontext(Name name) throws NamingException {
    throw new UnsupportedOperationException("destroySubcontext(name)");
  }

  @Override
  public void destroySubcontext(String name) throws NamingException {
    throw new UnsupportedOperationException("destroySubcontext(name)");
  }

  @Override
  public Context createSubcontext(Name name) throws NamingException {
    throw new UnsupportedOperationException("createSubcontext(name)");
  }

  @Override
  public Context createSubcontext(String name) throws NamingException {
    throw new UnsupportedOperationException("createSubcontext(name)");
  }

  @Override
  public Object lookupLink(Name name) throws NamingException {
    throw new UnsupportedOperationException("lookupLink(name)");
  }

  @Override
  public Object lookupLink(String name) throws NamingException {
    throw new UnsupportedOperationException("lookupLink(name)");
  }

  @Override
  public NameParser getNameParser(Name name) throws NamingException {
    throw new UnsupportedOperationException("getNameParser(name)");
  }

  @Override
  public NameParser getNameParser(String name) throws NamingException {
    throw new UnsupportedOperationException("getNameParser(name)");
  }

  @Override
  public Name composeName(Name name, Name prefix) throws NamingException {
    throw new UnsupportedOperationException("composeName(name, prefix)");
  }

  @Override
  public String composeName(String name, String prefix) throws NamingException {
    throw new UnsupportedOperationException("composeName(name, prefix)");
  }

  @Override
  public Object addToEnvironment(String propName, Object propVal) throws NamingException {
    throw new UnsupportedOperationException("addToEnvironment(propName, propVal):");
  }

  @Override
  public Object removeFromEnvironment(String propName) throws NamingException {
    throw new UnsupportedOperationException("removeFromEnvironment(propName)");
  }

  @Override
  public Hashtable<?, ?> getEnvironment() throws NamingException {
    throw new UnsupportedOperationException("getEnvironment()");
  }

  @Override
  public void close() throws NamingException {
    isActive = false;
  }

  @Override
  public String getNameInNamespace() throws NamingException {
    throw new UnsupportedOperationException("getNameInNamespace()");
  }

  private void assertActive() throws NamingException {
    if (!isActive) {
      throw new NamingException("This context has already been closed.");
    }
  }
}
