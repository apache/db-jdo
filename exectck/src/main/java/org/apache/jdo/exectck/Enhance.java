
package org.apache.jdo.exectck;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;

/**
 * Goal that enhances test classes for testing a JDO implementation.
 *
 * @goal enhance
 *
 * @phase integration-test
 */
public class Enhance extends AbstractMojo {

    private static final String[] PC_PKG_DIRS = {
        "org" + File.separator + "apache" + File.separator + "jdo" + File.separator + "tck" + File.separator + "api" + File.separator,
        "org" + File.separator + "apache" + File.separator + "jdo" + File.separator + "tck" + File.separator + "pc" + File.separator,
        "org" + File.separator + "apache" + File.separator + "jdo" + File.separator + "tck" + File.separator + "models" + File.separator + "inheritance" + File.separator
    };

    /**
     * Location of TCK generated output.
     * @parameter expression="${jdo.tck.doEnhance}"
     *      default-value="true"
     * @required
     */
    private boolean doEnhance;
    /**
     * Root of the TCK source installation.
     * @parameter expression="${project.src.directory}"
     *      default-value="${basedir}/src"
     * @required
     */
    private String srcDirectory;
    /**
     * Location of the logs directory.
     * @parameter expression="${project.log.directory}"
     *      default-value="${project.build.directory}/logs"
     * @required
     */
    private File logsDirectory;
    /**
     * Location of TCK generated output.
     * @parameter expression="${project.build.directory}"
     *      default-value="${basedir}/target"
     * @required
     */
    private String buildDirectory;
    /**
     * Implementation to be tested (jdori or iut).
     * Any value other than "jdori" will test an appropriately configured IUT
     * @parameter expression="${jdo.tck.impl}"
     *      default-value="jdori"
     * @required
     */
    private String impl;
    /**
     * Location of jar files for implementation under test.
     * @parameter expression="${project.lib.iut.directory}"
     *      default-value="${basedir}/../lib/iut"
     * @required
     */
    private String iutLibsDirectory;

    /**
     * List of identity types to be tested.
     * @parameter expression="${jdo.tck.identitytypes}"
     *      default-value="applicationidentity datastoreidentity"
     * @required
     */
    private String identitytypes;
    private Collection<String> idtypes;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (!doEnhance) {
            System.out.println("Skipping Enhance goal!");
            return;
        }

        idtypes = new HashSet<String>();
        PropertyUtils.string2Set(identitytypes, idtypes);

        // Create directory for enhancer logs
        String enhanceLogsDirName = logsDirectory + File.separator + "enhancer";
        File enhancerLogsDir = new File(enhanceLogsDirName);
        if (!(enhancerLogsDir.exists()) && !(enhancerLogsDir.mkdirs())) {
            throw new MojoExecutionException("Failed to create directory "
                    + enhancerLogsDir);
        }

        // Create directory for enhanced classes
        String enhancedDirName = buildDirectory + File.separator + "enhanced"
                + File.separator + impl + File.separator;
        File enhancedDir = new File(enhancedDirName);
        if (!(enhancedDir.exists()) && !(enhancedDir.mkdirs())) {
            throw new MojoExecutionException("Failed to create directory "
                    + enhancedDir);
        }

        String[] metadataExtensions = {"jdo", "jdoquery", "orm", "xml", "properties"};  // we really want "jdo.properties", but this is easier
        String[] srcDirs = {"jdo", "orm", "testdata"};
        File toFile = null;
        File fromFile = null;
        String fromFileName = null;
        String fromDirName = null;
        String pkgName = null;
        int startIdx = -1;
        Iterator<File> fi = null;
        String[] classArray = new String[10];
        String enhancedIdDirName = null;
        ArrayList<String> classes = null;

        // Copy metadata from src to enhanced
        for (String idtype : idtypes) {
            for (String srcDir : srcDirs) {
                fromDirName = srcDirectory + File.separator + srcDir;
                // iterator over list of abs name of metadata files in src
                fi = FileUtils.iterateFiles(
                        new File(fromDirName), metadataExtensions, true);

                while (fi.hasNext()) {
                    try {
                        fromFile = fi.next();
                        fromFileName = fromFile.toString();
                        if ((startIdx = fromFileName.indexOf(idtype + File.separator)) > -1) {
                            // fully specified name of file (idtype + package + filename)
                            pkgName = fromFileName.substring(startIdx);
                            toFile = new File(enhancedDirName + File.separator
                                    + pkgName);
                            FileUtils.copyFile(fromFile, toFile);
                        } else if (srcDir.equals("testdata")) {
                            startIdx = fromFileName.indexOf("org" + File.separator);
                            pkgName = fromFileName.substring(startIdx);
                            toFile = new File(enhancedDirName + File.separator
                                    + idtype + File.separator + pkgName);
                            FileUtils.copyFile(fromFile, toFile);
                        } else {
                            continue;  // idtype not in pathname, do not copy
                        }
                    } catch (IOException ex) {
                        throw new MojoExecutionException("Failed to copy files from "
                                + fromFileName + " to " + toFile.toString()
                                + ": " + ex.getLocalizedMessage());
                    }
                }

                // Copy pc and pa classes from target/classes to enhanced
                String[] extensions = {"class"};
                fromDirName = buildDirectory + File.separator
                        + "classes" + File.separator;
                enhancedIdDirName = enhancedDirName + idtype + File.separator;
                classes = new ArrayList<String>();
                for (String pcPkgName : PC_PKG_DIRS) {
                    // iterator over list of abs name of class files in target/classes
                    fi = FileUtils.iterateFiles(
                            new File(fromDirName + pcPkgName), extensions, true);
                    while (fi.hasNext()) {
                        try {
                            fromFile = fi.next();
                            fromFileName = fromFile.toString();
                            // fully specified name of file (package + filename)
                            int index = fromFileName.indexOf(pcPkgName);
                            if (index == -1) {
                                throw new MojoExecutionException(
                                    "Cannot get index of package path " + pcPkgName + 
                                    " in file name" + fromFileName);
                            }
                            toFile = new File(enhancedIdDirName + fromFileName.substring(index));
                            FileUtils.copyFile(fromFile, toFile);
                            classes.add(toFile.toString());
                        } catch (IOException ex) {
                            throw new MojoExecutionException("Failed to copy files from "
                                    + fromFileName + " to " + toFile.toString()
                                    + ": " + ex.getLocalizedMessage());
                        }
                    }
                }
            }
                // Enhance classes

                URL[] classPathURLs = new URL[2];
                ArrayList<URL> cpList = new ArrayList<URL>();
                ClassLoader loader = null;
                try {
                    cpList.add((new File(enhancedIdDirName)).toURI().toURL());
                    cpList.add((new File(fromDirName)).toURI().toURL());
                    String[] jars = {"jar"};
                    if (impl.equals("iut")) {
                        fi = FileUtils.iterateFiles(
                            new File(iutLibsDirectory), jars, true);
                        while (fi.hasNext()) {
                            cpList.add(fi.next().toURI().toURL());
                        }
                    }
                    loader = new URLClassLoader(cpList.toArray(classPathURLs),
                             getClass().getClassLoader());
//                    Utilities.printClasspath(loader);
                } catch (MalformedURLException ex) {
                    Logger.getLogger(Enhance.class.getName()).log(Level.SEVERE, null, ex);
                }
                JDOEnhancer enhancer = JDOHelper.getEnhancer(loader);
                enhancer.setVerbose(true);
                enhancer.setClassLoader(loader);
                String[] classArr = classes.toArray(classArray);
                enhancer.addClasses(classArr);
                System.out.println("Enhancing classes for identity type " +
                        idtype);
                enhancer.enhance();
        }
    }
}
