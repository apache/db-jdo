<!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  -->
Running the JDO 3.2 Technology Compatibility Kit
================================================

-   [Overview](#overview)
-   [Prerequisites](#prerequisites)
-   [Installation](#installation)
-   [Modifying the Configuration](#configuration)
-   [Running the Tests](#running)
-   [Debugging the IUT while Running TCK tests](#debugging)
-   [Publishing the Results of the TCK Tests](#results)
-   [First Level TCK Appeals Process](#firstlevel)
-   [Prerequisites](#prerequisites)

10-October-2021

<span id="overview"></span>

Overview
-----------------------

In order to demonstrate compliance with the Java Data Objects
specification, an implementation must successfully run all of the TCK
tests that are not on the \"excluded\" list. The implementation is
hereinafter referred to as the IUT (Implementation Under Test).

The results must be posted on a publicly accessible web site for
examination by the public. The posting includes the output of the test
run, which consists of multiple log files containing configuration
information and test results. For an example of the required posting,
please see <http://db.apache.org/jdo/tck/final>.

<span id="prerequisites"></span>

Prerequisites
-----------------------

In order to run the TCK, you must install Maven 2.x Maven
<http://maven.apache.org/> is the driver of the test programs.

You must test the IUT on all configurations that the IUT supports. This
includes different hardware and operating systems, different versions of
Java, and different datastores. The TCK supports Java version 1.8.

<span id="installation"></span>

Installation
-----------------------

Download the jdo.3.x-src.zip or jdo.3.x-src.gz file from the
distribution location. Unpack the archive file into a directory of your
choice. Follow the instructions in the root level README.html file for
building and running the JDO TCK. Please take note of the following
files:

-   Maven configuration file \"pom.xml\" in the top level directory and
    subprojects. These files must not be changed with the exception of
    the one in tck (see below).

-   lib/ext - Copy the jar files fscontext.jar and providerutil.jar used
    by the JNDI tests into this directory. It is permitted to use a
    different JNDI implementation; see README.html for more information.

-   the TCK directory, which contains:

    -   Maven configuration file \"pom.xml\". Please edit the file and
        add the iut dependencies to the profile called iut.

    -   assertions - contains the assertions file identifying the
        assertions tested by the tests. This is for reference.

    -   target - this directory contains artifacts of compiling and
        running the tests. It does not exist in the distribution and
        will be created by the maven build process.

    -   src - this directory contains the test configuration files and
        directories:

        -   testdata - this directory contains data (represented as .xml
            files) loaded into the datastore for tests. These files must
            not be modified.

        -   sql - this directory contains DDL to define the tables used
            in the tests. The files distributed must not be modified.
            Files may be created for databases for which the DDL for the
            database under test is not provided.

        -   jdo - this directory contains .jdo metadata files for the
            persistent classes used in the tests. These files must not
            be modified.

        -   orm - this directory contains .orm metadata files to map the
            persistent classes to the sql tables. These files must not
            be modified except to add DDL-generation information (which
            is not used by the TCK).

        -   java - this directory contains the source code to the TCK
            tests. These files must not be modified.

        -   conf - this directory contains the configuration information
            for the test runs. The files iut-pmf.properties,
            iut-log4j.properties, iut-jdoconfig.xml, and
            iut-persistence.xml in this directory must be changed to
            provide properties for the IUT persistence manager factory.
            The file jndi.properties may be changed to use a different
            jndi provider. Other files must not be modified, except to
            put a successfully challenged test case into the
            trunk/tck/test/conf/exclude.list. Please see below.

<span id="configuration"></span>

Modifying the Configuration
-----------------------

The Implementation Under Test (IUT) can be installed into the lib/iut
directory. Any jar files in this directory are added to the class path
used to run the tests.

There are properties in the build.properties file in the TCK directory
that must be changed to configure the IUT execution and enhancement
(optional) environment. These properties begin with iut.runtck and
iut.enhancer.

There is are three properties files that must be modified to be
IUT-specific, all located in the TCK src/conf directory. The
iut-pmf.properties file contains information used to construct the
PersistenceManagerFactory used in the tests. iut-jdoconfig.xml and
iut-persistence.xml also contain PersistenceManagerFactory properties
used only in tests in the
org.apache.jdo.tck.api.persistencemanagerfactory.config package.

SQL DDL files are provided for the sql table definitions. The existing
files must not be changed, but files may be added in the directory in
order to provide DDL for additional databases supported by the JDO
implementation under test.

<span id="running"></span>

Running the Tests
-----------------------

Follow the instructions in README.html for running the TCK on the JDO
Reference Implementation and the Implementation Under Test. This will
produce console output plus a directory in the tck/target/logs directory
whose name contains the date/time the tests were started. This directory
contains the output of the tests. This is the directory to be published.

Some of the TCK tests require the implementation to support up to 20
instances of PersistenceManager with open transactions simultaneously.

<span id="debugging"></span>

Debugging the IUT while Running TCK tests
-----------------------

Execute \"mvn jdo-exectck:help\" in the TCK directory in order to get
information on running the TCK tests with a debugger. In particular,
properties jdo.tck.cleanupaftertest, jdo.tck.cfglist,
jdo.tck.identitytypes, and jdo.tck.dblist may be useful.

If you make a change to the IUT enhancer while debugging the TCK tests
(for implementations that use an enhancer) you must remove the
target/classes directory before continuing in order to make sure that
the classes are re-enhanced by the changed code.

<span id="results"></span>

Publishing the Results of the TCK Tests
-----------------------

With a successful test run, the log directory with the results of the
tests must be published on a publicly-available web site. The unmodified
directory is the self-certification of the successful TCK test run.

<span id="firstlevel"></span>

First Level TCK Appeals Process
-----------------------

If any test does not pass on the JDO implementation under test, this may
be due to an error in the implementation or in the TCK test. If you
believe that the failure is due to an error in the TCK test, you may
challenge the test. To do so, send email to: <jdo-dev@db.apache.org>
with a subject line containing \"CHALLENGE\" and the name of the test
program, e.g. org.apache.jdo.tck.api.persistencemanager.ThreadSafe.java;
and the body of the email containing the details of the challenge.

The Maintenance Lead will respond within 15 working days with a decision
on whether there is an error in the test case. If the issue is found by
the Maintenance Lead to be due to an error in the test case, the
Maintenance Lead might patch the erroneous test or add the test to the
exclude list. The user can obtain the TCK updates by checking out the
latest minor version branch, 3.n.1. If a fix is not provided within 15
working days of the receipt of the challenge, then the user may put the
test into the TCK file src/conf/exclude.list and it will not be run as
part of the TCK.

Decisions of the Maintenance Lead may be appealed to the full expert
group. A vote of the full expert group will be conducted by the
Maintenance Lead, and a majority of votes cast will decide the issue.
The Maintenance Lead has one vote, as does each member of the expert
group at the time of the vote.
