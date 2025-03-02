package org.apache.jdo.tck.pc.order;

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

  protected synchronized void register(String name, Object obj) {
    rootMap.put(name, obj);
    rootList.add(obj);
  }

  public synchronized List<Object> getRootList() {
    return Collections.unmodifiableList(rootList);
  }
}
