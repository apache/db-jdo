package org.apache.jdo.tck.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DefaultListableInstanceFactory {

  private final HashMap<String, Object> rootMap = new HashMap<>();
  private final List<Object> rootList = new ArrayList<>();

  public synchronized Object getBean(String name) {
    if ("root".equals(name)) {
      return getRootList();
    }
    return rootMap.get(name);
  }

  public <T> T getBean(String name, Class<T> clazz) {
    return clazz.cast(getBean(name));
  }

  public synchronized void register(String name, Object obj) {
    rootMap.put(name, obj);
    rootList.add(obj);
  }

  public synchronized List<Object> getRootList() {
    System.out.println("Getting root list: " + rootList.size()); // TODO TZ remove
    return Collections.unmodifiableList(rootList);
  }
}
