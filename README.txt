This is a prototype of the JDO maven projects:
- api11 to build the jdo.jar which defines the JDO API version 1.1
- ri11 the current JDO1 RI
- tck11 the current JDO1 TCK
- api20 to build the jdo.jar which defines the JDO API version 2.0
- core20 the JDO2 core including utility and metadata model classes
- enhancer20 the JDO2 byte code enhancer 
- runtime20 the JDO2 runtime classes (pm, pmf, state manager, life cycle, 
  store manager interface, runtime meta data support)
- query20 the JDO2 JDOQL query compiler and JDOQL query tree nodes
- fostore20 the JDO2 file object store (fostore) datastore
- tck20 the JDO 2.0 TCK
- btree the Netbeans open source btree implementation used by ri11 and fostore20

-------------
Dependencies:
-------------

The JDO maven project define their dependencies in the project.xml file:
JDO1: api11, btree, ri11, tck11
JDO2: api20, tck20, btree, core20, enhancer20, runtime20, query20, fostore20
Please note, the JDO2 implementation projects enhancer20, runtime20, query20, 
and fostore20 do not yet implement the JDO2 API as defined in api20 and thus
they still depend on api11.

-------------
Prerequisites
-------------

- Maven
You need Maven version 1.0.1 or 1.0.2. You can download maven from 
http://maven.apache.org/start/download.html

- antlr
The JDORI uses antlr version 2.7.3 to implement the query compiler. We asked to 
add this version to the maven repository, but until this happens you need to 
manually add antlr 2.7.3 to your local maven repository. Please download 
version 2.7.3 from http://www.antlr.org/download/antlr-2.7.3.jar and copy 
it to your local maven repository:
  cp antlr-2.7.3.jar $HOME/.maven/repository/antlr/jars/antlr-2.7.3.jar

- JNDI implementation
- fscontext.jar and providerutil.jar
The JDORI JNDI test case in ri11 and fostore20 needs a JNDI implementation. 
To configure this please update the property jndi in ri11/project.properties 
and fostore20/project.properties to include all jars of your JNDI 
implementation. The properties file jndi.properties under ri11/test
should define all the necessary properties of your JNDI implemenation.
The defaults setting in project.properties and jndi.properties use Sun's File 
System Service Provider implementation (fscontext.jar and providerutil.jar) and 
assume to find both jars in the directory ri11. For donwload please go to 
http://java.sun.com/products/jndi/downloads/index.html, click the Download 
button at 'Download JNDI 1.2.1 & More', accept a license agreement, download 
'File System Service Provider, 1.2 Beta 3' and then unpack the downloaded zip.
 It includes the jars fscontext.jar and providerutil.jar.

- JPOX
The Reference Implementation for JDO 2.0 is JPOX. To run tck20 you must
manually add the JPOX jar file and JPOX enhancer jar file to your local
maven repository. Download both jars from
http://www.jpox.org/docs/download.html and copy them to your local maven
repository, changing the version number to "SNAPSHOT".  You must also
download the jpox plug-ins jpox-c3p0-<version>.jar and jpox-dbcp-<version>.jar
for connection pooling.
  cp jpox-<version>.jar $HOME/.maven/repository/jpox/jars/jpox-SNAPSHOT.jar
  cp jpox-enhancer-<version>.jar $HOME/.maven/repository/jpox/jars/jpox-enhancer-SNAPSHOT.jar
  cp jpox-c3p0-<version>.jar $HOME/.maven/repository/jpox/jars/jpox-c3p0-SNAPSHOT.jar
  cp jpox-dbcp-<version>.jar $HOME/.maven/repository/jpox/jars/jpox-dbcp-SNAPSHOT.jar

Finally, you must download c3p0-0.9.0-pre6.bin.zip, unzip it and copy c3p0-0.9.0-pre6.jar to $HOME/.maven/repository/c3p0/jars.

- derby
To use Derby as the datastore for tck20, download version 10.0.2.1 from
http://incubator.apache.org/derby/derby_downloads.html and add derby.jar
and derbytools.jar your maven repository. Rename them to include the version
number: derby-10.0.2.1.jar and derby-tools-10.0.2.1.jar.  NOTE!! Mac OSX users
must ncomment derby.storage.fileSyncTransactionLog=true in tck20/test/conf/derby.properties.

-------
Remarks
-------

ToDo:
- building ri
- no useapplicationidentity and usedatastoreidentity anymore

(1) Please note, maven uses the user.home system property for the location
of the maven local repostitory: ${user.home}/.maven/repository.
Under Windows this system property is C:\Documents and Settings\<user> 
no matter what the HOME variable is set to. As a workaround I set the 
system property by adding -Duser.home=%HOME% to the environment variable 
MAVEN_OPTS.

(2) The btree subproject checks out the Netbeans mdr btree implementation.
This requires cvs being installed on your system. The official netbeans cvs 
host might not work if you are behind a firewall that blocks the cvs port. 
Please consult http://www.netbeans.org/community/sources for more info. 
There is a special cvsroot if you are inside the Sun network (SWAN), please 
check the project.properties in the btree subproject.

If you do not have a cvs client installed on your system, you find a zip file 
including the Netbeans mdr btree implementation on the JDO wiki. Go to the 
bottom of page http://wiki.apache.org/jdo/SubversionRepository.

(3) Remarks about ri11:
- Calling 'maven build' in ri11 compiles the JDO RI sources and test classes 
and then runs the JUnit tests. 
- The maven goal runtest ('maven runtest') executes the full JDO RI test suite.
This includes running all the JUnit tests w/O and w/ security manager, plus 
some extra tests that require running more than one JVM. 
- If you prefer the JUnit gui please call 'maven -Dgui=true runtests'. This 
first starts a gui for running the JUnit tests w/o security manager. After 
you exit the gui it automatically starts a new gui running the JUnit tests
w/ security manager.

(4) Remarks about tck11:
Calling 'maven build' in tck11 compiles all the JDOTCK tests, runs the JDORI 
enhancer and then runs all the tests using the JDORI. 
To run the JDOTCK against an JDO implementation you should do the following:
- Place the jars of your JDO implementation in the directory iut_jars. All the 
jars in this directory are automatically added to the classpath.
- Check the property iut.runtck.properties in project.properties. It should 
refer to a file defining the PMF properties for your implementation. 
- Please add any system properties to the property iut.runtck.sysproperties in 
project.properties, e.g.
iut.runtck.sysproperties = -DMySystemProperty1=value -DMySystemProperty2=value
- If the JDO implementation comes with its own enhancer, please update the 
properties iut.enhancer.main, iut.enhancer.options, iut.enhancer.args, and 
iut.enhancer.sysproperties.
- Check the properties iut.applicationidentity.supported and 
iut.datastoreidentity.supported in project.properties and update them according 
to the JDO implementation to be tested.
- You run the TCK by calling 'maven runtck'. This first enhances the 
persistence-capable and persistence-aware classes for applicationidentity and 
for datastore identity. This uses the properties described in the previous item
in order to decide which kind of identitytype is supported. After enhancement 
'maven runtck' runs the JDO TCK test classes using the test configuration file
as specified by the property jdo.tck.configuration. You find the property in
project.properties.
- A test configuration is a file defining two properties:
jdo.tck.identitytype: either datastoreidentity or applicationidentity
jdo.tck.testclasses: a list of fully qualified class names of the test classes 
to be executed.
The first property is important to include the enhanced classes for this 
identitytype into the classpath. Today there is no checking whether the property
value is correct. There is a predefined property jdo.tck.alltests including all 
JDO TCK test classes. Please see the files datastoreidentity.conf and 
applicationidentity.conf in test/conf as an example.
- You can run the JUnit gui (instead of the batch mode) by setting the property
gui to true: 'maven -Dgui=true runtck'.

(5) Remarks about tck20:
This version of the TCK is under development.  It is premature to attempt to
run an implementation against it.  Currently only tests that use the persistence
capable classes in org.apache.jdo.tck.pc.mylib run without error.

- See Prerequisites concerning JPOX and Derby.

- Run "maven build" to build the tck.  This will compile, enhance, install the schemas, and run all the tests on all supported databases and identitytypes.

You may use the following custom goals and command line options
with tck20/maven:

Custom Goals:
    * runtck.jdori - runs the TCK on the JDO Reference Implementation
    * runtck.iut - runs the TCK on the implementation under test
    * installSchema - installs the database schema
    * enhance.jdori - enhances the class files using the JDO RI enhancer
    * enhance.iut - enhances the class files using the
                    implementation under test's enhancer

Command Line Options:
    -Djdo.tck.cfglist=<configuration file list>
          Overrides test/conf/configuration.list by supplying
          one or more space-separated test configuration files

      -Djdo.tck.dblist=<database list>
          Overrides the property value in project.properties by supplying
          one or more space-separated database names

      -Djdo.tck.identitytypes=<identity type list>
            Overrides the property value in project.properties by supplying
            one or more space-separated identity types (applicationidentity
            or datastoreidentity) to use for this run.


Maven looks for the following configuration files in test/conf:
    * configurations.list
          A list of files. Each file listed is a test configuration file.
    * test configuration files
          Each of these files sets values for
                jdo.tck.testdescription - an optional string describing
                    the purpose of these tests
                jdo.tck.classes - a list of one or more test classes.
                jdo.tck.testdata - fully qualified file name
                    (not required by all tests)
                jdo.tck.standarddata - fully qualified file name
                    (not required by all tests)
                jdo.tck.mapping - file designator that maven.xml uses
                    to build a javax.jdo.option.Mapping value and
                    corresponding schema name
    * exclude.list
          A list of test classes NOT to execute during a TCK test run
          [Not yet fully implemented]

For example, run "maven runtck.jdori" to run all tests or
"maven -Djdo.tck.cfg=<configuration file name> runtck.jdori"
to run one configuration.

(6) Logging
Apache JDO uses the apache commons logging package for logging.
Sub-projects ri11 and tck11 use several properties files to configure logging.
- common-logging.properties: specifies the logging implementation to use.
  It is tested with apache SimpleLog and JDK 1.4 logging.
- logging.properties: logger configuration when using JDK 1.4 logging.
- simplelog.properties: logger configuration when using apache SimpleLog.

(7) The file jdo_check.xml includes the checkstyle configuration. It is borrowed
from the sun_checks.xml, but does not use all of the sun rules and customizes 
some other rules. The checkstyle configuration is not yet finished.

(8) Mevenide is a nice maven plugin for IDEs (see http://mevenide.codehaus.org).
You find download instructions in http://mevenide.codehaus.org/download.html.
For Netbeans, once you installed the plugin, you should be able to open an 
existing maven project by File -> Open Project -> Open Project Folder.
Navigate to a directoy including a maven project (e.g. api11) and choose this 
directory. Netbeans will create a project folder. If you right-click the Maven 
project you can examine the contents of the project.xml (see Properties) or 
execute goals.
