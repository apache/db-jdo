package org.apache.jdo.tck.util.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class DummyContextFactory implements InitialContextFactory {

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        // jndiName: C:\work\github\db-jdo\tck\target\classes\pmf.ser
        // e: java.naming.factory.initial -> org.apache.jdo.tck.util.jndi.DummyContextFactory
        return new DummyContext((Hashtable<String, Object>) environment);
    }
}
