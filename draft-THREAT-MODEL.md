<!--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->

# Apache JDO Security Threat Model (draft)

## §1 Header

- **Project**: Apache JDO — `apache/db-jdo`, under the Apache DB PMC. Reference
  implementation home for the Java Data Objects (JDO) persistence API. Also
  contains the JDO Technology Compatibility Kit (TCK), a Maven plugin that
  drives the TCK against the reference implementation or against a third-party
  Implementation Under Test, and (historically) the JDO specification document
  *(documented: `README.md`)*. The DB PMC also covers Apache Torque and JDOQL;
  those repos are explicitly out of scope of this model.
- **Version / commit**: drafted against the `main` branch, HEAD
  `60c77b6` ("JDO-847: fix spdx warnings with java8 and java11"). Most recent
  release line is `jdo-api 3.2.x`; the in-repo `parent-pom` declares
  `3.3-SNAPSHOT`. A report against released version *N* should be triaged
  against the model as it stood at *N*, not at HEAD.
- **Date**: 2026-05-30.
- **Authors**: ASF Security team draft, awaiting Apache DB PMC review.
- **Status**: draft — under maintainer review.
- **Reporting cross-reference**: vulnerabilities that fall under §8 (claimed
  properties) should be reported per the Apache Security Team disclosure
  channel (`security@apache.org`, forwarded to the DB PMC's private list);
  findings that fall under §3 (out of scope), §9 (properties not
  provided), or §11a (known non-findings) will be closed by the DB PMC
  citing this document.
- **Scope of this draft**: this model covers the `apache/db-jdo` repository
  only — i.e. the published `jdo-api` jar (in `api/`), the `jdo-exectck`
  Maven plugin (in `exectck/`), the TCK harness (in `tck/`), and the
  parent build (in `parent-pom/`). It does **not** model:
  - the JDO specification document itself (that question — whether the
    spec text should be included in scope — is open and will be addressed
    in a separate maintainer thread; the spec source has in any case been
    moved out of this repo per `specification/README.md`);
  - the DataNucleus reference implementation, even though it is the
    `jdo.tck.impl=jdori` target the TCK is wired against;
  - any third-party Implementation Under Test (IUT);
  - the embedded Apache Derby instance the TCK installs schemas into.
- **Provenance legend** —
  *(documented)* = drawn from in-repo source, docs, or javadoc, with citation;
  *(maintainer)* = stated by a DB PMC maintainer in response to this draft;
  *(inferred)* = synthesized by the producer from code structure or domain
  knowledge, awaiting PMC ratification (every *(inferred)* tag has a matching
  §14 question).
- **Draft confidence**: 65 documented / 0 maintainer / 31 inferred.

JDO is a Java persistence API. Application classes are tagged as
*persistence-capable* (via XML metadata or annotations) and a build- or
load-time *Enhancer* tool rewrites their bytecode to delegate field access
through a JDO implementation's `StateManager`. At runtime, the application
obtains a `PersistenceManagerFactory` (PMF) via `JDOHelper` — which
discovers the implementation class via `META-INF/services/javax.jdo.PersistenceManagerFactory`
and/or by reading `META-INF/jdoconfig.xml` resources — and uses
`PersistenceManager` to begin transactions, fetch objects by identity, and
run *JDOQL* queries (a query language transpiled by the implementation into
SQL or a datastore-native language). The `apache/db-jdo` repo ships the
**API jar** (`javax.jdo.*`), the **TCK** that certifies a JDO
implementation, the `exectck` Maven plugin that drives the TCK, and the
build glue. The repo does **not** ship a runtime persistence engine: that
is the job of an implementation such as DataNucleus.

## §2 Scope and intended use

### Intended use

- **In-process Java library** *(documented: `README.md`, `api/src/main/java/javax/jdo/package.html`)*.
  The `jdo-api` jar is added to a host application's classpath. Application code
  calls `javax.jdo.JDOHelper.getPersistenceManagerFactory(...)` (or looks the
  PMF up via JNDI), obtains a `PersistenceManager`, and uses the JDO interfaces
  for object-graph persistence and JDOQL queries. The PMF is loaded from
  configuration the application supplies (a `Map` of properties, a
  `Reader`/`InputStream` over `jdoconfig.xml`, or system-resource lookup).
- **Build-time bytecode enhancement** *(documented: `api/src/main/java/javax/jdo/Enhancer.java`,
  `api/src/main/java/javax/jdo/JDOEnhancer.java`)*. The Enhancer is invoked
  as `java -cp {classpath} javax.jdo.Enhancer {options} {classes/jars}` from
  a build tool (Maven, Gradle, Ant). It rewrites application `.class` files
  in place (or to a target directory) so that field access goes through the
  generated `StateManager` callbacks. The enhancement step trusts both its
  classpath and its inputs.
- **TCK execution** *(documented: `README.md`, `tck/RunRules.md`,
  `exectck/src/main/java/org/apache/jdo/exectck/RunTCK.java`)*. The
  `jdo-exectck` Maven plugin is run by JDO implementors to certify
  conformance. It installs schemas in a developer-supplied database
  (Derby by default), enhances test classes, and drives JUnit tests. It
  is **developer tooling**, not a production runtime.

### Deployment shape

The `jdo-api` jar is **an in-process Java library**, not a service. It does
not open listening sockets, does not accept network input, and does not
have an authn/authz layer of its own. The `exectck` Maven plugin and the
TCK harness are **build-time / certification tooling** and inherit the
trust model of the build environment they run in.

The threat model is therefore that of a library + a developer tool, not
that of a distributed service. The split matters: §2 carves the repo
into families that are modeled separately. *(inferred — §14 Q1)*

### Caller roles

Following §2 of the output-structure rubric:

| Role | Trust level | Notes |
| --- | --- | --- |
| **Embedding application developer** | trusted | Picks the JDO implementation jar, chooses which classes are persistence-capable, writes the XML metadata or annotations, supplies the PMF configuration (including JDBC URL, user, password). *(documented: `api/src/main/java/javax/jdo/JDOHelper.java`)* |
| **End user of the embedding application** | varies by host | JDO has **no notion** of an end user; whoever calls into the JDO surface has full authority within the configured PMF. Authentication and authorization of end users is entirely the embedder's problem. *(inferred — §14 Q1)* |
| **JDO implementation vendor** | trusted | The PMF class named in `META-INF/services/javax.jdo.PersistenceManagerFactory` is loaded by `Class.forName` and given full access to JDOImplHelper internals. *(documented: `api/src/main/java/javax/jdo/JDOHelper.java` lines 680–728)* |
| **Persistence-capable class author** | trusted | A class flagged as persistence-capable is enhanced at build time; the enhanced bytecode is part of the application's trusted code base. *(documented: `api/src/main/java/javax/jdo/spi/PersistenceCapable.java`)* |
| **TCK / build operator** | trusted | Runs `mvn` against the TCK with full filesystem and network privileges of the developer's workstation. *(documented: `tck/RunRules.md`, `README.md`)* |
| **Underlying datastore** | trusted by virtue of operator-granted credentials | A relational DB (typically Derby in TCK runs), object DB, etc. JDO holds the JDBC URL + credentials the embedder supplies. *(documented: `api/src/main/java/javax/jdo/Constants.java` property names)* |

### Component-family table

| Family | Representative entry point | Touches outside the process? | In-model? |
| --- | --- | --- | --- |
| `jdo-api` jar — JDO interfaces (`javax.jdo.*`) | `PersistenceManager`, `PersistenceManagerFactory`, `Query`, `Transaction` | no directly — control surface only | **yes** |
| `jdo-api` jar — `JDOHelper` PMF bootstrap | `JDOHelper.getPersistenceManagerFactory(props)` and the `jdoconfig.xml` reader path | **filesystem / classpath / XML parser** | **yes** |
| `jdo-api` jar — `JDOImplHelper` SPI registry | `JDOImplHelper.getInstance()`, `registerAuthorizedStateManagerClass`, `construct(className, keyString)` | reflection only | **yes** |
| `jdo-api` jar — `javax.jdo.identity.*` (`StringIdentity`, `LongIdentity`, `ObjectIdentity`, `SingleFieldIdentity`, …) | `ObjectIdentity.readExternal(in)` | **`Externalizable` deserialization** | **yes** |
| `jdo-api` jar — `javax.jdo.spi.JDOPermission` and the SecurityManager-gated entry points | `JDOImplHelper.getInstance()`, `registerAuthorizedStateManagerClass`, `closePersistenceManagerFactory` | none directly | **yes** for the API surface; **N/A on Java 17+** (see §5a) |
| `jdo-api` jar — `Enhancer` CLI | `javax.jdo.Enhancer.main(String[])` | **filesystem + jar/class file writes + dynamic classloading via `URLClassLoader`** | **yes** |
| `jdo-api` jar — `Query` interface (JDOQL string surface) | `PersistenceManager.newQuery(String filter)`, `Query.setFilter(String)`, `Query.declareParameters(String)`, `Query.declareImports(String)`, `Query.setOrdering(String)`, `Query.setExtensions(Map)`, `newQuery(Query.SQL, sqlString)` | **the JDO implementation translates this to a datastore query** — JDO API itself does not execute SQL | **yes** for the API contract; **execution is the implementation's responsibility** |
| `jdo-api` jar — JDOQL-typed query (`JDOQLTypedQuery`) | `javax.jdo.JDOQLTypedQuery` | none directly | **yes** |
| `jdo-api` jar — lifecycle listeners (`javax.jdo.listener.*`) | `*Callback` and `*LifecycleListener` interfaces invoked by the implementation | runs application code in PMF context | **yes** for the contract |
| `jdo-exectck` Maven plugin | `org.apache.jdo.exectck.*` Mojos (`InstallSchema`, `Enhance`, `RunTCK`) | **JDBC + ProcessBuilder + filesystem + sub-JVM** | **yes** but **developer-only** (see §3) |
| `tck/` — test classes, configs, jdoconfig, ORM mappings, SQL DDL, sample data | TCK execution | filesystem + Derby DB | **out of model — TCK is not shipped to end users** *(§3)* |
| `specification/` | `specification/README.md` (single line pointing elsewhere) | n/a | **out of model — the spec source is no longer here** *(§3)* |
| `parent-pom/`, root `pom.xml`, `lib/`, `jdo_checks.xml` | Maven build wiring | n/a | **out of model — build hygiene** *(§3)* |

## §3 Out of scope (explicit non-goals)

Apache JDO is **not**, and does not aim to be:

1. **A SQL or JDOQL execution engine.** The `jdo-api` jar defines interfaces
   only. Translation of a JDOQL filter string into datastore-native SQL,
   parameter escaping, prepared-statement binding, result-set hydration, and
   transaction state management are the responsibility of the **JDO
   implementation** (DataNucleus, others) that the embedder ships. A
   SQL-injection report against a JDOQL filter string is in scope only
   insofar as the API contract documents how a parameter is bound; the
   actual injection bug, if any, lives in the implementation. → For reports
   against the API contract: `VALID-HARDENING` at most; for reports against
   an implementation: `OUT-OF-MODEL: unsupported-component` (with a pointer
   to the implementation's tracker) *(inferred — §14 Q2)*.
2. **A defender against the embedding application.** The application
   chooses the JDO implementation jar (via `META-INF/services` or a
   property), supplies the JDBC URL and credentials, and chooses which of
   its own classes are persistence-capable. A malicious embedder is out of
   scope. *(inferred — §14 Q1)*. → `OUT-OF-MODEL: adversary-not-in-scope`.
3. **A defender against the datastore.** A relational DB returning crafted
   bytes, wrong column types, or maliciously-shaped result sets is treated
   as a trusted control-plane error. The DB's authn/authz is the
   embedder's responsibility. *(inferred — §14 Q3)*. → `OUT-OF-MODEL:
   trusted-input`.
4. **A defender against attacker-supplied persistence-capable classes,
   jars, or `jdoconfig.xml`.** The Enhancer is documented to mutate
   bytecode and the PMF bootstrap is documented to load classes by name
   from XML and `META-INF/services`. An attacker who can land a `.jar`,
   `.class`, `.jdo`, or `jdoconfig.xml` on the JDO classpath has already
   compromised the application. *(inferred — §14 Q4)*. → `OUT-OF-MODEL:
   adversary-not-in-scope`.
5. **A sandbox for lifecycle callbacks (`StoreCallback`, `LoadCallback`,
   `*LifecycleListener`).** Lifecycle callbacks are application code
   running inside the application's classpath; they get whatever the JVM
   gives them. JDO does not isolate them. *(documented: `api/src/main/java/javax/jdo/listener/package.html`)*. →
   `BY-DESIGN: property-disclaimed` (§9).
6. **A SecurityManager-enforced sandbox in modern JVMs.** `LegacyJava`
   states explicitly that on Java 17+ the SecurityManager wrapper returns
   `null` and `doPrivileged()` becomes a no-op *(documented:
   `api/src/main/java/javax/jdo/LegacyJava.java` lines 28–34, 177–200)*.
   The `JDOPermission` checks in `JDOImplHelper.getInstance()`,
   `registerAuthorizedStateManagerClass`, and `closePersistenceManagerFactory`
   therefore do nothing on a modern JVM. The model treats these checks as a
   pre-Java-17 historical artefact, **not** a current isolation boundary.
   Reports that depend on `JDOPermission` actually being enforced on a
   modern JVM are not valid against current JDO. *(inferred — §14 Q5)*. →
   `OUT-OF-MODEL: equivalent-harm` for "anyone in the JVM can call
   `JDOImplHelper`".
7. **An access-control layer over object identities.** `javax.jdo.identity.*`
   classes (`StringIdentity`, `LongIdentity`, `ObjectIdentity`, …) carry a
   target class name + key. They are `Externalizable` and the
   `readExternal` paths reconstruct the class name from the stream
   *(documented: `api/src/main/java/javax/jdo/identity/SingleFieldIdentity.java`
   line 199, `ObjectIdentity.java` line 206)*. JDO does **not** apply an
   `ObjectInputFilter` to the stream and does **not** restrict which
   target classes may be reconstructed. The responsibility for deciding
   which serialized bytes to feed into `readExternal`, and for installing
   a process-wide `ObjectInputFilter` if needed, sits with the embedder.
   *(inferred — §14 Q6)*. → `OUT-OF-MODEL: trusted-input`.
8. **A defender against XXE / XML-bomb attacks on a `jdoconfig.xml` the
   attacker controls.** `JDOHelper.getDefaultDocumentBuilderFactory()`
   already sets
   `http://apache.org/xml/features/disallow-doctype-decl=true`, suppresses
   validation, and sets `setExpandEntityReferences(true)` (which, with
   doctype declarations disallowed, leaves no external-entity reference
   path) *(documented: `api/src/main/java/javax/jdo/JDOHelper.java` lines
   1145–1159)*. But that hardening is bypassable: the embedder can call
   `JDOImplHelper.registerDocumentBuilderFactory(...)` with a custom
   factory that re-enables doctype declarations and external entities
   *(documented: `api/src/main/java/javax/jdo/spi/JDOImplHelper.java` lines
   610–612)*. A reported XXE that requires the embedder to have replaced
   the default factory is therefore `OUT-OF-MODEL: trusted-input`; an XXE
   against the *default* factory remains in scope as §8 P3.
   *(documented + inferred — §14 Q7)*.
9. **TCK code (`tck/`), the Maven plugin (`exectck/`), build wiring
   (`parent-pom/`, root `pom.xml`, `lib/`, `jdo_checks.xml`), and the
   spec stub (`specification/`).** These ship in the repository but are
   not part of the runtime product:
   - `tck/` is shipped to JDO implementors who certify their products.
     Hardcoded test credentials, fixture DBs, and SQL scripts under
     `tck/src/main/resources/sql/derby/` are test data, not production
     configuration. *(documented: `tck/RunRules.md`.)*
   - `exectck/` is a Maven plugin run by JDO implementors. It spawns
     child JVMs (`ProcessBuilder` at `exectck/src/main/java/org/apache/jdo/exectck/Utilities.java`
     line 109) and executes operator-supplied SQL via `Statement.execute`
     against a developer-supplied JDBC URL. Treating it as a defender
     against a hostile build environment is out of scope. *(documented:
     `exectck/src/main/java/org/apache/jdo/exectck/InstallSchema.java`
     line 293, `Utilities.java`.)*
   - `specification/README.md` is a single-line pointer; the spec source
     itself has moved to `clr-apache/jdo-specification`.
   - `lib/` and `lib/ext/` exist to hold JNDI dependencies for TCK runs.

   → `OUT-OF-MODEL: unsupported-component`. *(inferred — §14 Q8)*.
10. **DataNucleus and any other JDO implementation.** The TCK is wired
    against DataNucleus by default (`jdo.tck.impl=jdori`) but DataNucleus is
    a separate project at github.com/datanucleus. A vulnerability in
    DataNucleus's JDOQL-to-SQL translator, its enhancer, its caching, etc.
    is upstream-to-DataNucleus, not JDO. *(documented: `tck/pom.xml` and
    `README.md` "JDO TCK on an Implementation Under Test")*. → `OUT-OF-MODEL:
    unsupported-component` (with an upstream pointer).
11. **Apache Derby.** The TCK uses Derby as the default test database.
    Vulnerabilities in Derby are upstream to Apache Derby. → `OUT-OF-MODEL:
    unsupported-component`.
12. **The JDO specification document.** Whether the spec itself should be
    in scope is an open question between the DB PMC and the Security team
    (raised by Tilmann Zäschke, 2026-05-29) and will be handled in a
    separate thread; per `specification/README.md` the spec source no
    longer lives in this repo.
13. **Build, release, and supply-chain hygiene.** GitHub Actions pinning,
    release signing, dependency freshness, MSRV (Java 8 source/target),
    GPG-signed staging — important, but out of scope per the SKILL's §1.

## §4 Trust boundaries and data flow

### Where the boundary is

The `jdo-api` jar is a **library**, not a service. The trust boundary is
the **public API surface** of `javax.jdo.*`: any caller already inside the
JVM has the privileges of the embedder by definition. There is no
authentication, no authorization, and (on Java 17+) no SecurityManager.

The boundaries the `jdo-api` jar *does* touch — and therefore the
transitions where a finding can live — are:

| # | Transition | Mechanism | Trust direction |
| --- | --- | --- | --- |
| B1 | Embedder → `JDOHelper` PMF bootstrap | property map / `Reader` / `META-INF/services` lookup / `META-INF/jdoconfig.xml` parse | inputs are embedder-trusted (see §6); the parse machinery is the JDO API's responsibility |
| B2 | Embedder → `JDOHelper.getPersistenceManagerFactory(name)` → DOM parse of `jdoconfig.xml` | `DocumentBuilderFactory.newInstance()` (default) or an embedder-registered factory | the URL/InputStream the embedder supplied is data; the default DBF is hardened (see §3 item 8) |
| B3 | Application bytecode → `Enhancer` CLI | filesystem reads of `.class`/`.jdo`/`.jar`; filesystem writes of enhanced `.class`; dynamic loading via `URLClassLoader` of an enhancer impl named via `Class.forName` | all inputs are trusted developer config |
| B4 | Externalized identity bytes → `*Identity.readExternal(in)` | Java `Externalizable` deserialization; `targetClassName` reconstructed from the stream | inputs are bytes the embedder fed in; JDO does **not** add an `ObjectInputFilter` and does **not** restrict which target classes may be named (§3 item 7) |
| B5 | JDOQL filter / parameter declarations / SQL string → the implementation | `Query.setFilter(String)`, `Query.declareParameters(String)`, `Query.setOrdering(String)`, `Query.setExtensions(Map<?,?>)`, `newQuery(Query.SQL, sqlString)` | the API contract is that the *implementation* (DataNucleus etc.) parses and binds these; the API itself does not execute SQL |
| B6 | Lifecycle event → application callback | `*Callback` / `*LifecycleListener` invocation by the implementation | application code runs unsandboxed in the PMF context |
| B7 | Application config → JDBC driver (via PMF properties `javax.jdo.option.ConnectionURL` / `…UserName` / `…Password`) | property map | embedder-trusted; the password may be in cleartext in the properties (see §11 misuse) |
| B8 | `exectck` plugin → child JVM | `ProcessBuilder` in `exectck/src/main/java/org/apache/jdo/exectck/Utilities.java` line 109 | TCK-only; B8 is out of model per §3 item 9 |
| B9 | `exectck` plugin → JDBC + raw `Statement.execute(s)` | `InstallSchema.executeSQLStatement` line 293 | TCK-only; B9 is out of model per §3 item 9 |

### Reachability preconditions per family

A finding against the `jdo-api` jar is in-model only when it is reachable
from B1–B6, with the inputs as described in §6. Specifically:

- **API interfaces** (`PersistenceManager`, `Transaction`, `Query`, …):
  any finding presupposes that the *implementation* is doing what the API
  says it should. A bug in the contract description (e.g. a Javadoc
  guarantee the API itself violates by virtue of how the interface is
  declared) is in-model; a bug in a specific implementation's behaviour is
  not. *(inferred — §14 Q2)*.
- **`JDOHelper` PMF bootstrap**: a finding is in-model when it requires
  only the embedder-supplied properties / URL / `META-INF/services`
  lookup. An XML-injection report against `jdoconfig.xml` must clear the
  *default* `DocumentBuilderFactory` hardening (§3 item 8); a report
  that requires `registerDocumentBuilderFactory` to have been called with
  an unsafe factory is `OUT-OF-MODEL: trusted-input`.
- **`Enhancer`**: a finding is in-model when it can be triggered by
  malformed `.class` / `.jdo` / `.jar` input the embedder has chosen to
  feed in. A finding that requires control of the classpath (the
  `-cp` argument) is `OUT-OF-MODEL: adversary-not-in-scope` per §3
  item 4.
- **`*Identity.readExternal`**: a finding is in-model when it shows that
  `targetClassName` deserialization opens a gadget chain *introduced by
  the JDO API itself*. A gadget chain in a downstream class the embedder
  has on the classpath is `OUT-OF-MODEL: equivalent-harm` per §3 item 7
  — the embedder has already accepted that classpath.
- **JDOQL string surface**: a finding is in-model when it shows that the
  API contract *forces* an implementation to be unsafe. A specific
  implementation's parser bug is `OUT-OF-MODEL: unsupported-component`
  per §3 item 10.
- **Lifecycle callbacks**: a finding is in-model only when the listener
  invocation contract itself violates an §8 property; "the callback can
  do anything" is `BY-DESIGN: property-disclaimed` per §3 item 5.

## §5 Assumptions about the environment

- **JVM**: Java 8 minimum (`maven.compiler.source=8` /
  `…target=8` in `parent-pom/pom.xml`) *(documented)*. The library
  compiles and runs on Java 8 through Java 21+ *(inferred — §14 Q9)*.
- **SecurityManager**: where present (pre-Java-17), the JDO API gates
  certain SPI calls with `JDOPermission` (`getMetadata`,
  `manageMetadata`, `setStateManager`, `closePersistenceManagerFactory`)
  *(documented: `api/src/main/java/javax/jdo/spi/JDOPermission.java`,
  `JDOImplHelper.java` lines 187–195, 559–600, and
  `LegacyJava.java`)*. On Java 17+ the SecurityManager is null and these
  checks are no-ops *(documented: `LegacyJava.java` lines 28–34)*. The
  model takes the post-Java-17 stance as the *default* (§3 item 6).
- **Classloader**: `JDOHelper` operates against the current
  thread's context class loader by default *(documented:
  `JDOHelper.java` lines 1607–1617)*; the embedder can supply a different
  loader.
- **JNDI**: PMF lookup via JNDI is a documented mechanism
  *(documented: `JDOHelper.java`, `api/src/main/java/javax/jdo/package.html`)*.
  The JNDI provider is the embedder's responsibility.
- **XML stack**: standard JAXP via `DocumentBuilderFactory.newInstance()`.
  The default factory is hardened against doctype declarations
  *(documented: `JDOHelper.java` lines 1145–1159)*.
- **Concurrency**: a `PersistenceManager` is "associated with at most one
  thread at a time" per the implementation's contract; the API itself does
  not make a thread-safety claim across `PersistenceManager` instances
  *(documented: `api/src/main/java/javax/jdo/package.html`,
  `api/src/main/java/javax/jdo/Query.java` lines 33–36)*. *(inferred —
  §14 Q10)*.
- **Filesystem / network / process**: the `jdo-api` jar's runtime
  surface does not open sockets, does not spawn processes, and does not
  write to disk in normal operation — except via `Enhancer`, which writes
  enhanced `.class` files and reads operator-supplied `.jar` / directory
  paths.
- **What `jdo-api` does NOT do to its host** (negative claims, awaiting
  maintainer ratification — these are the highest-priority confirmation
  targets per the rubric):
  - **does not** open listening sockets *(inferred — §14 Q11)*;
  - **does not** install signal handlers *(inferred — §14 Q11)*;
  - **does not** spawn child processes (except `exectck`, which is
    developer tooling) *(inferred — §14 Q11)*;
  - **does not** set a `java.security.Policy` or modify global
    `ObjectInputFilter` settings *(inferred — §14 Q11)*;
  - **does not** persist credentials to disk on its own; whatever ends up
    in `jdoconfig.xml` or the PMF properties was put there by the
    embedder *(inferred — §14 Q11)*;
  - **does** read `META-INF/services/javax.jdo.PersistenceManagerFactory`,
    `META-INF/services/javax.jdo.JDOEnhancer`, and `META-INF/jdoconfig.xml`
    resources off the classpath *(documented)*;
  - **does** use a small number of `doPrivileged`-wrapped reflection and
    classloading calls for portability across pre-/post-Java-17 JVMs
    *(documented: `JDOHelper.java` lines 1719–1736)*;
  - **does** read system properties `user.country` and `user.language`
    for `DateFormat` initialization *(documented: `JDOImplHelper.java`
    line 858)*.

## §5a Build-time and configuration variants

The `jdo-api` jar is a single artefact; there are no compile-time feature
gates. Variation comes from runtime configuration that the embedder
supplies. The security-relevant knobs:

| Knob | Default | Effect on the model | Maintainer stance |
| --- | --- | --- | --- |
| `META-INF/services/javax.jdo.PersistenceManagerFactory` entry | none (the embedder ships one impl jar; the first entry wins) | The named class is loaded by `Class.forName` and given full JDOImplHelper access. *(documented: `JDOHelper.java` lines 680–728.)* | Supported; the embedder is trusted to put the right jar on the classpath. *(inferred — §14 Q12)* |
| `JDOImplHelper.registerDocumentBuilderFactory(factory)` | not called; `JDOHelper.getDefaultDocumentBuilderFactory()` returns a factory with `disallow-doctype-decl=true` | When the embedder calls this with a non-hardened factory, the `jdoconfig.xml` parser becomes vulnerable to XXE / XML bombs. *(documented: `JDOImplHelper.java` lines 610–622, `JDOHelper.java` lines 1145–1159.)* | **Maintainer ruling required** *(inferred — §14 Q7)*: the proposed answer is that the default factory is the supported posture, and replacing it with a non-hardened factory is an embedder choice that is `OUT-OF-MODEL: trusted-input`. |
| `JDOImplHelper.registerErrorHandler(handler)` | not called; default throws on error/fatalError and ignores warnings | Custom handler can swallow signal of malformed XML. *(documented: `JDOImplHelper.java` lines 624–640, `JDOHelper.java` lines 1170–1184.)* | Embedder choice; out of scope. *(inferred — §14 Q12)* |
| `javax.jdo.option.PersistenceManagerFactoryClass` property | unset; PMF then looked up via `META-INF/services` | Sets the exact PMF class to load. *(documented: `JDOHelper.java` line 673.)* | Embedder choice. |
| `javax.jdo.option.ConnectionURL` / `…UserName` / `…Password` / `…DriverName` | unset | JDBC connection target. Cleartext password is supported in the property map and in `jdoconfig.xml`. *(documented: `JDOHelper.java` lines 112–115, `Constants.java`.)* | Embedder choice; cleartext credentials in `jdoconfig.xml` is a documented misuse pattern (see §11). |
| `Enhancer` `-cp` flag (classpath for dynamic enhancer-implementation loading) | inherits the JVM classpath | The Enhancer runs `Class.forName` against this path and runs the impl's `JDOEnhancer.enhance()`. *(documented: `Enhancer.java` lines 41–62.)* | Embedder choice; an attacker who controls the enhancer classpath has already won (see §3 item 4). |
| Pre-Java-17 `java.security.policy` granting `JDOPermission("getMetadata")` / `"setStateManager"` / `"manageMetadata"` / `"closePersistenceManagerFactory"` to the JDO impl | depends on the JVM's policy | The `JDOImplHelper` SPI hooks fail-fast if the caller does not hold these permissions. *(documented: `JDOPermission.java`, `JDOImplHelper.java`.)* | Documented but effectively obsolete on Java 17+; the model treats the SecurityManager as not present by default (§3 item 6). |
| `JDOQLTypedQuery` vs string-based `Query` | both supported | The typed API gives the implementation type information that string JDOQL cannot. The string API is the historical surface and is still fully supported. *(documented: `Query.java`, `JDOQLTypedQuery.java`.)* | Both supported; typed queries are preferred where available, but using string JDOQL is not a misuse. *(inferred — §14 Q13)* |
| `setExtensions(Map)` on `Query` | empty map | Implementation-specific tuning knobs. The API contract does not constrain what an implementation accepts here. *(documented: `Query.java` line 544.)* | Embedder-implementation contract; out of scope for the API. |

### The insecure-default case

The default `DocumentBuilderFactory` configuration is the *hardened* one
(doctype declarations disallowed). The escape hatch
(`registerDocumentBuilderFactory`) is the unsafe path. This is the
opposite of an insecure default and the model treats it accordingly: an
XXE report against the *default* factory is `VALID` (§8 P3); an XXE
report that requires the embedder to have replaced the factory is
`OUT-OF-MODEL: trusted-input` (§3 item 8) *(documented + inferred — §14 Q7)*.

## §6 Assumptions about inputs

### Where inputs come from

A real-world JDO embedder feeds the API:

- *Trusted from the embedder:* the PMF property map (JDBC URL, credentials,
  PMF class name, persistence-unit name), the `META-INF/services/…` entries
  on the classpath, the `META-INF/jdoconfig.xml` resources on the classpath,
  and the choice of which application classes are persistence-capable.
- *Trusted from the embedder:* the classpath itself — `Enhancer`'s
  classpath, the JDO impl's classpath, the application's classpath.
- *Trusted from the embedder:* the JDOQL filter / parameter declarations /
  ordering strings, the SQL strings, and the lifecycle-callback
  implementations.
- *Trusted from the datastore:* result rows, column types, error
  responses.
- *Untrusted-but-in-model:* externalized object-identity bytes that the
  embedder has fed into `*Identity.readExternal` — see §3 item 7 and
  §11 first bullet for the embedder's responsibility, but the
  `targetClassName` reconstruction itself is in the API code.
- *Untrusted-but-in-model:* `META-INF/jdoconfig.xml` parsed by the
  *default* hardened `DocumentBuilderFactory`. Any XXE / XML-bomb against
  the *default* factory is §8 P3.

### Per-entry-point trust table

| Entry point | Parameter | Attacker-controllable in the model? | Caller must enforce |
| --- | --- | --- | --- |
| `JDOHelper.getPersistenceManagerFactory(Map props)` | property map (incl. JDBC URL, username, password) | **no** — embedder config | do not source PMF properties from end-user input; protect cleartext passwords (see §11) |
| `JDOHelper.getPersistenceManagerFactory(InputStream/Reader/URL, …)` | the XML stream | **no** — embedder-supplied stream | do not source `jdoconfig.xml` from untrusted bytes |
| `JDOHelper.getPersistenceManagerFactory(String name)` | persistence-unit name | **no** — embedder config | do not source the name from untrusted callers if `jdoconfig.xml` carries multiple PUs with different credentials |
| `JDOHelper`'s `META-INF/jdoconfig.xml` resource lookup | XML bytes on the classpath | **bytes are trusted (classpath ⇒ trusted); parser is hardened** | do not register a non-hardened `DocumentBuilderFactory` via `JDOImplHelper.registerDocumentBuilderFactory` |
| `JDOImplHelper.registerDocumentBuilderFactory(DocumentBuilderFactory)` | factory instance | **no** — embedder config | per §10, if called, pass a factory that disables doctype declarations |
| `JDOImplHelper.construct(String className, String keyString)` | `className` | **the API contract is "embedder trusts this"** but the bytes can flow from `readExternal` of an `*Identity` (B4) | per §10, install a process-wide `ObjectInputFilter` and/or do not deserialize identities from untrusted bytes |
| `SingleFieldIdentity.readExternal(ObjectInput in)` | `targetClassName` (read via `in.readObject()`) | **bytes are whatever the embedder fed into the stream** — JDO does not filter | per §10, install an `ObjectInputFilter` if any byte stream from a trust-uncertain source ever reaches `readExternal` |
| `ObjectIdentity.readExternal(ObjectInput in)` | `keyAsObject` (read via `in.readObject()`) — full Java deserialization of an arbitrary object | as above | as above — this is the highest-blast-radius identity surface |
| `Query.setFilter(String filter)` | JDOQL filter expression | **embedder chooses; if embedder concatenates end-user strings, attacker-controllable** | per §10, use parameter declarations + `setParameters(Map)` rather than string concatenation |
| `Query.declareParameters(String)` / `declareImports(String)` / `setOrdering(String)` / `setGrouping(String)` | JDOQL fragments | as above | as above |
| `PersistenceManager.newQuery(Query.SQL, String sqlString)` | raw SQL | **embedder chooses; if embedder concatenates end-user strings, attacker-controllable** | per §10, use parameter binding rather than string concatenation; treat `Query.SQL` as a raw-SQL escape hatch |
| `Query.setExtensions(Map<?,?>)` | impl-specific tuning knobs | **embedder-supplied; impl-defined effect** | per §10, do not source extension values from untrusted callers |
| `Enhancer.main(String[] argv)` | command-line args incl. file/jar paths | **no** — developer-tool input | per §10, do not run the Enhancer over untrusted `.class` / `.jar` inputs |
| `*Callback` / `*LifecycleListener` registration | listener instance | **no** — embedder code | per §10, the callback runs unsandboxed; the embedder must not register untrusted code |
| Datastore result row (via `Query.execute()` returning `List`) | bytes from the datastore | **trusted from the datastore** | the datastore's ACLs are the gate |

### Size / shape / rate

- The `jdo-api` jar makes **no claim** about the size, shape, or rate of
  inputs the embedder feeds in — there is no built-in cap on JDOQL filter
  length, parameter count, listener count, identity-stream depth, or
  `jdoconfig.xml` size *(inferred — §14 Q14)*. Resource bounds are the
  implementation's and embedder's responsibility.
- `Externalizable` deserialization of an `ObjectIdentity` chain has **no
  depth limit and no class-name filter** beyond what the JVM-wide
  `ObjectInputFilter` setting provides *(documented: `ObjectIdentity.java`,
  `SingleFieldIdentity.java`)*.

## §7 Adversary model

### Actors

| Actor | In scope? | Capabilities granted |
| --- | --- | --- |
| **End user of the embedding application** | **only insofar as the embedder forwards their input into the JDO surface** | whatever JDOQL strings, parameter values, and identity bytes the embedder hands to the API |
| Attacker who can supply JDOQL filter / parameter / ordering strings (via the embedder) | **partial** | JDO's API contract delegates parsing to the implementation; an injection bug at the implementation layer is upstream |
| Attacker who can supply bytes to `*Identity.readExternal` (via the embedder) | **yes** | a Java-deserialization gadget chain is reachable to whatever depth `ObjectInputFilter` allows — JDO itself adds no filter |
| Attacker who can supply an XML byte stream to `JDOHelper`'s default-factory parser | **yes for XXE / XML-bomb classes the hardened default does not block; no for XXE that requires doctype declarations to be allowed** | bounded by the default `DocumentBuilderFactory` posture |
| Attacker who can land a jar / class file / `.jdo` / `jdoconfig.xml` on the JDO classpath | **out of scope** — §3 item 4 | n/a |
| Embedding application developer / maintainer | **out of scope** — §3 item 2 | trusted by definition |
| Datastore (relational DB, object DB) | **out of scope** — §3 item 3 | trusted control-plane |
| JDO implementation vendor (DataNucleus, …) | **out of scope** — §3 item 10 | trusted; bugs go upstream |
| Build operator running the Enhancer or the TCK | **out of scope** — §3 item 9 | full developer-environment authority |
| Side-channel observer (cache timing, branch prediction) | **out of scope** *(inferred — §14 Q15)* | n/a |
| Local process on the same host running as a different OS user | **out of scope** — JDO defends only across the API surface, not the OS | n/a |
| Quantum adversary | **out of scope** | n/a |

### Authenticated-but-Byzantine peer

Not applicable — JDO is not a distributed protocol.

## §8 Security properties the project provides

For each property: condition, violation symptom, severity tier, provenance.

### P1 — `JDOPermission`-gated SPI operations on a SecurityManager-enabled JVM

- **Condition**: a non-null `java.lang.SecurityManager` is installed
  (effectively, pre-Java-17 JVMs only) *(documented:
  `JDOImplHelper.java` lines 187–195, 559–600, `LegacyJava.java`)*. The
  `JDOPermission` named permissions (`getMetadata`, `manageMetadata`,
  `setStateManager`, `closePersistenceManagerFactory`) gate
  `JDOImplHelper.getInstance()`,
  `registerAuthorizedStateManagerClass(es)`, and the
  PMF-close path.
- **Violation symptom**: a caller without the named permission
  successfully obtains the singleton `JDOImplHelper`, registers a
  state-manager class, or closes a `PersistenceManagerFactory`.
- **Severity**: **security-critical** when the SecurityManager is in use;
  the property is N/A on Java 17+.
- *(documented)*

### P2 — Default `DocumentBuilderFactory` rejects DOCTYPE declarations

- **Condition**: the embedder has **not** called
  `JDOImplHelper.registerDocumentBuilderFactory(…)` with a custom factory
  *(documented: `JDOHelper.java` lines 1145–1159)*. The default factory
  sets `http://apache.org/xml/features/disallow-doctype-decl=true`, is
  non-validating, sets `setNamespaceAware(true)`, and uses an error
  handler that throws on `error`/`fatalError`.
- **Violation symptom**: a crafted `jdoconfig.xml` (e.g. one containing a
  DOCTYPE plus an external entity reference, or a recursive entity
  expansion) succeeds in extracting a local file, contacting a remote
  host, or expanding to consume unbounded memory.
- **Severity**: **security-critical** (XXE / XML-bomb in a config parser).
- *(documented)*

### P3 — `JDOHelper`'s PMF bootstrap loads only classes named by the embedder

- **Condition**: PMF classes are loaded via
  `Class.forName` against names taken either from the embedder-supplied
  property `javax.jdo.option.PersistenceManagerFactoryClass` or from
  `META-INF/services/javax.jdo.PersistenceManagerFactory` resource files
  on the classpath *(documented: `JDOHelper.java` lines 662–728)*. JDO
  does not load PMF classes from unauthenticated network locations on its
  own initiative.
- **Violation symptom**: a PMF class name flowing from anywhere other
  than (a) the embedder-supplied property map, or (b) a classpath
  resource the embedder controls, causes `Class.forName` to fire.
- **Severity**: security-critical.
- *(documented)*

### P4 — `Externalizable` identity round-tripping preserves type identity

- **Condition**: identity instances written via
  `writeExternal` round-trip through `readExternal` produce an instance
  with the same `targetClassName` and key value *(documented:
  `SingleFieldIdentity.java`, `ObjectIdentity.java`, `StringIdentity.java`,
  …)*.
- **Violation symptom**: a round-trip produces an instance whose
  `targetClass`/`getTargetClassName()` differs from the source.
- **Severity**: correctness-only (`VALID-HARDENING` at most) — JDO does
  *not* claim that `readExternal` is safe to invoke on untrusted bytes
  (see §9).
- *(documented)*

### P5 — JDOQL parameter declarations + `setParameters(Map)` provide a binding surface that an implementation can use to avoid string concatenation into datastore SQL

- **Condition**: the embedder uses `declareParameters(String)` +
  `setParameters(Map<String,?>)` (or the typed-query equivalent) rather
  than concatenating end-user strings into the filter / ordering / SQL
  text *(documented: `Query.java` lines 49, 66, 230, 342)*.
- **Violation symptom**: an API change that forces parameters to be
  passed as inline text in the filter rather than as bound values.
- **Severity**: this is an *API contract* property, not a property of any
  given implementation. A specific implementation that ignores the
  binding and concatenates is `OUT-OF-MODEL: unsupported-component` per
  §3 item 1.
- *(documented)*

### P6 — The `JDOHelper` PMF bootstrap is reentrant and the singleton `JDOImplHelper` is thread-safe for class registration

- **Condition**: `JDOImplHelper.registeredClasses` is a synchronized
  `HashMap`, `authorizedStateManagerClasses` and `listeners` access is
  synchronized, and registration is the documented entry point for
  persistence-capable classes' static initialisers *(documented:
  `JDOImplHelper.java` lines 78–91)*.
- **Violation symptom**: a data race on class registration that leaves
  `registeredClasses` in a corrupt state.
- **Severity**: correctness-only.
- *(documented)*

## §9 Security properties the project does *not* provide

State each plainly so a triager can route an inbound report to the
matching disclaimer.

- **No isolation between the embedder and the JDO impl.** Any class on
  the classpath can call `JDOImplHelper`, register listeners, register
  document-builder factories, etc. The `JDOPermission` gates exist for
  pre-Java-17 SecurityManager deployments; on a modern JVM they are
  no-ops (§3 item 6, §5a, §8 P1) *(documented)*.
- **No defense against Java-deserialization gadget chains in
  `*Identity.readExternal`.** The API does not install an
  `ObjectInputFilter`, does not restrict which class names may appear in
  `targetClassName`, and does not constrain `keyAsObject`'s actual class
  in `ObjectIdentity`. A process-wide `ObjectInputFilter`, configured by
  the embedder, is the only defense (§3 item 7, §11, §11a)
  *(inferred — §14 Q6)*.
- **No JDOQL / SQL injection defense at the API layer.** The
  `Query.setFilter(String)` / `declareParameters(String)` /
  `setOrdering(String)` / `setGrouping(String)` /
  `newQuery(Query.SQL, sqlString)` surface is designed for the
  embedder to pass *trusted* strings; the binding mechanism
  (`setParameters(Map)`) is the API's recommended way to keep end-user
  bytes out of the parsed text. Whether a *specific implementation*
  honours the binding contract is outside the API jar (§3 item 1)
  *(inferred — §14 Q2)*.
- **No XXE / XML-bomb defense when the embedder has replaced the default
  `DocumentBuilderFactory`** via `registerDocumentBuilderFactory(…)`
  (§3 item 8) *(documented + inferred — §14 Q7)*.
- **No sandbox for lifecycle callbacks.** `*Callback` / `*LifecycleListener`
  implementations execute with the privileges of the PMF context
  (§3 item 5) *(documented)*.
- **No defense against persistence-capable bytecode that abuses the
  enhanced contract.** The Enhancer modifies bytecode at build time; an
  embedder who enhances hostile source code has already lost.
- **No credential protection.** Connection passwords supplied via the
  `javax.jdo.option.ConnectionPassword` property or via `jdoconfig.xml`
  are held in cleartext for the lifetime of the PMF (§11, §11a)
  *(documented: `JDOHelper.java` line 112, `Constants.java`)*.
- **No transport security**. JDO has no notion of TLS — the JDBC driver
  (or other backend driver) makes any transport-security decision.
- **No defense against concurrent abuse of a `PersistenceManager` from
  multiple threads.** The API allows a `PersistenceManager` to be
  associated with at most one thread; concurrent access is the embedder's
  problem *(documented: API contract)*.
- **No defense against side channels** (cache, timing) *(inferred —
  §14 Q15)*.
- **No defense against build-environment compromise of the Enhancer or
  the TCK** (§3 item 9).

### False-friend properties (call out separately)

- **`JDOPermission` is not an isolation boundary.** On Java 17+ it is a
  no-op (LegacyJava `IS_SECURITY_DEPRECATED` is `true` and `getSecurityManager()`
  returns `null`); on pre-Java-17 JVMs it gates only the SPI calls listed
  in `JDOPermission.java`'s docstring. It does **not** sandbox
  user-defined callbacks, JDOQL execution, or the Enhancer.
- **`Externalizable` round-trip is not a deserialization safety
  guarantee.** That `writeExternal` and `readExternal` round-trip an
  identity correctly does *not* mean it is safe to call `readExternal`
  on bytes from an untrusted source. The `readExternal` path reads a
  class name and (in `ObjectIdentity`) a full Java object from the
  stream — a classic Java-deserialization sink.
- **`META-INF/services/javax.jdo.PersistenceManagerFactory`-based PMF
  lookup is not class-name validation.** Any entry on the classpath
  whose `META-INF/services` file names a class will be loaded by
  `Class.forName` without further checks. Embedders must control their
  classpath.
- **The `Query.SQL` constant is not a SQL-injection defender.** It is a
  raw-SQL escape hatch by which the embedder hands a SQL string to a
  relational JDO implementation; whether parameter binding is used at
  all is the embedder's choice.
- **The hardened `DocumentBuilderFactory` default is bypassable.** A
  single call to `JDOImplHelper.registerDocumentBuilderFactory(…)` with
  a non-hardened factory reverts the §8 P2 guarantee. The embedder is
  trusted not to do that.
- **`Detachable`'s `jdoDetachedState` is not an integrity guarantee.**
  The detached-state array is serialized along with the detached
  instance *(documented: `Detachable.java`)*; an attacker who can mutate
  detached bytes can present a state-managed instance with arbitrary
  loaded/modified field flags. JDO does **not** sign or HMAC the
  detached state.

### Well-known attack classes the project does not defend against

- **Java deserialization gadget chains** against `*Identity.readExternal`
  (see §11a first bullet for the suppression rationale at the API
  layer).
- **JDOQL / SQL injection** by concatenating end-user strings into
  `setFilter` / `Query.SQL` strings — the bind mechanism exists but the
  API does not refuse string concatenation by the embedder.
- **XXE / XML bombs** against a non-default
  `DocumentBuilderFactory` the embedder has registered.
- **Path traversal / arbitrary file write** against the `Enhancer`'s
  `setOutputDirectory` argument (operator config, see §3 item 9).
- **Configuration-injection** against a `jdoconfig.xml` the embedder
  built from end-user input — JDO trusts the XML.

## §10 Downstream responsibilities

The embedder of the `jdo-api` jar **must**:

1. **Control the classpath.** The PMF impl class, the application's
   persistence-capable classes, the `META-INF/services/javax.jdo.*`
   entries, and the `META-INF/jdoconfig.xml` resources are all
   classpath-trusted *(documented: `JDOHelper.java`, `Enhancer.java`)*.
2. **Not source PMF property maps or `jdoconfig.xml` content from
   untrusted callers.** The property keys include cleartext credentials
   *(documented: `Constants.java`, `JDOHelper.java`)*.
3. **Not deserialize `*Identity` instances from untrusted byte streams**
   without installing a process-wide
   `java.io.ObjectInputFilter` that constrains target class names and
   serialization depth. JDO does **not** add one *(inferred — §14 Q6)*.
4. **Use `declareParameters(String) + setParameters(Map)`** (or the
   typed-query API) rather than concatenating end-user strings into the
   JDOQL filter / ordering / grouping text, or into `Query.SQL` strings.
   The binding mechanism exists exactly so the implementation can avoid
   passing end-user bytes through the SQL parser *(documented: `Query.java`)*.
5. **Not call `JDOImplHelper.registerDocumentBuilderFactory(…)` with a
   factory that allows DOCTYPE declarations or external entity
   references.** The default factory is hardened; an embedder who
   replaces it owns the consequences *(documented: `JDOHelper.java` lines
   1145–1159)*.
6. **Treat lifecycle callbacks (`*Callback`, `*LifecycleListener`) as
   application code.** They run with full PMF privileges; do not
   register listener code sourced from outside the application's
   trusted code base *(documented: `listener/package.html`)*.
7. **Not run the `Enhancer` over untrusted `.class` / `.jar` / `.jdo`
   inputs.** The Enhancer reads, parses, and writes bytecode; an
   attacker-supplied class file is an attacker-supplied bytecode rewrite
   target *(documented: `Enhancer.java`)*.
8. **Protect cleartext credentials in `jdoconfig.xml` and PMF property
   maps.** JDO does not encrypt them; whoever can read the file or the
   memory of the JVM has them *(documented: `Constants.java`)*.
9. **Choose a JDO implementation that has its own threat model.** JDO is
   an API; the engine you run it through (DataNucleus or other) has its
   own contract for JDOQL parsing, SQL escaping, transaction safety, and
   bytecode-enhancement details.
10. **Configure the JVM-wide `ObjectInputFilter`** if any path in the
    embedder accepts serialized `Externalizable` data from across a
    trust boundary. JDO's identity classes are intentionally
    `Externalizable` and intentionally trust the stream
    *(inferred — §14 Q6)*.
11. **Apply datastore-level access control.** JDO holds whatever JDBC /
    JNDI credential the embedder gave it and queries succeed within
    those credentials; the datastore's ACLs are the only enforcement
    against the datastore.
12. **Treat `Query.setExtensions(Map)` values as implementation-defined
    behaviour.** A specific extension may unlock unsafe modes
    (skipping security checks, enabling debug paths) in a given
    implementation *(documented: `Query.java`)*.

## §11 Known misuse patterns

- **Calling `*Identity.readExternal` on bytes from an untrusted source
  without an `ObjectInputFilter`.** Java-deserialization gadget chains
  via `ObjectIdentity`'s `keyAsObject` (which is a full `Object` read
  from the stream) are the highest-blast-radius misuse pattern. JDO
  does not install a filter; the JVM-wide setting is the only defense
  *(inferred — §14 Q6)*.
- **Building JDOQL filter strings by concatenation of end-user
  input.** The classic `setFilter("name == '" + userName + "'")`
  anti-pattern: JDO supports it, the implementation will accept it, and
  whether it is exploitable is entirely down to the implementation's
  parser. The bind mechanism (`declareParameters` +
  `setParameters(Map)`) exists exactly to avoid this.
- **Passing raw end-user SQL through `newQuery(Query.SQL, sqlString)`.**
  Same shape as above against the SQL surface.
- **Storing cleartext database credentials in `jdoconfig.xml` checked
  into source control.** JDO supports cleartext passwords by design;
  protecting them is the embedder's job.
- **Replacing the default `DocumentBuilderFactory` with one that allows
  DOCTYPE declarations or external entities** via
  `JDOImplHelper.registerDocumentBuilderFactory`. This reverts §8 P2.
- **Running the `Enhancer` against an attacker-supplied jar** (e.g.
  user-uploaded plugin code). The Enhancer writes new bytecode; an
  attacker who can land arbitrary classpath entries can shape what gets
  emitted.
- **Treating `JDOPermission` as a current-day isolation boundary.** It
  is a no-op on Java 17+ (§3 item 6) and even pre-17 it gated only
  the SPI calls listed in `JDOPermission.java`'s table.
- **Mutating a detached object's `jdoDetachedState` (loaded / modified
  field BitSets) before re-attaching** to bypass JDO's integrity check
  on attach. JDO does not sign the detached state *(documented:
  `Detachable.java`)*.
- **Sharing a `PersistenceManager` across threads.** The API allows
  association with at most one thread at a time; concurrent use is the
  embedder's bug *(documented)*.
- **Registering listener / callback classes loaded by an
  end-user-controlled classloader** (e.g. a "plugin" mechanism in the
  embedder). The callbacks run in the PMF context with full privileges
  *(documented: `listener/package.html`)*.

## §11a Known non-findings (recurring false positives)

This section is the highest-leverage input for automated agentic
security scans. Each entry: tool symptom, why it is safe under the
model, the § that licenses the call.

- **"`SingleFieldIdentity.readExternal` and `ObjectIdentity.readExternal`
  perform unrestricted Java deserialization."** True, by design, in the
  API contract. Filtering serialized streams is the embedder's
  responsibility (§3 item 7, §10 item 3, §10 item 10).
  → `BY-DESIGN: property-disclaimed` per §9.
- **"`JDOImplHelper.construct(className, keyString)` resolves a class
  name from a String via `Class.forName` and calls a String constructor
  reflectively."** Same as above; the `className` is part of the
  embedder-trusted identity contract. → `BY-DESIGN: property-disclaimed`
  per §9.
- **"`JDOHelper`'s PMF lookup loads classes from
  `META-INF/services/javax.jdo.PersistenceManagerFactory` without
  validating the class name."** The classpath is embedder-trusted
  (§3 item 4); the API loads exactly the class the embedder put on
  the path. → `OUT-OF-MODEL: trusted-input` per §6.
- **"The `Enhancer` writes arbitrary bytes to the operator-supplied
  output directory."** The output directory is operator-supplied
  developer configuration (§3 item 9). → `OUT-OF-MODEL:
  unsupported-component` for the Enhancer-on-untrusted-input shape.
- **"SQL injection via `Query.setFilter(String)` /
  `newQuery(Query.SQL, …)`."** The API exposes a string surface and the
  binding mechanism exists exactly so the embedder can keep
  attacker-controlled bytes out of it (§9, §10 item 4, §11
  second bullet). The actual injection bug, if any, lives in the JDO
  *implementation* (§3 item 1). → `BY-DESIGN: property-disclaimed`
  for the API layer; `OUT-OF-MODEL: unsupported-component` for
  implementation reports.
- **"Cleartext password in `jdoconfig.xml` / in the PMF property map."**
  Documented by design — `PROPERTY_CONNECTION_PASSWORD`
  (`javax.jdo.option.ConnectionPassword`) is a String property. Storage
  protection is the embedder's responsibility (§10 item 8). →
  `BY-DESIGN: property-disclaimed`.
- **"`JDOPermission` checks are no-ops at runtime."** True on Java 17+,
  by design — `LegacyJava.isSecurityManagerDeprecated()` returns `true`
  and the wrappers short-circuit (§3 item 6, §8 P1). →
  `BY-DESIGN: property-disclaimed`.
- **"Hardcoded test passwords / SQL DDL under `tck/src/main/resources/`."**
  Test fixtures (§3 item 9). → `OUT-OF-MODEL:
  unsupported-component`.
- **"`exectck`'s `ProcessBuilder` accepts caller-supplied command
  lists."** The plugin runs with the developer's authority and the
  caller is the developer's `pom.xml` (§3 item 9). →
  `OUT-OF-MODEL: unsupported-component`.
- **"`exectck`'s `InstallSchema.executeSQLStatement` runs raw SQL via
  `Statement.execute(s)`."** The plugin installs schemas on a
  developer-supplied DB from developer-supplied DDL files; the entire
  flow is developer-side TCK tooling (§3 item 9). →
  `OUT-OF-MODEL: unsupported-component`.
- **"`JDOHelper` parses XML via JAXP."** The default
  `DocumentBuilderFactory` configuration disables DOCTYPE declarations
  (§8 P2); a report that requires the embedder to have replaced the
  factory is `OUT-OF-MODEL: trusted-input` per §6.
- **"The Java 8 source/target is below current LTS."** This is a build
  / release-hygiene concern, out of scope per the SKILL.

## §12 Conditions that would change this model

Revise this document when any of the following lands:

- A new public API surface in `javax.jdo.*` (e.g. a new typed-query
  entry point, a new identity class).
- A new bootstrap mechanism beyond `META-INF/services` /
  `META-INF/jdoconfig.xml` (e.g. a new SPI lookup, a network-based
  config fetch).
- A change to the default `DocumentBuilderFactory` configuration —
  re-enabling DOCTYPE declarations, or moving the hardening to a
  configurable knob (§8 P2).
- Adoption of `java.io.ObjectInputFilter` inside `*Identity.readExternal`
  paths (would convert §3 item 7 and §9 from disclaimed to a §8
  property).
- A new `JDOPermission` constant, or a runtime-replacement for the
  pre-Java-17 SecurityManager contract.
- A change to the bytecode-enhancement contract that affects which
  callers can replace a `StateManager`.
- Promotion of `exectck/` or `tck/` content into a runtime path.
- Re-importing of the JDO specification document into this repo (the
  open question handled outside this draft).
- A vulnerability report that cannot be cleanly routed to one of the
  §13 dispositions — that is evidence the model is incomplete.

## §13 Triage dispositions

A report against `apache/db-jdo` receives exactly one of the following:

| Disposition | Meaning | Licensed by |
| --- | --- | --- |
| `VALID` | Violates a §8 property via an in-scope §7 adversary using an in-scope §6 input. | §8, §6, §7 |
| `VALID-HARDENING` | No §8 property violated, but a §11 misuse pattern can be made harder to fall into by API or default change. Fixed at maintainer discretion, typically no CVE. | §11 |
| `OUT-OF-MODEL: trusted-input` | Requires attacker control of a §6 parameter the model marks trusted (PMF properties, classpath, `jdoconfig.xml` content the embedder fed in, custom `DocumentBuilderFactory`, `Externalizable` identity bytes the embedder fed in, JDOQL/SQL strings the embedder built). | §6 |
| `OUT-OF-MODEL: adversary-not-in-scope` | Requires a §7 actor the model excludes (embedder, datastore, JDO impl vendor, build operator, side-channel observer, same-host attacker). | §7 |
| `OUT-OF-MODEL: unsupported-component` | Lands in `tck/`, `exectck/`, `parent-pom/`, `lib/`, `specification/`, `jdo_checks.xml`, or in a downstream JDO implementation. | §3 item 9, §3 item 10, §3 item 11 |
| `OUT-OF-MODEL: equivalent-harm` | An actor already-authorized under the model can cause the same harm via a documented path (any classpath entry can already call `JDOImplHelper`; the embedder can already drop any class on the path). | §3 item 6 |
| `BY-DESIGN: property-disclaimed` | Concerns a §9 property the project explicitly does not provide (Java-deserialization filtering, JDOQL/SQL injection defense at the API layer, callback sandbox, credential encryption, post-Java-17 SecurityManager). | §9 |
| `KNOWN-NON-FINDING` | Matches a §11a recurring false positive. | §11a |
| `MODEL-GAP` | Cannot be cleanly routed to any of the above — triggers §12 model revision. | §12 |

## §14 Open questions for the maintainers

Every *(inferred)* tag in the body maps to one of these. Proposed
answers are inline; please confirm, correct, or strike.

### Wave 1 — scope, intended use, library-vs-impl boundary

**Q1.** The model frames `apache/db-jdo` as "the JDO API jar + the TCK +
the exectck plugin", with the API as an in-process library, the TCK and
exectck as developer tooling, and the *implementation* (DataNucleus or
other) as a separate project with its own threat model. Confirm? Are
there embedder-facing guarantees the API makes that we have not
captured? *(maps to §2, §3)*

(maintainer) The framing is correct. There or no additional embedder-facing guarantees.

**Q2.** Proposed answer: a JDOQL parser / SQL escaping / transaction
atomicity / cache poisoning report against DataNucleus is upstream to
DataNucleus, not in scope here. A report against the *API contract*
that the API itself forces an implementation to be unsafe is in scope.
Is that the right split? *(maps to §3 item 1, §11a)*

(maintainer) Yes, that is the right split.

**Q3.** The datastore (Derby for the TCK, anything for production) is
modeled as a trusted control-plane. Confirm? *(maps to §3 item 3)*

(maintainer) Yes.

**Q4.** Anyone who can land a `.jar` / `.class` / `.jdo` /
`jdoconfig.xml` on the JDO classpath is treated as an embedder-trusted
actor, because the classpath is part of the embedder's
trusted-code-base contract. Confirm? *(maps to §3 item 4)*

(maintainer) Yes.

**Q5.** Pre-Java-17 the `JDOPermission` SPI gates were a real
SecurityManager boundary; Java 17+ deprecated `SecurityManager` and
`LegacyJava` makes the wrappers no-ops. The model takes the post-Java-17
stance as default. Confirm? *(maps to §3 item 6, §5a, §8 P1, §9
false-friend, §11a)*

(maintainer) Correct.

**Q6.** `SingleFieldIdentity.readExternal` and `ObjectIdentity.readExternal`
read a class name and (for `ObjectIdentity`) a full `Object` from the
stream without any `ObjectInputFilter`. Proposed answer: this is `BY-DESIGN`
— the identity classes are `Externalizable` by spec, the JDO API does
not install a filter, and the embedder is documented to install one if
it ever feeds untrusted bytes to `readExternal`. Confirm, or should the
API ship a default filter (which would change §3 item 7, §9, §10
item 3, §11a, §11)? *(maps to §3 item 7, §6, §9, §10 item 3,
§11, §11a)*

(maintainer) Confirmed. TODO?

**Q7.** The default `DocumentBuilderFactory` disables DOCTYPE
declarations *(documented at `JDOHelper.java` line 1148)*. A report that
requires the embedder to have called
`JDOImplHelper.registerDocumentBuilderFactory(…)` with a non-hardened
factory is `OUT-OF-MODEL: trusted-input`. A report against the *default*
factory remains `VALID` (§8 P2). Confirm, and confirm there are no
other JAXP entry points (e.g. in `JDOHelper.readJDOConfigFromURL`) that
predate or bypass the hardened default? *(maps to §3 item 8, §5a, §8 P2,
§9, §11)*

(maintainer) Confirmed. TODO?

### Wave 2 — XML, classpath, callbacks

**Q8.** Proposed unsupported-component list: `tck/`, `exectck/`, `parent-pom/`,
`lib/`, `specification/`, `jdo_checks.xml`, root `pom.xml`. Anything to
add or remove? *(maps to §3 item 9, §13)*

(maintainer) Correct.

**Q9.** MSRV: the parent-pom declares Java 8 source/target. Is the
runtime target the same (i.e. is the released jar usable on Java 8
JREs), or is the project actively dropping Java 8 in 3.3? The model's
§5 assumption is "Java 8+". *(maps to §5)*

(maintainer) The assumption is correct.

**Q10.** `PersistenceManager` thread-association: the API package
javadoc says a PM is associated with at most one thread at a time, but
no formal guarantee. Confirm that the API itself makes *no*
thread-safety claim across PM instances — and that concurrent abuse of
a single PM is implementation behaviour. *(maps to §5)*

(maintainer) A PM may be associated with multiple threads if 
PersistenceManager.setMultithreaded(...) is set to "true".
Concurrent use of a single PM is implementation behavior. The JDO API
library should support multi-threaded use and provide minimum safeguard 
against wrong usage.

**Q11.** Confirm the "what `jdo-api` does NOT do to its host" inventory
in §5: no listening sockets; no signal handlers; no spawned processes
(except `exectck`); no `java.security.Policy` mutation; no global
`ObjectInputFilter` mutation; no credential persistence. Any
exceptions, particularly under unusual JNDI providers or unusual
classloaders? *(maps to §5)*

(maintainer) The assumptions are correct.

**Q12.** Are the two "embedder-supplied factory" hooks
(`registerDocumentBuilderFactory`, `registerErrorHandler`) the *only*
places where the embedder can replace a JDO-default with a
custom-and-possibly-unsafe implementation, or are there other
similar SPIs we should enumerate under §5a (e.g. for I18N, classloader
strategy)? *(maps to §5a)*

(maintainer) These are the only two hooks.

### Wave 3 — JDOQL surface, identity, lifecycle

**Q13.** Both `Query` (string-based JDOQL) and `JDOQLTypedQuery` are
supported. The model treats them as equally first-class for §8 P5
(parameter-binding contract). Is the typed API actually preferred from
a security stance? Should §11 carry a "prefer the typed API where
possible" misuse-pattern entry, or is that premature for a
spec-level document? *(maps to §5a, §8 P5, §11)*

(maintainer) This is premature, both should be treated equals.

**Q14.** §6 size/shape/rate: the API has no input caps and the
implementation is responsible for resource bounds. Confirm? *(maps to
§6, §9)*

(maintainer) Confirmed.

**Q15.** Side-channel observers (cache timing, branch prediction) and
local non-`embedder` users are out of scope (proposed). *(maps to §7, §9)*

(maintainer) Correct.

**Q16.** The `Detachable.jdoDetachedState` array — proposed answer is
that JDO does *not* sign or HMAC it, and an attacker who can mutate
detached bytes can present arbitrary loaded/modified bitsets. Is that
the correct framing, or does the spec actually require an integrity
check at re-attach time we should describe as §8? *(maps to §9
false-friend, §11)*

(maintainer) The framing is correct. No integrity check is required.

### Wave 4 — meta

**Q17.** Should this document live at `docs/threat-model.md` (proposed,
new directory) or as a sibling of `README.md` at the repo root? *(meta)*

(maintainer) It should live at `docs/threat-model.md`.

**Q18.** Is there an existing JDO threat-model artefact we should
reconcile against rather than supersede (Confluence, internal wiki,
JDO-* JIRA closure history)? *(meta — §3.1a of the rubric)*

(maintainer) No.

**Q19.** Should this draft also pick up the JDO specification document
once Tilmann Zäschke's separate question about including the spec
returns? If yes, what is the scope split between "API jar threat model"
and "spec threat model"? *(meta — open per the briefing)*

(maintainer) If possible, the scan agent should use the JDO specification document at 
https://github.com/clr-apache/jdo-specification/tree/main/src .
If that cannot be accessed, 
https://github.com/clr-apache/jdo-specification/blob/main/releases/JDO-3.2.1.pdf should be used.
If that is also not available, neither should be used.

**Q20.** §11a is currently populated from API-shape reasoning only.
Can the PMC contribute 3–5 patterns from inbound JDO-* JIRA history
("we ruled this out as not a vulnerability")? §11a is the highest-leverage
suppression input for an automated scan agent and is currently the
section a real triage history would most strengthen. *(meta — §11a)*

(maintainer) Nothing yet.

**Q21.** No website cross-check was possible during this draft (the
producer environment denied `https://db.apache.org/jdo/` and
`https://security.apache.org/projects/`). Does the Apache DB project
publish a security policy on its website that this model should
reconcile against — and is there a `SECURITY.md` we missed elsewhere in
the org (e.g. in a sibling DB repo)? *(meta — §3.1a)*

(maintainer) The only additional security information is available
in the specification mentioned in the answer to **Q19.** .

**Q22.** What kind of change to JDO should trigger a model revision
(proposed list in §12 — confirm or correct)? *(meta — §12)*

(maintainer) Confirmed.

---

## Appendix: SECURITY.md → §x back-map

Apache JDO does not ship a `SECURITY.md` in the repo and the website
cross-check could not be completed (see §14 Q21). The de-facto
security-policy statements are scattered across in-repo source and
javadoc; the back-map below covers what the producer was able to find.

| Source | Claim | Lands in |
| --- | --- | --- |
| `api/src/main/java/javax/jdo/spi/JDOPermission.java` lines 36–66 | enumerates four named permissions (`setStateManager`, `getMetadata`, `manageMetadata`, `closePersistenceManagerFactory`) and their risks | §8 P1, §9 false-friend, §11 |
| `api/src/main/java/javax/jdo/spi/JDOImplHelper.java` lines 62–66 | "There is no security restriction on this access. JDO implementations get access to the functions provided by this class only if they are authorized by the security manager." | §8 P1, §9 first bullet |
| `api/src/main/java/javax/jdo/spi/JDOImplHelper.java` lines 187–195 | `JDOImplHelper.getInstance()` checks `JDOPermission.GET_METADATA` | §8 P1 |
| `api/src/main/java/javax/jdo/spi/JDOImplHelper.java` lines 559–600, 663–681 | `JDOPermission.SET_STATE_MANAGER` gates `registerAuthorizedStateManagerClass(es)` and `checkAuthorizedStateManager` | §8 P1 |
| `api/src/main/java/javax/jdo/LegacyJava.java` lines 28–34 | "For pre-17 this class provides access to the expected SecurityManager and AccessController. For 17+ the getSecurityManager() will always return 'null' and the doPrivileged() methods will simply execute the lambda argument without any security checks." | §3 item 6, §5, §8 P1, §9 false-friend |
| `api/src/main/java/javax/jdo/JDOHelper.java` lines 1145–1159 | default `DocumentBuilderFactory` sets `http://apache.org/xml/features/disallow-doctype-decl=true`, `setNamespaceAware(true)`, `setValidating(false)` | §5a, §8 P2 |
| `api/src/main/java/javax/jdo/spi/JDOImplHelper.java` lines 610–622 | embedder may replace the default `DocumentBuilderFactory` via `registerDocumentBuilderFactory` | §3 item 8, §5a, §9 false-friend, §10 item 5 |
| `api/src/main/java/javax/jdo/JDOHelper.java` lines 662–728 | PMF lookup via `META-INF/services/javax.jdo.PersistenceManagerFactory` and the `javax.jdo.option.PersistenceManagerFactoryClass` property | §8 P3 |
| `api/src/main/java/javax/jdo/spi/JDOImplHelper.java` lines 827–854 | `construct(className, keyString)` does `Class.forName(className)` + reflective String-constructor call | §6 trust table, §9, §11a second bullet |
| `api/src/main/java/javax/jdo/identity/SingleFieldIdentity.java` line 199 + `ObjectIdentity.java` line 206 | `readExternal` reads `targetClassName` and (for `ObjectIdentity`) a full `Object` from the stream | §3 item 7, §6 trust table, §9, §10 item 3, §11 first bullet, §11a first bullet |
| `api/src/main/java/javax/jdo/spi/Detachable.java` lines 26–36 | `jdoDetachedState` is serialized along with the detached instance; "only the BitSet of modified fields will be modified" while detached | §9 false-friend, §11, §14 Q16 |
| `api/src/main/java/javax/jdo/listener/package.html` | lifecycle callbacks are invoked on application-defined classes during life-cycle events | §3 item 5, §9, §10 item 6 |
| `api/src/main/java/javax/jdo/Query.java` lines 49, 66, 230, 342 | `declareParameters(String)` + `setParameters(Map)` is the binding mechanism; `Query.SQL` is the raw-SQL escape hatch | §6 trust table, §8 P5, §9, §10 item 4, §11 |
| `api/src/main/java/javax/jdo/Enhancer.java` lines 41–62 | Enhancer CLI: classpath, recurse, output directory, in-place rewrite | §3 item 4, §6 trust table, §10 item 7, §11 |
| `exectck/src/main/java/org/apache/jdo/exectck/Utilities.java` line 109, `InstallSchema.java` line 293 | `exectck` plugin spawns child JVMs and runs raw SQL on the developer-supplied DB | §3 item 9, §11a |
| `README.md` | repo subprojects: `api/`, `exectck/`, `tck/`, `parent-pom/`, `specification/` | §2 component table, §3 item 9 |
| `specification/README.md` | "The specification is no longer being maintained in this repository. The specification has been moved to https://github.com/clr-apache/jdo-specification." | §3 (spec out of scope), §14 Q19 |
| `parent-pom/pom.xml` lines 169–170 | `maven.compiler.source=8`, `maven.compiler.target=8` | §5 (JVM assumption), §14 Q9 |
