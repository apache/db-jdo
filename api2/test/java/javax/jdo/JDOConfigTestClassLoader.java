package javax.jdo;

import org.apache.tools.ant.AntClassLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class loader used to ensure that classpath URLs added in JUnit tests
 * aren't included in subsequent JUnit tests.
 */
public class JDOConfigTestClassLoader extends URLClassLoader {

    public JDOConfigTestClassLoader(
            String partialPathToIgnore,
            ClassLoader unparent
    ) {
        this(new String[]{partialPathToIgnore}, unparent);
    }

    public JDOConfigTestClassLoader(
            String[] partialPathsToIgnore,
            ClassLoader unparent
    ) {
        super(new URL[]{}, null);
        
        if (unparent instanceof URLClassLoader) {
            addNonTestURLs(
                    partialPathsToIgnore == null
                            ? new String[]{}
                            : partialPathsToIgnore,
                    (URLClassLoader) unparent);
        }
        else if (unparent instanceof AntClassLoader) {
            addNonTestURLs(
                    partialPathsToIgnore == null
                            ? new String[]{}
                            : partialPathsToIgnore,
                    (AntClassLoader) unparent);
        }
        else {
            throw new RuntimeException(
                    "unknown ClassLoader type: "
                            + unparent.getClass().getName());
        }
    }

    // HACK:  need to identify a better way of controlling test classpath
    protected void addNonTestURLs(
            String[] partialPathsToIgnore,
            URLClassLoader unparent
    ) {
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

    protected void addNonTestURLs(
            String[] partialPathsToIgnore,
            AntClassLoader unparent
    ) {
        List elements = new ArrayList();
        String classpath = unparent.getClasspath();
        StringTokenizer st = new StringTokenizer(
                classpath, System.getProperty("path.separator"));
        while (st.hasMoreTokens()) {
            elements.add("file://" + st.nextToken());
        }
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            String element = (String) i.next();
            for (int j = 0; j < partialPathsToIgnore.length; j++) {
                if (element.indexOf(partialPathsToIgnore[j]) == -1) {
                    try {
                        addURL(new URL(element));
                    }
                    catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
