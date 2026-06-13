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

# Apache JDO — Threat Model

## §1 Header

- **Project:** Apache JDO (`apache/db-jdo`) — the `jdo-api` jar, the JDO TCK,
  the `exectck` Maven plugin, and the JDO specification document.
- **Written against:** `main` @ HEAD (2026-06), JDO API 3.2-line.
- **Author:** ASF Security team, drafted via the threat-model-producer rubric
  (Michael Scovetta rubric) at the Apache JDO PMC's request (path 3).
- **Status:** APPROVED by three PMC members who are maintainers
- **Version binding:** This model is versioned with the project. A report
  against JDO API version *N* is triaged against the model as it stood at *N*,
  not at HEAD.
- **Reporting cross-reference:** Findings that violate a §8 claimed property
  should be reported privately per [`SECURITY.md`](SECURITY.md). Findings that
  fall under §3 (out of scope) or §9 (disclaimed properties) will be closed
  citing this document.
- **Provenance legend:** *(documented)* = stated in project source/docs (cited);
  *(maintainer)* = stated by a JDO maintainer in the threat-model review;
  *(inferred)* = reasoned from code/structure, not yet confirmed — each has a
  matching §14 open question.
- **Draft confidence:** ~34 documented / 1 maintainer / 14 inferred.

**What Apache JDO is.** Java Data Objects (JDO) is a Java **specification** for
transparent persistence of plain Java objects to datastores. This repository
publishes the *API* (`jdo-api-<version>.jar`) — the set of interfaces, helper
classes, exception types, identity classes, and annotations that applications
compile against and that persistence *implementations* (e.g. DataNucleus)
implement — plus a Technology Compatibility Kit (TCK) used to certify those
implementations, a Maven plugin to run the TCK, and the specification document.
The published artifact is an **API-definition library**: it defines a contract
and bootstraps an implementation; it does not itself execute queries, open
datastore connections, manage credentials, or persist data. Those are the
implementation's job and are out of this model (§3).

## §2 Scope and intended use

Primary intended use: an application declares persistence-capable classes and,
at runtime, calls `JDOHelper.getPersistenceManagerFactory(...)` to obtain a
`PersistenceManagerFactory` from a JDO *implementation* selected by
configuration, then uses the `PersistenceManager` / `Query` / `Transaction`
interfaces to persist and retrieve objects. The `jdo-api` jar is loaded
**in-process** by the application and the implementation; it is not a daemon,
service, or network endpoint. *(documented — `README.md`, `api/` Javadoc.)*

Caller trust level: the calling application and the JDO implementation are
**in-process, trusted** code. JDO configuration (the implementation class name,
datastore URL, driver, credentials — §6) is **operator-supplied** and trusted,
exactly as for any JDBC/JPA bootstrap. *(inferred — Q1.)*

**Component families.**

| Family | Representative entry point | Touches outside the process? | In this model? |
| --- | --- | --- | --- |
| `api` — bootstrap | `JDOHelper.getPersistenceManagerFactory`, `getEnhancer` | Reads classpath resources / a config file path; reflectively loads the impl class; parses `jdoconfig.xml` | **Yes** |
| `api` — contract surface | `PersistenceManager`, `Query`, `Transaction`, `FetchPlan`, `Extent` (interfaces) | No (interfaces; behavior is the impl's) | Interface only — see §3 |
| `api` — helpers / identity / exceptions | `JDOImplHelper`, `*Identity`, `JDO*Exception`, `Constants` | Reflection over registered classes; `Serializable` | **Yes** |
| `api` — legacy permission model | `JDOPermission` (SecurityManager) | No | **Yes** (largely inert on modern Java — §5) |
| `tck` | JUnit test suite + `RunRules` | Filesystem, spawns an RI/IUT, datastore | No — §3 (test artifact, run by implementers) |
| `exectck` | Maven plugin | Build-time only | No — §3 (build tool) |
| `specification` | OpenDocument spec | No | No — reference material *(maintainer — Tilmann Zäschke, 2026-05-29)* |

## §3 Out of scope (explicit non-goals)

- **The JDO implementation.** Query (JDOQL/SQL) parsing and execution,
  connection pooling, credential handling, datastore I/O, caching, and the
  L2 cache all live in the *implementation* (DataNucleus and others), not in
  `jdo-api`. **JDOQL/SQL injection, connection-string abuse, datastore-side
  resource exhaustion, and ORM-mapping flaws are the implementation's threat
  model, not this one.** A report against query execution or connection
  handling is `OUT-OF-MODEL: unsupported-component` here and should be routed
  to the implementation. *(documented — the `api/` `Query`/`PersistenceManager`
  types are interfaces with no execution logic.)*
- **The TCK (`tck/`) and `exectck/`.** The TCK is a certification test suite
  run by implementers; it ships a `security.policy` / `security.conf` purely
  to exercise the JDO SecurityManager permission checks under test. It is not a
  downstream-consumed artifact. The `exectck` Maven plugin is a build-time tool.
  Both are out of model; threat-model separately if needed. *(documented —
  `tck/src/main/resources/conf/security.policy`, `README.md`.)*
- **The specification document.** Useful as reference material for this model;
  not an executable artifact and not scan input. *(maintainer — Tilmann
  Zäschke, 2026-05-29.)*
- **The bytecode enhancer.** `jdo-api` only *locates and invokes* an enhancer
  (`JDOHelper.getEnhancer()` reflectively loads a vendor `JDOEnhancer`); the
  enhancer implementation itself is out of scope. *(documented —
  `JDOHelper.java` `getEnhancer`.)*

## §4 Trust boundaries and data flow

The trust boundary is the **process**: `jdo-api` runs inside the application's
JVM alongside trusted application code and the trusted implementation. There is
no network or cross-process boundary inside `jdo-api` itself. *(inferred — Q1.)*

The one data flow that crosses an *configuration* boundary is bootstrap:

```
operator config (jdoconfig.xml / *.properties / Map / System props)
      │  trusted, operator-supplied
      ▼
JDOHelper.getPersistenceManagerFactory(...)
      │  reads resource via context ClassLoader (doPrivileged)
      │  parses jdoconfig.xml (DOCTYPE disallowed — §8)
      │  Class.forName(implClassName).getMethod("getPersistenceManagerFactory").invoke(...)
      ▼
implementation PMF  ──►  (datastore — out of model, §3)
```

**Reachability preconditions per component (the triager's first test):**

- A finding in the **bootstrap** family is in-model only if it is reachable
  from configuration that the model treats as *attacker-controlled* — which,
  per §6, it is **not** in the default posture. A finding that requires the
  attacker to already control `jdoconfig.xml` / the JDO properties / the
  classpath is `OUT-OF-MODEL: trusted-input` (§13).
- A finding in the **contract surface** (the `Query`/`PersistenceManager`
  interfaces) is in-model only if it is in `jdo-api` code, not in the
  implementation behind the interface. Execution-side findings are §3.
- A finding in **identity/exception** classes is in-model only if reachable
  without the embedding application first deserializing attacker-controlled
  bytes (Java deserialization of these `Serializable` types from an untrusted
  source is a downstream responsibility, §10/§9).

## §5 Assumptions about the environment

- **Runtime:** A conformant JVM, Java 8+ (`LegacyJava` shims reflectively
  bridge `AccessController`/`doPrivileged` across versions where it has been
  deprecated/removed). *(documented — `api/.../LegacyJava.java`.)*
- **SecurityManager:** `JDOPermission` and the `doPrivileged` wrappers target
  the Java SecurityManager. The SecurityManager is deprecated for removal
  (JEP 411) and disabled by default on modern JDKs, so these checks are
  **effectively inert** in current deployments and present mainly for
  backward compatibility. *(inferred — Q5.)*
- **Class loading:** Bootstrap resolves the implementation, enhancer, and
  registered persistence-capable classes via the **thread context
  ClassLoader** (obtained in a `doPrivileged` block). The application is
  assumed to control its own classpath. *(documented — `JDOHelper.java`
  `getContextClassLoader`, `forName`.)*
- **Concurrency:** Thread-safety of `PersistenceManager`/`Transaction` is
  defined by the JDO spec and provided by the implementation, not the API
  jar. `JDOImplHelper` registration is the API's own shared mutable state.
  *(inferred — Q2.)*
- **What `jdo-api` does NOT do to its host:** it opens no sockets, spawns no
  processes, installs no signal handlers, and (apart from reading the
  configured JDO config resource/file and registered class metadata) performs
  no datastore or network I/O of its own. It reads `System` properties for JDO
  configuration keys and reads named classpath resources. *(inferred — Q3,
  high-priority negative-claim confirmation.)*

## §5a Build-time and configuration variants

`jdo-api` has no compile-time feature flags that change its security envelope.
The runtime configuration that matters is the **JDO bootstrap configuration**
itself (§6): the implementation class, datastore connection properties, and the
config resource/file names. There is no "insecure default" knob in the API jar
— the security-relevant defaults (e.g. XML DOCTYPE handling, §8) are hardcoded
to the safe value. *(documented — `JDOHelper.getDefaultDocumentBuilderFactory`.)*

## §6 Assumptions about inputs

`jdo-api` accepts two kinds of input: **bootstrap configuration** (trusted,
operator-supplied) and **application data passed through the contract API**
(handled by the implementation, not the API jar). Per-parameter trust:

| Entry point | Parameter | Attacker-controllable? | Caller/operator must enforce |
| --- | --- | --- | --- |
| `getPersistenceManagerFactory(String name, ...)` | `name` — resource/PMF name | **no** — trusted config | keep config + classpath operator-controlled |
| `getPersistenceManagerFactory(Map props, ...)` | `javax.jdo.PersistenceManagerFactoryClass` | **no** — trusted config | never source the impl class name from untrusted input |
| `getPersistenceManagerFactory(...)` | `javax.jdo.option.ConnectionURL` / `ConnectionDriverName` | **no** — trusted config | treat as a credential/endpoint; never from untrusted input |
| `getPersistenceManagerFactory(...)` | `ConnectionUserName` / `ConnectionPassword` | **no** — trusted secret | supply via a secret manager; not attacker-reachable |
| `getPersistenceManagerFactory(InputStream)` | `jdoconfig.xml` bytes | **no** — trusted classpath resource | keep the config resource out of attacker write paths |
| `getEnhancer()` | enhancer vendor class (config) | **no** — trusted config | trusted classpath |
| `PersistenceManager` / `Query` methods | query strings, parameters, persisted objects | **handled by the implementation** | see §3 / the implementation's model |
| `*Identity(Class, String)` constructors | identity key string | **maybe** (if app builds IDs from untrusted input) | validate before constructing IDs from external data |

The JDO connection credentials (`ConnectionUserName`/`ConnectionPassword`) are
defined as plain JDO property names *(documented — `Constants.java` lines
745–784)*; the API passes them through to the implementation and applies no
encryption or masking of its own (§9).

## §7 Adversary model

Because `jdo-api` is an in-process, trusted library, it has **no untrusted
network or input adversary of its own** in the default posture. The actors:

- **In-process application code** — trusted. A malicious in-process caller has
  already won (it can read memory, call any API, replace the classpath) and is
  not a meaningful adversary at this layer. *(inferred — Q1.)*
- **Whoever controls the JDO configuration / classpath** — trusted by
  assumption (operator). A threat that requires control of `jdoconfig.xml`, the
  JDO properties, or the classpath is out of model (§3/§13). If a deployment
  lets an *untrusted* party influence JDO config, that is a deployment flaw, not
  a `jdo-api` flaw. *(inferred — Q1.)*
- **A party supplying bytes the application later deserializes into JDO
  identity / detached objects** — only relevant if the embedding application
  deserializes untrusted data (a downstream decision, §10). *(inferred — Q4.)*

Explicitly **not** in scope: side-channel / co-tenant adversaries; attackers
with control of the calling process; attackers targeting the datastore or the
implementation's query/connection layer (§3).

## §8 Security properties the project provides

1. **XML config parsing rejects DOCTYPE / external entities (XXE-safe).**
   `getDefaultDocumentBuilderFactory()` sets
   `http://apache.org/xml/features/disallow-doctype-decl = true`, which blocks
   DTDs entirely (and therefore external-entity and entity-expansion attacks)
   when parsing `jdoconfig.xml`. *Violation symptom:* external entity resolved
   / SSRF / file read during config parse. *Severity:* security-critical if
   ever broken. *(documented — `JDOHelper.java` `getDefaultDocumentBuilderFactory`.)*
2. **Bootstrap reflection is confined to configured names via the context
   ClassLoader.** `Class.forName` / `getMethod` / `invoke` are used only to
   load the *configured* implementation/enhancer class and call its documented
   static factory method, each inside a `doPrivileged` block. *Violation
   symptom:* loading/invoking a class not named by trusted config. *Severity:*
   security-critical. *(documented — `JDOHelper.java` `invoke...Implementation`,
   `forName`, `getMethod`.)*
3. **API/ABI contract fidelity** — the jar defines the JDO interfaces,
   exception hierarchy, and identity semantics as specified; the TCK certifies
   implementations against them. *Violation symptom:* spec-noncompliant
   behavior. *Severity:* correctness-only (ordinary bug), not security.
   *(documented — TCK `RunRules`.)*
4. **No ambient host side effects** — the API jar performs no socket, process,
   or signal-handler activity of its own (§5). *Violation symptom:* unexpected
   I/O from the API jar. *Severity:* security-relevant. *(inferred — Q3.)*

## §9 Security properties the project does *not* provide

- **JDO is not a security/authorization boundary.** `PersistenceManager`,
  `Query`, and `Transaction` are persistence interfaces, not an access-control
  layer; the API does not authenticate callers or authorize data access. *(inferred — Q1.)*
- **No input sanitization of queries or parameters.** The API jar does not
  parse or sanitize JDOQL/SQL; **injection safety is entirely the
  implementation's responsibility.** Building queries by string-concatenating
  untrusted input is unsafe regardless of JDO. *(documented — `Query` is an
  interface; §3.)*
- **No protection of connection credentials.** `ConnectionUserName` /
  `ConnectionPassword` are passed through as plain configuration properties;
  the API does not encrypt, mask, or vault them. *(documented — `Constants.java`.)*
- **No safe-deserialization guarantee for JDO objects.** Identity classes,
  detached instances, and `JDO*Exception` types are `Serializable`. The API
  does not defend against Java deserialization of attacker-controlled bytes —
  deserializing untrusted data into these types is a classic gadget risk and
  is the embedding application's responsibility (§10). *(documented — `implements
  Serializable` across `api/.../identity/` and the exception classes.)*
  - *False friend:* a JDO object identity is **not** a capability or an
    authorization token — possessing or forging an identity string does not by
    itself grant access; access control is the application's job.
- **No resource guarantee over implementation behavior.** Memory/CPU bounds on
  query execution, fetch, and caching are the implementation's, not the API's.
- **Well-known attack classes left to the integrator:** JDOQL/SQL injection
  (implementation + caller), Java deserialization gadgets (caller), and
  untrusted-config / classpath-injection (operator) — none are defended by
  `jdo-api`.

## §10 Downstream responsibilities

- Keep JDO configuration (`jdoconfig.xml`, `*.properties`, the implementation
  class name, connection properties) **operator-controlled**; never let an
  untrusted party set them.
- Supply `ConnectionPassword` and other secrets from a secret manager; do not
  commit them to the config resource in the clear.
- Treat JDOQL/SQL the way you treat SQL: use parameters, never concatenate
  untrusted input. (Enforced by the implementation, but the *caller* chooses
  whether to misuse it.)
- Do **not** deserialize JDO identity / detached objects from untrusted sources
  without an allow-list / safe-deserialization filter.
- Apply authorization in the application or datastore layer — JDO is not it.

## §11 Known misuse patterns

- **Treating JDO as an authorization layer** — relying on "the object isn't
  returned" for access control instead of enforcing authz explicitly. Unsafe
  because queries/fetch are the implementation's and not access-scoped by JDO.
- **Building queries from untrusted strings** — JDOQL string concatenation with
  user input, expecting JDO to sanitize it. It does not.
- **Deserializing untrusted bytes into JDO identities / detached objects** —
  exposes the application to Java deserialization gadget chains.
- **Sourcing the implementation class or connection URL from request data** —
  turns trusted-config bootstrap into arbitrary class loading / SSRF.

## §11a Known non-findings (recurring false positives)

- **`Class.forName` / `Method.invoke` in `JDOHelper`** — flagged as "unsafe
  reflection / arbitrary class load". Non-finding: the loaded name comes from
  trusted JDO configuration (§6), not from untrusted input; reachability
  precondition (§4) is not met. `KNOWN-NON-FINDING`.
- **`DocumentBuilderFactory` usage in `JDOHelper`** — flagged as "XXE". Non-
  finding: `disallow-doctype-decl=true` is set (§8); DTDs are rejected.
  `KNOWN-NON-FINDING`.
- **`Serializable` classes without hardening / `readObject`** — flagged as
  "deserialization of untrusted data". Non-finding within this model:
  deserializing untrusted bytes is a downstream decision (§9/§10); the API
  does not itself read untrusted serialized input.
- **`ConnectionPassword` as a String property** — flagged as "hardcoded/cleartext
  credential". Non-finding: it is a configuration *property name*, not a stored
  secret; credential storage is the operator's (§10).
- **`AccessController` / SecurityManager usage** — flagged as "deprecated
  API". Non-finding: present for backward compatibility (§5); not a
  vulnerability.
- **Findings in `tck/` or `exectck/`** — out of scope per §3.
  `OUT-OF-MODEL: unsupported-component`.

## §12 Conditions that would change this model

- `jdo-api` gaining a network surface, parsing a new external format, or
  reading untrusted input directly (today it does neither).
- The bootstrap reading configuration from a source the model would have to
  treat as untrusted.
- A change to the XML parser hardening (§8) or to the reflection confinement.
- Promotion of `tck`/`exectck` into a downstream-consumed artifact.
- **A report that cannot be routed to one §13 disposition** — that is evidence
  of a model gap; revise §8/§9 rather than making an ad-hoc call.

## §13 Triage dispositions

| Disposition | Meaning | Licensed by |
| --- | --- | --- |
| `VALID` | Violates a claimed property via an in-scope adversary/input. | §8, §6, §7 |
| `VALID-HARDENING` | No §8 property violated, but a §11 misuse is easy enough to warrant hardening. | §11 |
| `OUT-OF-MODEL: trusted-input` | Requires attacker control of trusted config / classpath. | §6 |
| `OUT-OF-MODEL: adversary-not-in-scope` | Requires an excluded attacker capability. | §7 |
| `OUT-OF-MODEL: unsupported-component` | Lands in the implementation, `tck/`, or `exectck/`. | §3 |
| `BY-DESIGN: property-disclaimed` | Concerns a property §9 says is not provided (injection, credential protection, safe deserialization, authz). | §9 |
| `KNOWN-NON-FINDING` | Matches a §11a recurring false positive. | §11a |
| `MODEL-GAP` | Cannot be routed to any of the above. | triggers §12 |

## §14 Open questions for the maintainers

**Wave 1 — scope & trust (these anchor everything).**

- **Q1.** We model `jdo-api` as an *in-process, trusted* library: the
  application, the implementation, and the JDO configuration (impl class,
  connection properties, `jdoconfig.xml`, classpath) are all trusted, and
  `jdo-api` has no untrusted-input adversary of its own. Confirm? (Lands in
  §2/§4/§7.) *(backs the §2/§4/§7 inferred tags.)*
  - (**maintainer**) Confirmed.
- **Q2.** Is any of `jdo-api`'s own state thread-safety a property you claim
  (e.g. `JDOImplHelper` class/metadata registration under concurrent
  classloading), or is all thread-safety delegated to the implementation per
  the spec? (Lands in §5/§8.)
  - (**maintainer**) Confirmed; all thread-safety is delegated to the implementation.
- **Q3.** Confirm the negative-side-effects inventory: `jdo-api` opens no
  sockets, spawns no processes, installs no signal handlers, and does no I/O
  beyond reading the configured JDO config resource/file and `System`
  properties. (Lands in §5/§8.)
  - (**maintainer**) Confirmed.

**Wave 2 — deserialization & legacy.**

- **Q4.** Do you want to state explicitly that deserialization of JDO
  identity / detached / exception objects from untrusted sources is a
  downstream responsibility (we have it in §9/§10), or does the PMC consider
  any serialized-form hardening to be in `jdo-api`'s remit? (Lands in §9/§10.)
  - (**maintainer**) Confirmed; there is no safe-deserialization guarantee for JDO objects.
- **Q5.** The `JDOPermission` / SecurityManager / `doPrivileged` machinery is
  effectively inert on JEP 411 JDKs. Do you still claim any SecurityManager-based
  property for older deployments, or should §5/§9 state plainly that no
  SecurityManager-enforced guarantee is made going forward? (Lands in §5/§9.)
  - (**maintainer**) Confirmed; SecurityManager usage is legacy backward compatibility only.

**Wave 3 — coexistence & spec.**

- **Q6.** There is no in-repo `SECURITY.md` today (only the TCK's test
  `security.policy`). We are adding one (disclosure pointer) alongside this
  `THREAT_MODEL.md`. Confirm the disclosure channel (the JDO PMC private list /
  the ASF security process) so the `SECURITY.md` points to the right place.
  (Lands in §1.)
  - (**maintainer**) Confirmed.
- **Q7.** The JDO specification documents intended behavior. Should any
  spec-level security statement (e.g. around identity, detachment, or the
  permission model) be lifted into §8/§9 as a claimed/disclaimed property, or
  is the spec purely reference material as Tilmann indicated? (Lands in §8/§9.)
  - (**maintainer**) Confirmed; the specification is purely for reference.

## §15 Appendix — existing-policy back-map

There is no pre-existing `SECURITY.md` or website security page for Apache JDO
to supersede; the only security-policy artifact in the repo is the TCK's test
`security.policy` / `security.conf`, which configure a SecurityManager for the
*test run* and are not a downstream security policy. This `THREAT_MODEL.md` and
the new `SECURITY.md` (disclosure pointer) are therefore the project's first
canonical security documents. If the PMC later publishes a website security
page, reconcile it here.
