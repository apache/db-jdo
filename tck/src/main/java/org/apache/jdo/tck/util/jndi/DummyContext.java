package org.apache.jdo.tck.util.jndi;

import javax.naming.*;
import java.util.Hashtable;

public class DummyContext implements Context {
    private Hashtable<String, Object> map;

    public DummyContext(Hashtable<String, Object> environment) {
        map = environment;
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        throw new UnsupportedOperationException("lookup(name):" + name.toString());
    }

    @Override
    public Object lookup(String name) throws NamingException {
        // lookup: java:comp/BeanManager
        if (!map.containsKey(name)) {
            throw new NamingException("lookup: " + name);
        }
        return map.get(name);
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        throw new UnsupportedOperationException("bind(name):" + name.toString());
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        if (map.containsKey(name)) {
            throw new IllegalStateException("bind: " + name);
        }
        map.put(name, obj);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        throw new UnsupportedOperationException("rebind(name):" + name.toString());

    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        if (!map.containsKey(name)) {
            throw new IllegalStateException("rebind: " + name);
        }
        map.put(name, obj);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public void unbind(String name) throws NamingException {
        // unbind: C:\work\github\db-jdo\tck\target\classes\pmf.ser
        map.remove(name);
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + oldName.toString());

    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + oldName.toString());

    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());

    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());

    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + name.toString());
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + propName.toString());
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        throw new UnsupportedOperationException("unbind(name):" + propName.toString());
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        throw new UnsupportedOperationException("getEnvironment()");
    }

    @Override
    public void close() throws NamingException {

    }

    @Override
    public String getNameInNamespace() throws NamingException {
        throw new UnsupportedOperationException("getNameInNamespace()");
    }
}
