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

A distribution of JDO is built from a branch of svn. It is copied into a
release directory, from which it is staged and tested prior to release.
Once released, it is propagated to mirror servers around the world.

The process is performed by a release manager with cooperation from
testers in the community.

<span id="procoverview"></span>

Overview of the process
-----------------------

The community first decides on the name of the release. The format of
the name is *spec-number*.*major*.*minor*. A trailing *minor* number
with a zero value is right trimmed, so there might be a 2.0.1 but not a
2.0.0.

Interim releases prior to final release are identified by a suffix on
the release number. Common suffixes include: -alpha, -beta, -beta2,
-rc1, -rc2. Generally, the suffixes are part of the release plan, and
the contents of each suffix release are agreed by the community. There
might be significant changes in functionality between the suffix
releases. Each suffix release goes through the process documented here.

The release manager makes a branch from the trunk (for a major release)
or from another branch (for a minor release) to create a branch with the
source of the distribution.

The release manager follows the Apache process detailed below to build
and deploy a release.

The release manager calls for the community to test the release.

The community tests the release. If necessary, cycle until all issues
are resolved.

The release manager closes the staged repository

The release manager calls for a vote to release by sending a message to
the community and forwarding the message to the pmc.

The community votes on the release. If necessary, cycle until issues are
resolved.

The release manager notifies the pmc of the successful vote outcome.
Note that a successful vote includes three +1 votes from PMC members.

The release manager notifies the worldwide community of the availability
of the release.

The release manager updates the JDO web sites
(http://db.apache.org/jdo/index.html, http://java.sun.com/jdo/).

If bugs are found or test challenges are sustained after the release is
approved and distributed, the release manager creates a new branch to
address the bugs found.

<span id="procdetail"></span>

Detailed process steps
----------------------

1.  You might want to run [Apache Rat](http://creadur.apache.org/rat) to
    check the sources for any lisence issues.

        mvn apache-rat:check

2.  Create a branch from the trunk and increment the spec or major
    number. For example, create a 3.1 branch from the trunk.

        cd jdo
        svn copy https://svn.apache.org/repos/asf/db/jdo/trunk \
        https://svn.apache.org/repos/asf/db/jdo/branches/3.1

3.  In trunk, update version numbers in the following files in
    preparation for the next release:
    trunk/README.html  
    File names and version references in the Overview section

    trunk/tck/RunRules.html  
    Update version number and date

    Use the maven version plug-in to update version numbers in the
    pom.xml files at the root and subproject levels.

4.  If needed, update the dependency to the RI, DataNucleus, in the tck
    pom.xml.

5.  If needed, apply patches from the trunk or branches to the new
    branch.

6.  Update version numbers where necessary in projects to be released,
    if these changes haven't been made previously. Check the following
    files:
    branches/*version*/README.html  
    File names and version references in the Overview section (for a
    major release only.)

    branches/*version*/tck/RunRules.html  
    Update version number

7.  Check the scm settings in the pom.xml files in the new branch and
    make sure they refer the new branch (instead of the trunk).

8.  Follow the instructions at [Publishing Maven
    Artifacts](http://www.apache.org/dev/publishing-maven-artifacts.html)
    to set up your development environment.

9.  Copy the JNDI implementation jars (providerutil.jar and
    fscontext.jar) to the branch lib/ext directory. This is needed to
    test the tck before distributing it.**  
    Do not check these in to SVN**

10. Build the distribution with the following command:

             mvn clean install -Papache-release
            

    This creates the .jar and .pom files in the target directory of each
    subproject. Be prepared to enter your key passcode when prompted.
    This happens multiple times.

11. Run [Apache Rat](http://creadur.apache.org/rat) on the release.

12. Do a dry run prepare and deployment of a snapshot release. You might
    want to do this in a fresh workspace, since you cannot have local
    modifications when preparing a release. The files in lib/ext and
    lib/jdori count as local modifications. Be prepared to enter your
    key passcode when prompted. This happens multiple times.

         mvn release:prepare -Papache-release -DautoVersionSubmodules=true -DdryRun=true -Dresume=false

         mvn deploy -Papache-release 
            

    Check the artifacts at [the Maven release
    repository](https://repository.apache.org/content/repositories/snapshots/)

13. Prepare and release the artifacts. There are interoperability issues
    with the maven release plugin and cygwin, so if on Windows, use a
    Windows command window for this step and the following one.

        mvn release:clean -Papache-release
        mvn release:prepare -Papache-release
              

    Note: If you're located in Europe then release:prepare may fail with
    'Unable to tag SCM' and ' svn: No such revision X '. Wait 10 seconds
    and run mvn release:prepare again.

14. Stage the release for a vote.

        mvn release:perform -Papache-release
            

15. Go to [the Nexus
    repository](https://repository.apache.org/index.html), login with
    your apache account, click on Staging Repositories in the menu on
    the left and close the staged repository. Press the refresh button
    to see the new status 'closed'. See [11.4.1. Closing an Open
    Repository](http://books.sonatype.com/nexus-book/reference/staging-repositories.html)
    for details.

16. Send an announcement to test the release to the
    jdo-dev@db.apache.org alias. If problems are found, fix and repeat.

17. Send an announcement to vote on the release to the
    jdo-dev@db.apache.org alias. The message subject line contains
    \[VOTE\]. Forward the \[VOTE\] message to private@db.apache.org.
    Iterate until you get a successful vote. Mail the results of the
    vote to jdo-dev@db.apache.org, cc: general@db.apache.org, and
    include \[VOTE\] \[RESULTS\] in the subject line.

18. After testing and voting is complete, follow the instructions at
    [11.4.3. Releasing a Staging
    Repository](http://books.sonatype.com/nexus-book/reference/staging-repositories.html)
    to release the artifacts.

19. Update the distribution repository at http://apache.org/dist/db/jdo/
    by adding the new release directory to the svnpubsub directory.
    Check out the repository at
    https://dist.apache.org/repos/dist/release/db/jdo and add the new
    release with all artifacts under the new directory. Make sure that
    the key used to sign the artifacts is included in the KEYS file.
    Committing the new directory will trigger an update to the mirrors.

20. If the release is a bug fix release to a maintenance release, update
    README.txt in the parent branch, adding the following line: "This
    release has been deprecated. Please use version 2.x.y.", with a link
    to the svn web interface for that version.

21. After updating the site (below), announce the release to the Apache
    community via email to announce@apache.org This must be sent from an
    @apache.org email address. \*\*\* Be aware that by sending to this
    address you will be bombarded with piles of emails from people with
    "I'm out of the Office" as if you really cared \*\*\*

<span id="site"></span>

Site updates
------------

1.  Update the Apache JDO web site to point the downloads page to the
    release.
    1.  In site/src/site/xdoc/releases create release-*N.n*.xml. Edit
        the release numbers and the link to the release notes. You will
        need to change the '&'s in the URL to "&amp;"

    2.  In site/src/site/resources/releases create release-*N.n*.cgi.
        The .cgi file contents are identical to the other .cgi files in
        the release directory; only the file name differs.

    3.  Edit site/src/site/xdoc/downloads.xml to link to the new release
        page .cgi document.

    4.  Build and test as described in the site/HOWTO document. Note
        that the cgi page will not be active until it is on the server,
        so can't really be tested.

    5.  Add the new files to the subversion repository.

            svn add site/src/site/xdoc/releases/release-N.n.xml 
            svn add site/docs/releases/release-N.n.html 
            svn add site/src/site/resources/releases/release-N.n.cgi 
            svn add site/docs/releases/release-N.n.cgi 
                

    6.  Set the svn properties svn:eol-style to native and
        svn:executable to true for the .cgi files.
2.  Change the link to RunRules on the
    [TCK](http://db.apache.org/jdo/tck.html) page to link to the
    RunRules.html file of the latest release.
3.  Update the news list on the site home page to announce the new
    release.
4.  Update the specification page to link to the new specification pdf
    document. If the release has been approved by the JCP, link to the
    documentation page of the JCP web site. If the release has not been
    approved by the JCP, link to the .pdf in the JDO source repository.
5.  Add the javadoc for the release to the site.
    1.  Make a new directory under site/docs for the release, e.g.
        api2.1. We'll call it *docsdir*.
    2.  Download the javadoc artifact from the repository and copy it to
        *docsdir*.
    3.  Unzip it in *docsdir*.
    4.  Do svn add on *docsdir*.
    5.  Edit xdocs/javadoc.xml and add links to the new javadoc.
6.  Build and test. Follow the instructions in site/HOWTO to push the
    site changes to the Apache web site.

<span id="postrelease"></span>

Post release modifications and documentation
--------------------------------------------

Follow this procedure if a significant bug is found or if the TCK must
be modified because a test challenge is found to be valid.

1.  Create a new branch from the release branch, incrementing the minor
    number. For example, create a branch named 2.1.1, from the 2.1
    branch.
2.  Merge bug fixes or other modifications into the new branch.
3.  In the new branch, modify trunk/README.txt to include a section on
    bug fixes since the 2.1 release, and to suggest checking out the
    source from a bug-fix branch to get the fixes listed.
4.  Link to this README in the web interface to svn from the .cgi
    download page and from http://db.apache.org/jdo/tck.html.
