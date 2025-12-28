<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  -->
# Apache JDO

The Apache JDO project includes the following subprojects:

* [api](https://github.com/apache/db-jdo/tree/main/api) contains source to build `jdo-api-{version}.jar`, which defines the JDO API for Java 1.8 and later.
* [exectck](https://github.com/apache/db-jdo/tree/main/exectck) contains source to build the maven plugin to run the JDO TCK the Reference Implementation (RI) or an implementation under test (IUT)
* [tck](https://github.com/apache/db-jdo/tree/main/tck) contains the JDO TCK for Java 1.8 and later.
* [parent-pom](https://github.com/apache/db-jdo/tree/main/parent-pom) contains the Maven `pom.xml` that ties the projects together.
* [specification](https://github.com/apache/db-jdo/tree/main/specification) contains the JDO API specification, in OpenDocument format.

Apache JDO releases may be downloaded from [the Apache JDO downloads page](http://db.apache.org/jdo/downloads.html).
Minor updates of releases are only available as source from the GitHub repository.
Follow the instructions [below](#building-the-jdo-api) to build the API from source.

For complete rules for certifying a JDO implementation, see [RunRules.html](https://github.com/apache/db-jdo/blob/main/tck/RunRules.html) in the *tck* project directory.

Raise any issues with the content of this project over in [Apache JIRA](https://issues.apache.org/jira/browse/JDO).


## Checking Out The Project

You can check out the source for this project using the `Clone or Download` option above. For example you can type either of the following:

    git clone git@github.com:apache/db-jdo.git

or

    git clone https://github.com/apache/db-jdo.git

which will create a folder `db-jdo` with all code in it. You can read up on [GIT here](https://git-scm.com/book/en/v2/Getting-Started-About-Version-Control).


## Prerequisites

You must install the software listed in the following sections to build the API, and successfully run the TCK.
Other dependencies, such as the reference implementation, [DataNucleus](https://github.com/datanucleus), and the Apache Derby database, are downloaded automatically by Maven.
Note that Apache JDO uses the Apache Commons Logging package for logging.

### Maven

You must have Maven (version 2+) to build the projects from source and to execute the TCK. You can download Maven from [here](http://maven.apache.org/download.html).

Note that Maven uses the `user.home` system property for the location of the Maven local repository: `${user.home}/.m2/repository`.
Under Windows this system property is `C:\Documents and Settings\{user}` no matter what the `HOME` variable is set to. As a workaround you may set the system property by adding `-Duser.home=%HOME%` to the environment variable `MAVEN_OPTS`.

### JNDI Implementation 

The JNDI test cases in *tck* need a JNDI implementation. The TCK is configured to use the TCK's own JNDI mock implementation. To use your own implementation, add the dependencies to the TCK's `.pom` or put the implementation jar files directly into `lib/ext`. Then update `jndi.properties` in the TCK directory `src/conf`.

For example, to use Oracle's implementation, go [here](http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-java-plat-419418.html#7110-jndi-1.2.1-oth-JPR). Accept the license agreement and download *File System Service Provider, 1.2 Beta 3* and then unpack the downloaded zip into `lib/ext`. It includes the jars `fscontext.jar` and `providerutil.jar`. 
Then update the factory class property in `tck/src/main/resources/conf/jndi.properties`: 
`java.naming.factory.initial=com.sun.jndi.fscontext.RefFSContextFactory`.


## Building from Top Level Project

To build Apache JDO with all subprojects (api, exectck, tck) go to the root directory of the branch you are working in

    mvn clean install

This will build the artifacts *jdo-api* and *jdo-exectck* and will then run the TCK.


## Building the JDO API

To build just the API, change to the "api" directory of the branch you are working in and run

    mvn clean install

This will build the *jdo-api* artifact and install it in your local Maven repository.


## Running the JDO TCK on an Implementation Under Test

Firstly build from the top level project as described above.
To run the JDO TCK on an Implementation Under Test, edit [tck/pom.xml](https://github.com/apache/db-jdo/blob/main/tck/pom.xml) and add the iut dependencies to the profile called *iut*. 
Also check the following files under [tck/src/main/resources/conf](https://github.com/apache/db-jdo/tree/main/tck/src/main/resources/conf) and change the content to the needs of the Implementation Under Test: `iut-jdoconfig.xml`, `iut-log4j.properties`, `iut-persistence.xml` and `iut-pmf.properties`.

Change to the "tck" directory of the branch you are working in and run

    mvn -Djdo.tck.impl="iut" clean install

and this will run the TCK (via the "jdo-exectck" plugin) on the Implementation Under Test on all supported databases and identity types.

The *jdo-exectck* Maven plugin (built from the *exectck* directory) has various options so you can run other implementations or only run particular tests.


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
* -Djdo.tck.doRunTCK : Setting this parameter to false will bypass running the TCK.
* -Djdo.tck.doInstallSchema : Setting this parameter to *false* will bypass schema installation.
* -Djdo.tck.doEnhance : Setting this parameter to false will bypass enhancement.
* -Djdo.tck.runTCKVerbose : Setting this parameter to *true* will display test progress and error output while the TCK is running.
* -Djdo.tck.verbose: Setting this parameter to *true* will run the TCK tests in verbose mode.
* -Djdo.tck.onFailure : Specifies how test failures are treated. *failFast* will immediately abort the test run. *failGoal* (default) will execute the whole TCK before failing. *logOnly* will report failures to console and logs only but return 'SUCCESS' to the Maven execution environment.
* -Djdo.tck.debugTCK: Setting this parameter to *true* will run the TCK in a debugger.
* -Djdo.tck.debug.jvmargs: User-supplied arguments for debug directives.
* -Djdo.tck.pmfproperties: Name of file in src/conf containing pmf properties.
* -Djdo.tck.excludefile: Name of file in src/conf containing property jdo.tck.exclude, whose value is a list of files to be excluded from testing.
* -Djdo.tck.signaturefile: Name of the signatute file used to test the signatures of the JDO API classes.
* -Djdo.tck.cleanupaftertest: Setting this parameter to *false* withh retain test output for debugging.
* -Djdo.tck.jvmproperties: JVM properties.
* -Djdo.tck.logfile: Name of the tck log file.
* -Djdo.tck.testrunner.class: Class used to run a batch of tests.
* -Djdo.tck.testrunner.details: Output mode for test run. Use one of: none, summary, flat, tree, verbose, testfeed. If `none`is selected, then only the summary and test failures are shown.
* -Djdo.tck.testrunner.colors: Setting this parameter to *enable* displays colors in the junit result log file.
* -Djdo.tck.parallel.execution: Setting this parameter to *false* will disable parallel exceution of tck test methods in parallel threads.
* -Djdo.tck.parallel.config.dynamic.factor: Computes the desired parallelism based on the number of available processors/cores multiplied by the factor configuration parameter. Default: junit default.
* -Djdo.tck.datastore.supportsQueryCancel: Setting this parameter to *true* indicates whether the datastore supports query canceling.
* -Dproject.lib.ext.directory: Location of third party libraries such as JNDI.
* -Ddatabase.runtck.sysproperties: Properties to use in accessing database.

### Examples

This example runs the test configurations specified in `alltests.conf` and `cfg1.conf` on the JDORI, using all supported identity types and databases.

    mvn -Djdo.tck.cfglist="alltests.conf cfg1.conf" install



### Files

While running the TCK, maven uses the following configuration files in src/conf:

* configurations.list  : A list of files. Each file listed is a test configuration file.
* test configuration files (*.conf). Each of these files sets values for
    * jdo.tck.testdescription : An optional string describing the purpose of these tests
    * jdo.tck.classes : A list of one or more test classes (required)
    * jdo.tck.testdata : The fully qualified name of the class that creates test data (optional)
    * jdo.tck.mapping : The file designator that maven.xml uses to build a javax.jdo.option.Mapping value and corresponding schema name (required)
* exclude.list  : A list of test classes NOT to execute during a TCK test run

### Warnings

There is a profile called `warnings` that configures the maven-compiler-plugin to show compiler warnings and passes 
`-Xlint:all,-try,-rawtypes` as argument to javac. Activate the warnings profile to enable compiler warnings. 
Please note, the tck enables the profile `jdori` per default and this default is disabled when specifying the profile 
`warnings`. So if you want to run the tck with the jdori with warnings enabled please use the following:

    mvn -Pwarnings,jdori clean install

You can pass different compiler arguments using the `-D` option:

    mvn -Pwarnings,jdori -DcompilerArgument=-Xlint:all clean install

### Formatting

The JDO project uses the google-java-format for checking and reformatting the Java code to comply with 
Google Java Style [Google Java Style](https://google.github.io/styleguide/javaguide.html). 

The code is formatted using the [Google Java Format](https://github.com/google/google-java-format) tool.
The formatter can be run in the command line or can be integrated into IntelliJ or Eclipse as a plugin.
More information on this is given in the [GJF README](https://github.com/google/google-java-format#using-the-formatter).

#### Formatting using Maven

There are two profiles to support code formatting using the maven fmt-maven-plugin.

* `verify-format` checks the formatting of the project's Java files. It prints the list of the files that are not compliant.

        mvn -Pverify-format clean compile

* `format` reformats the project's Java files.

        mvn -Pformat clean compile

#### GitHub Action Integration

The formatting of the code is checked automatically through the GitHub actions for every PR and/or push on master.

