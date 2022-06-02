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
-   [Overview of the process](#procoverview)
-   [Detailed process steps](#procdetail)
-   [Site updates](#site)
-   [Post release modifications and documentation](#postrelease)

How to Release an Apache JDO Distribution
=========================================

A distribution (release) of JDO is built from a branch of git. A release 
consists of a compressed source (e.g. zip file) and associated checksums and 
signatures. It is copied into a release directory, from which it is staged 
and tested.
Once approved by a formal vote of the DB PMC, it is copied to the official 
Apache distribution infrastructure and propagated to mirror servers around the world.

The process is performed by a release manager with cooperation from
testers in the community.

<span id="procoverview"></span>

Overview of the process
-----------------------

The community first decides on the name of the release. The format of
the name is *spec-number*.*major*.*minor*. A trailing *minor* number
with a zero value is right trimmed, so there might be a 3.0.1 but not a
3.0.0.

Interim releases prior to final release are identified by a suffix on
the release number. Common suffixes include: -alpha, -beta, -beta2,
-rc1, -rc2. Generally, the suffixes are part of the release plan, and
the contents of each suffix release are agreed by the community. There
might be significant changes in functionality between the suffix
releases. Each suffix release goes through the process documented here.

The community decides whether to continue development on the main branch 
while the release is in progress. In this case, a branch is created 
first. If a new branch is needed, the release manager makes a new branch 
from the main branch (for a major release) or from another branch (for 
a minor release). If a new branch is not needed, the release will be 
created from the main branch and a release branch will only be created 
later for a maintenance release.

The release manager follows the Apache process detailed below to build
and deploy a release.

The release manager calls for the community to test the release.

The community tests the release. If necessary, cycle until all issues
are resolved.

The release manager closes the staging repository

The release manager calls for a vote to release by sending a message to
the community and forwarding the message to the PMC.

The community votes on the release. If necessary, cycle until issues are
resolved.

The release manager notifies the PMC of the successful vote outcome.
A successful vote includes three +1 votes from PMC members and more +1 
than -1 votes.
The release manager copies the approved release to the official Apache 
distribution directory which is then mirrored worldwide.

The release manager notifies the worldwide community of the availability
of the release.

The release manager updates the JDO web sites
(http://db.apache.org/jdo/index.html, http://java.sun.com/jdo/).

If bugs are found or test challenges are sustained after the release is
approved and distributed, the release manager creates a new branch to
address the bugs found and proceeds from the beginning.

<span id="procdetail"></span>

Detailed process steps
----------------------

1. Verify licensing of the sources by running 
    [Apache Rat](http://creadur.apache.org/rat) to
    check the sources for any licence issues.

        mvn clean apache-rat:check

2. If necessary, create a branch from the main branch and increment the 
    spec or major number. For example, create a "3.1" branch from the 
    main branch.

        git checkout -b 3.2
        git push -u origin 3.2

3. If needed, update the dependency to the RI, DataNucleus, in the tck
    `pom.xml`.

4. If needed, apply patches from the main branch to the release branch.

5. Update version numbers where necessary in projects to be released,
    if these changes haven't been made previously (some changes are for majopr releases only).
    Updating `pom.xml` files is not necessary at this point. Check the following files:
     * `README.md`
     * `tck/RunRules.md`: Update version number and date

6. Check the `<scm>` settings in the parent `pom.xml` file in the new branch. Note that and
   that the maven release plugin tends to remove the section. It should be between 
   `<mailingLists>` and `<properties>`, and look as follows:
   
       <scm child.scm.connection.inherit.append.path="false" child.scm.developerConnection.inherit.append.path="false"
         child.scm.url.inherit.append.path="false">
           <connection>scm:git:https://gitbox.apache.org/repos/asf/db-jdo.git</connection>
           <developerConnection>scm:git:https://gitbox.apache.org/repos/asf/db-jdo.git</developerConnection>
           <url>https://gitbox.apache.org/repos/asf?p=db-jdo.git</url>
       </scm>

7. Follow the instructions at [Publishing Maven
    Artifacts](https://infra.apache.org/publishing-maven-artifacts.html)
    to set up your development environment.

8. Copy the JNDI implementation jars (`providerutil.jar` and
    `fscontext.jar`) to the branch `lib/ext` directory. This is needed to
    test the tck before distributing it.
    **Do not check these in into the repository**

9. Make sure the TCK passes

       mvn clean install

10. Build the distribution with the following command:

        mvn clean install -Papache-release -Djdo.tck.doRunTCK=false -Djdo.tck.doInstallSchema=false -Djdo.tck.doEnhance=false
  
    This creates the .jar and .pom files in the target directory of each
    subproject. Be prepared to enter your key passcode when prompted, 
    to create the `<artifact>.asc` GPG signatures.
    This happens multiple times.

11. Run [Apache Rat](http://creadur.apache.org/rat) on the release 
    artifacts to verify the results of the build.

    Download Apache Rat from https://creadur.apache.org/rat/download_rat.cgi 
    and extract the Jar-File, e.g. `apache-rat-0.13.jar` 

        java -jar apache-rat-0.13.jar -E .rat-excludes -d api/target/jdo-api-3.2-SNAPSHOT-sources.jar
        java -jar apache-rat-0.13.jar -E .rat-excludes -d tck/target/jdo-tck-3.2-SNAPSHOT-sources.jar


13. Do a dry run prepare and deployment of a *snapshot release*. You might
    want to do this in a fresh workspace, since you cannot have local
    modifications when preparing a release. The files in `lib/ext and`
    `lib/jdori` count as local modifications. Be prepared to enter your
    key passcode when prompted. This may happen multiple times.

    The release plugin will ask the following questions and expects an input:
    * What is the release version for "JDO Root POM"?
    * What is the SCM release tag or label for "JDO Root POM"?
    * What is the new development version for "JDO Root POM"?
    
    The proposed defaults should be fine.

        mkdir tmp
        cd tmp 
        git clone https://github.com/apache/db-jdo.git
        cd db-jdo
        git checkout 3.2
        mvn release:prepare -Papache-release -DautoVersionSubmodules=true -DdryRun=true -Dresume=false
        mvn deploy -Papache-release 
  
    Check the artifacts at the Maven release repository for 
    [javax.jdo](https://repository.apache.org/content/repositories/snapshots/javax/jdo/jdo-api/) and
    [org.apach.jdo](https://repository.apache.org/content/repositories/snapshots/org/apache/jdo/).

14. Make sure that the key used to sign the artifacts is included in the 
    [KEYS](https://dist.apache.org/repos/dist/release/db/jdo/KEYS) file (this is an SVN repository).

15. Prepare and release the artifacts. There are interoperability issues
    with the maven release plugin and cygwin, so if on Windows, use a
    Windows command window for this step and the following one.

        mvn release:clean -Papache-release
        mvn release:prepare -Papache-release -DreleaseVersion=3.2-RC1 -DdevelopmentVersion=3.2-RC2-SNAPSHOT -Dtag=v3.2-rc1 -DautoVersionSubmodules=true
    
    or, in case of the final release

        mvn release:clean -Papache-release
        mvn release:prepare -Papache-release -DreleaseVersion=3.2 -DdevelopmentVersion=3.2.1-SNAPSHOT -Dtag=v3.2 -DautoVersionSubmodules=true
    
16. Stage the release for a vote.

        mvn release:perform -Papache-release

17. Go to [the Nexus repository](https://repository.apache.org/index.html) and login with your apache account, then:
    1. Click on "Staging Repositories" and select the current release.
    2. Select the "Content" tab in the lower part of the UI and use right-click to remove unnecessary files,
    such as `.sha512.md5` and `.sha512.sha1`.
    3. "Close" the staged repository. 
    4. Press the "Refresh" button to see the new status 'closed'. 
    
    See [Closing an Open Repository](https://help.sonatype.com/repomanager2/staging-releases/managing-staging-repositories#ManagingStagingRepositories-ClosinganOpenRepository)
    for details.

18. Send an announcement (e.g. Subject: Please test staged JDO 3.2 release) 
    to test the release to the
    jdo-dev@db.apache.org alias. If problems are found, fix and repeat.

19. Send an announcement to vote on the release to the
    jdo-dev@db.apache.org alias. The message subject line contains
    \[VOTE\]. Forward the \[VOTE\] message to private@db.apache.org.
    Iterate until you get a successful vote. Mail the results of the
    vote to jdo-dev@db.apache.org, cc: general@db.apache.org, and
    include \[VOTE\] \[RESULTS\] in the subject line.

20. After testing and voting is complete, follow the instructions at
    [Releasing a Staging Repository](https://help.sonatype.com/repomanager2/staging-releases/managing-staging-repositories#ManagingStagingRepositories-ReleasingaStagingRepository)
    to release the artifacts.

21. Update the distribution repository at https://apache.org/dist/db/jdo/
    by adding the new release directory.
    Check out the SVN repository at
    https://dist.apache.org/repos/dist/release/db/jdo and add the new
    release under the new directory. Since this is a source release,
    the only artifacts published are the ...source-release.zip and ...source-release.tar.gz
    and the corresponding .asc and .sha512. Make sure that
    the key used to sign the artifacts is included in the KEYS file.
    Committing the new directory will trigger an update to the mirrors.

22. After updating the site (below), announce the release to the Apache
    community via email to announce@apache.org This must be sent from an
    @apache.org email address. **Be aware that by sending to this
    address you will be bombarded with piles of emails from people with
    "I'm out of the Office" as if you really cared.**

23. Finally, use the maven version plug-in on the `main` branch to update version numbers in the
    `pom.xml` files at the root and subproject levels.

        mvn versions:set -DnewVersion=3.2-RC1 -DprocessAllModules
        mvn versions:update-parent -DallowSnapshots=true -DnewVersion=3.2-RC1 -DprocessAllModulesss
        mvn versions:commit -DprocessAllModules

<span id="site"></span>

Site updates
------------

1.  Update the Apache JDO web site to point the downloads page to the
    release.
    1.  In [dbo-jdo-site/src/main/asciidoc](https://github.com/apache/db-jdo-site/tree/main/src/main/asciidoc)
        create release-*N.n*.adoc.

    2.  Edit db-jdo-site/src/main/asciidoc/downloads.adoc to link to the 
        new release page .adoc document.

    3.  Build and test as described in the [db-jdo-site/README.md](https://github.com/apache/db-jdo-site/blob/main/README.md) 
        document. 
        
2.  Change the link to RunRules on the
    [TCK](http://db.apache.org/jdo/tck.html) page to link to the
    RunRules.md file of the latest release.
3.  Update the news list on the site home page to announce the new
    release.
4.  Update the specification page to link to the new specification pdf
    document. If the release has been approved by the JCP, link to the
    documentation page of the JCP web site. If the release has not been
    approved by the JCP, link to the .pdf in the JDO source repository.
5.  Add the javadoc for the release to the site.
    1.  Make a new directory under [db-jdo-site/src/main/resources/javadoc](https://github.com/apache/db-jdo-site/tree/main/src/main/resources/javadoc) 
        for the release, e.g. api2.1. We'll call it *docsdir*.
    2.  Download the javadoc artifact from the repository and copy it to
        *docsdir*.
    3.  Unzip it in *docsdir*.
    4.  Do `git add` on *docsdir*.
    5.  Edit  db-jdo-site/src/main/asciidoc/javadoc.adoc and add links to the new javadoc.
6.  Build and test. Follow the instructions in [db-jdo-site/README.md](https://github.com/apache/db-jdo-site/blob/main/README.md)
    to push the site changes to the Apache web site.

<span id="postrelease"></span>

Post release modifications and documentation
--------------------------------------------

Follow this procedure if a significant bug is found or if the TCK must
be modified because a test challenge is found to be valid.

1.  Create a new branch from the release branch, incrementing the minor
    number. For example, create a branch named 2.1.1, from the 2.1
    branch.
2.  Merge bug fixes or other modifications into the new branch.
3.  In the new branch, modify README.md to include a section on
    bug fixes since the previous release, and to suggest checking out the
    source from a bug-fix branch to get the fixes listed.
4.  Link to this README in the download page of the release.
