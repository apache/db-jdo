package org.apache.jdo.tck.util;

import java.util.*;

public class DefaultListableInstanceFactory {

  private final HashMap<String, Object> rootMap = new HashMap<>();
  private final List<Object> rootList = new ArrayList<>();

  public synchronized Object getBean(String name) {
    if ("root".equals(name)) {
      return getRootList();
    }
    return rootMap.get(name);
  }

  public synchronized <T> T getBean(String name, Class<T> clazz) {
//    if (!rootMap.containsKey(name)) {
//      System.err.println("CLIF: Not found: " + name + " ---------------------------- ");
//      System.err.println("CLIF:   Beans: " + rootMap.size() + " " + Arrays.toString(rootMap.keySet().toArray()) + " ---------------------------- ");
//    }
//    if (rootMap.get(name) == null) {
//      System.err.println("CLIF: NULL: " + name + " ---------------------------- ");
//      System.err.println("CLIF:   Beans: " + rootMap.size() + " " + Arrays.toString(rootMap.keySet().toArray()) + " ---------------------------- ");
//    }
    return clazz.cast(getBean(name));
  }

  public synchronized void register(String name, Object obj) {
//    System.out.println("CLIF: Registering: " + name + " " + rootList.size() + " -----------------------"); // TODO TZ remove
    rootMap.put(name, obj);
    rootList.add(obj);
  }

  public synchronized List<Object> getRootList() {
//    System.out.println("CLIF: Getting root list: " + rootList.size() + " -----------------------"); // TODO TZ remove
    return Collections.unmodifiableList(rootList);
  }

  public synchronized void reset() {
    rootMap.clear();
    rootList.clear();
  }
}
