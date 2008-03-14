This is the official release of the JDO 2 project. It includes
the JDO 2.1 TCK and its dependencies:

- api2 contains source to build jdo2-api-2.1.jar, which defines the JDO API
  version 2.1 for Java 1.5 and later.
- api2-legacy contains source to build jdo2-api-legacy-2.1.jar, which defines
  the JDO API version 2.1 for Java 1.4 and earlier.
- core20 contains the JDO2 core, including utility and metadata model classes
- enhancer20 contains the utility classes used for verifying enhanced files
- tck2 contains the JDO 2.1 Technology Compatibility Kit for Java 1.5 and later.
- tck2-legacy contains the JDO 2.1 Technology Compatibility Kit for Java 1.4
  and earlier.

-------------
Prerequisites
-------------

The following are needed to successfully run the TCK. (For complete rules for
certifying a JDO implementation, see RunRules.html in the top level
tck2 or tck2-legacy project directory.)

- Maven
You must have Maven version 1.0.1, 1.0.2, or 1.1 to build the projects
from source and to execute the TCK. You can download maven from 
http://maven.apache.org/start/download.html

Note that maven uses the user.home system property for the location
of the maven local repostitory: ${user.home}/.maven/repository.
Under Windows this system property is C:\Documents and Settings\<user> 
no matter what the HOME variable is set to. As a workaround you may set the 
system property by adding -Duser.home=%HOME% to the environment variable 
MAVEN_OPTS.

- JNDI implementation (fscontext.jar and providerutil.jar)
The JNDI test cases in tck2 need a JNDI implementation.
The TCK is configured to use Sun's JNDI implementation.
To use your own implementation, put the implementation
jar files into lib/ext and update jndi.properties in the TCK directory
src/conf.  To download Sun's implementation, go to 
http://java.sun.com/products/jndi/downloads/index.html,
click the Download button at 'Download JNDI 1.2.1 & More', accept a license 
agreement, download 'File System Service Provider, 1.2 Beta 3' and then unpack
the downloaded zip. It includes the jars fscontext.jar and providerutil.jar.

- JPOX
The Reference Implementation for JDO 2.1 is JPOX 1.2. The tck2 subproject 
automatically downloads the JPOX jar files via maven configuration.

- Derby
The default datastore for tck2 is Apache Derby. The tck2 subproject 
automatically downloads version 10.2.1.6 of derby.jar and derbytools.jar.

-------
Notes
-------

- Logging
Apache JDO uses the apache commons logging package for logging.

- Checkstyle
The file jdo_check.xml includes the checkstyle configuration. It is borrowed
from the sun_checks.xml, but does not use all of the sun rules and customizes 
some other rules. The checkstyle configuration is not yet finished.

- Mevenide
Mevenide is a nice maven plugin for IDEs (see http://mevenide.codehaus.org).
You find download instructions in http://mevenide.codehaus.org/download.html.
For Netbeans, once you installed the plugin, you should be able to open an 
existing maven project by File -> Open Project -> Open Project Folder.
Navigate to a directory including a maven project (e.g. api2) and choose this 
directory. Netbeans will create a project folder. If you right-click the Maven 
project you can examine the contents of the project.xml (see Properties) or 
execute goals.

-------
Running the TCK
-------

In the tck2 project, run "maven build" to build the tck.  This will 
compile, enhance, install the schemas, and run all the tests on the RI on all 
supported databases and identitytypes.

You may use the following custom goals and command line options
with the tck2 project:

Custom Goals:
    * help - displays help text describing custom goals and options
    * runtck.jdori - runs the TCK on the JDO Reference Implementation
    * runtck.iut - runs the TCK on the implementation under test
    * installSchema - installs the database schema
    * enhance.jdori - enhances the class files using the JDO RI enhancer
    * enhance.iut - enhances the class files using the
                    implementation under test's enhancer
    * debugtck.jdori - waits for a debugger to attach and then runs the TCK
                     on the JDO RI
    * debugtck.iut - waits for a debugger to attach and then runs the TCK
                     on the implementation under test
    * cleanClasses - deletes classes and enhanced classes

Command Line Options:
    -Djdo.tck.cfglist=<configuration file list>
          Overrides the definition of jdo.tck.cfglist found in
          tck2/src/conf/configuration.list by supplying
          one or more space-separated test configuration files.
          Test configuration files typically have the .conf extension.
          To run a single test, create a .conf file (copy an existing
          file) and put the test into the jdo.tck.classes property.

      -Djdo.tck.dblist=<database list>
          Overrides the property value in project.properties by supplying
          one or more space-separated database names

      -Djdo.tck.identitytypes=<identity type list>
            Overrides the property value in project.properties by supplying
            one or more space-separated identity types (applicationidentity
            or datastoreidentity) to use for this run.

      -Djdo.tck.cleanupaftertest=xxx - true/false. Setting it to false will
            retain data in database after test. This will allow inspection of
            data after test is run. Default is true

      -Djdo.tck.debug.port=##### - the port number the JVM should listen for
            a debugger on (default 8787)

      -Djdo.tck.debug.jvmargs=xxx - the "-Xdebug ..." arguments in the event
            you want to supply your own debug directives

Maven looks for the following configuration files in src/conf:
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

Examples:
   maven -Djdo.tck.identitytypes=datastoreidentity installSchema
       Installs the database schema for datastore identity for all
       supported databases

   maven -Djdo.tck.cfglist="alltests.conf cfg1.conf" runtck.jdori
       Runs the test configurations specified in alltests.conf and cfg1.conf
       on the JDORI, using all supported identity types and databases.

   maven -Djdo.tck.cfglist=detach.conf debugtck.jdori
       Runs the test detach.conf configuration, waiting for a debugger to
       attach on the default port

   maven -Djdo.tck.cfglist=detach.conf -Djdo.tck.debug.port=9343 debugtck.jdori
       Runs the test detach.conf configuration, waiting for a debugger to
       attach on port 9343

Note:
   By default, the database schema is NOT installed when the custom goals
     runtck.iut and runtck.jdori are run.
   maven build installs the database schema and runs the TCK on the
     JDO Reference Implementation.
   Enhancement is always done before running the TCK when the enhanced classes
     are not up to date.

