package org.apache.jdo.tck.pc.order;

import java.util.HashMap;

public class DefaultListableInstanceFactory {

    private final HashMap<String, Object> rootMap = new HashMap<>(); // TODO concurrent?

    public synchronized Object getBean(String name) {
        if (!rootMap.containsKey(name)) {
            throw new IllegalArgumentException("Not found: " + name);
        }
        if (rootMap.get(name) == null) {
            throw new IllegalArgumentException("Not found2: " + name);
        }
        Object o = rootMap.get(name);
        System.err.println("Found: " +name + "  -->> " + o.getClass());
        // throw new IllegalStateException("Found: " +name + "  -->> " + o.getClass());
        return rootMap.get(name);
    }

    public <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(getBean(name));
    }

    protected synchronized void register(String name, Object obj) {
        rootMap.put(name, obj);
    }
}
