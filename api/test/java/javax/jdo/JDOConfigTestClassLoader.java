package javax.jdo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * A class loader that allows the user to add classpath entries.
 */
public class JDOConfigTestClassLoader extends URLClassLoader {

    /**
     * Uses the CTCCL as the parent and adds the given path to this loader's classpath.
     */
    public JDOConfigTestClassLoader(String... additionalPath) throws IOException {
	this(Thread.currentThread().getContextClassLoader(), additionalPath);
    }
    
    /**
     * Uses the given ClassLoader as the parent & adds the given paths to this loader's classpath.
     */
    public JDOConfigTestClassLoader(ClassLoader parent, String... additionalPaths) throws IOException {
	super(new URL[] {}, parent);

	for (String path : additionalPaths) {
	    addFile(path);
	}
    }

    public void addFile(String s) throws IOException {
	addFile(new File(s));
    }

    public void addFile(File f) throws IOException {
	addURL(f.toURI().toURL());
    }

    @Override
    public void addURL(URL url) {
	super.addURL(url);
    }
}
