package javax.jdo;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * A class loader used to ensure that classpath URLs added in JUnit tests
 * aren't included in subsequent JUnit tests.
 */
public class JDOConfigTestClassLoader extends URLClassLoader {

    public JDOConfigTestClassLoader(String partialPathToIgnore, URLClassLoader unparent) {
        this(new String[]{partialPathToIgnore}, unparent);
    }

    public JDOConfigTestClassLoader(String[] partialPathsToIgnore, URLClassLoader unparent) {
        super(new URL[]{}, null);
        addNonTestURLs(partialPathsToIgnore == null ? new String[]{} : partialPathsToIgnore, unparent);
    }

    // HACK:  need to identify a better way of controlling test classpath
    protected void addNonTestURLs(String[] partialPathsToIgnore, URLClassLoader unparent) {
        URL[] urls = unparent.getURLs();
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            String urlString = url.toString();
            for (int j = 0; j < partialPathsToIgnore.length; j++) {
                if (urlString.indexOf(partialPathsToIgnore[j]) == -1) {
                    addURL(url);
                }
            }
        }
    }
}
