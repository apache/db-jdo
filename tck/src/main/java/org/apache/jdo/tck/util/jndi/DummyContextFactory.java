package org.apache.jdo.tck.util.jndi;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

public class DummyContextFactory implements InitialContextFactory {

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        // jndiName: C:\work\github\db-jdo\tck\target\classes\pmf.ser
        // e: java.naming.factory.initial -> org.apache.jdo.tck.util.jndi.DummyContextFactory
        StringBuffer sb = new StringBuffer();
        sb.append("GET_INITIAL_CONTEXT ############################################\n");
        for (Map.Entry e : environment.entrySet()) {
            sb.append("e: " + e.getKey() + " -> " + e.getValue() + "\n");
        }
        //if (true) throw new UnsupportedOperationException("GIC: " + sb.toString());
        return new DummyContext((Hashtable<String, Object>) environment);
    }

    private Properties loadProperties(String fileName) {
        if (fileName == null) {
            fileName = System.getProperty("user.home") + "/.jdo/PMFProperties.properties";
        }
        Properties props = new Properties();
        InputStream propStream = null;
        try {
            propStream = new FileInputStream(fileName);
        } catch (IOException ex) {
            System.out.println("Could not open properties file \"" + fileName + "\"");
            System.out.println(
                    "Please specify a system property PMFProperties "
                            + "with the PMF properties file name as value "
                            + "(defaults to {user.home}/.jdo/PMFProperties.properties)");
            System.exit(1);
        }
        try {
            props.load(propStream);
        } catch (IOException ex) {
            System.out.println("Error loading properties file \"" + fileName + "\"");
            ex.printStackTrace();
            System.exit(1);
        }
        return props;
    }

}
