# Apache JDO

The JDO project includes the following subprojects:

* *api* contains source to build jdo-api-{version}.jar, which defines the JDO API for Java 1.5 and later.
* *exectck* contains source to build the maven plugin to run the TCK the Reference Implementation (RI) or an implementation under test (IUT)
* *tck* contains the JDO Technology Compatibility Kit for Java 1.5 and later.
* *parent-pom* contains the maven pom.xml that ties the projects together.

JDO releases may be downloaded from [the Apache JDO downloads page](http://db.apache.org/jdo/downloads.html).
Minor updates of releases are only available as source from the GitHub repository.
Follow the instructions [below](link:#building) to build the API from source.

For complete rules for certifying a JDO implementation, see [RunRules.html](https://github.com/apache/db-jdo/blob/master/tck/RunRules.html) in the *tck* project directory.


## Prerequisites

You must install the software listed in the following sections
to successfully run the TCK.
Other dependencies, such as the reference implementation, DataNucleus,
and the Apache Derby database, are downloaded automatically by maven.
Note that Apache JDO uses the apache commons logging package for logging.

### Maven

You must have Maven version 2+ to build the projects from source and to execute the TCK. You can download maven from [here](http://maven.apache.org/download.html)

Note that maven uses the `user.home` system property for the location of the maven local repostitory: `${user.home}/.m2/repository`.
Under Windows this system property is `C:\Documents and Settings\{user}` no matter what the HOME variable is set to. 
As a workaround you may set the system property by adding -Duser.home=%HOME% to the environment variable `MAVEN_OPTS`.

### JNDI Implementation (fscontext.jar and providerutil.jar)

The JNDI test cases in tck need a JNDI implementation.
The TCK is configured to use Sun's JNDI implementation.
To use your own implementation, put the implementation
jar files into <i>lib/ext</i> and update `jndi.properties` in the TCK directory src/conf.  
To download Oracle's implementation, go [here](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#7110-jndi-1.2.1-oth-JPR).
Accept the license agreement and download *File System Service Provider, 1.2 Beta 3* and then unpack the downloaded zip into `lib/ext`. It includes the jars `fscontext.jar` and `providerutil.jar`.


## Building from Top Level TCK Project

For instructions for checking out the JDO source, see the Apache JDO [source code](http://db.apache.org/jdo/svn.html) page.

To build JDO with all subprojects go to the root directory of the branch you are working in (or trunk)

    mvn clean install

This will build the artifacts *jdo-api* and *jdo-exectck* and will run the TCK.


## Building the API

To build the API, change to the "api" directory of the branch you are working in (or trunk) and run

    mvn clean install

This will build the *jdo-api* artifact and install it in your local Maven repository and run the TCK on the Reference Implementation.


## Running the TCK on an Implementation Under Test

First build the from the top level TCK project as described above.
To run the JDO TCK on an Implementation Under Test, edit [tck/pom.xml](https://github.com/apache/db-jdo/blob/master/tck/pom.xml) and add the iut dependencies to the profile called *iut*. 
Also check the following files under [tck/src/main/resources/conf](https://github.com/apache/db-jdo/tree/master/tck/src/main/resources/conf) and change the content to the needs of the Implementation Under Test:
`iut-jdoconfig.xml`, `iut-log4j.properties`, `iut-persistence.xml` and `iut-pmf.properties`.

Change to the "tck" directory of the branch you are working in (or trunk) and run

    mvn -Djdo.tck.impl="iut" clean install

and this will run the TCK (via the "jdo-exectck" plugin) on the Implementation Under Test on all supported databases and identity types.

The "jdo-exectck" plugin has various options so you can run other implementations or only run particular tests.


### Custom Goals

The *jdo-exectck* Maven plugin has the following custom goals

* *help* : Displays help text describing custom goals and options
* *installSchema* : Installs the database schema
* *enhance* : enhances the test classes being used
* *runtck* : Runs the TCK


### Command Line Options

The *jdo-exectck* Maven plugin has the following options

* -Djdo.tck.impl : either *jdori* (reference implementation) or *iut* (implementation under test).
* -Djdo.tck.cfglist : Overrides the definition of jdo.tck.cfglist found in tck/src/conf/configuration.list by supplying one or more comma-separated test configuration files. Test configuration files typically have the .conf extension. To run a single test, create a .conf file (copy an existing file) and put the test into the jdo.tck.classes property.
* -Djdo.tck.dblist : Overrides the property value in project.properties by supplying one or more comma-separated database names. Currently only derby is supported.
* -Djdo.tck.identitytypes : Overrides the identity types to be run, supplying one or more comma-separated identity types (*applicationidentity* or *datastoreidentity*) to use for this run.
* -Djdo.tck.impl.logfile : Location of implementation log file. Default: `${user.dir}/datanucleus.txt`
* -Djdo.tck.doInstallSchema : Setting this parameter to *false* will bypass schema installation.
* -Djdo.tck.doEnhance : Setting this parameter to false will bypass enhancement.
* -Djdo.tck.doRunTCK : Setting this parameter to false will bypass running the TCK.
* -Djdo.tck.runTCKVerbose : Setting this parameter to *true* will display test progress and error output while the TCK is running.
* -Djdo.tck.onFailure : Specifies how test failures are treated. *failFast* will immediately abort the test run. *failGoal* (default) will execute the whole TCK before failing. *logOnly* will report failures to console and logs only but return 'SUCCESS' to the Maven execution environment.


### Examples

Example 1 : Installs the database schema for datastore identity for all supported databases.

    mvn -Djdo.tck.identitytypes=datastoreidentity jdo-exectck:installSchema


Example 2 : Runs the test configurations specified in `alltests.conf` and `cfg1.conf` on the JDORI, using all supported identity types and databases.

    mvn -Djdo.tck.cfglist="alltests.conf cfg1.conf" jdo-exectck:runtck



### Files

While running the TCK, maven uses the following configuration files in src/conf:

* configurations.list  : A list of files. Each file listed is a test configuration file.
* test configuration files (*.conf). Each of these files sets values for
    * jdo.tck.testdescription : An optional string describing the purpose of these tests
    * jdo.tck.classes : A list of one or more test classes (required)
    * jdo.tck.testdata : The fully qualified file name of the xml test data file(optional)
    * jdo.tck.standarddata : The fully qualified file name of the xml test data file(optional)
    * jdo.tck.mapping : The file designator that maven.xml uses to build a javax.jdo.option.Mapping value and corresponding schema name (required)
* exclude.list  : A list of test classes NOT to execute during a TCK test run


