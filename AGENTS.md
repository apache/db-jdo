<!-- SPDX-License-Identifier: Apache-2.0 -->
# AGENTS.md

## Security

You are helping a security researcher find and report vulnerabilities in
Apache JDO. Before drafting any report or reaching any conclusion, complete
these reading steps — skipping them leads to duplicate and out-of-scope
reports.

### Step 1 — Read the threat model

Read **[THREAT_MODEL.md](THREAT_MODEL.md)** first: the trust boundaries, the
in-scope / out-of-scope declarations, the properties the project does and does
not provide, and the known non-findings.

### Step 2 — Read the security policy

Read **[SECURITY.md](SECURITY.md)** for how (and where) to report.

### Key scoping facts (see THREAT_MODEL.md)

- `jdo-api` defines the JDO API and **bootstraps an implementation**; it does
  not execute queries or manage datastore connections. Those belong to the JDO
  implementation (DataNucleus and others) and are **out of scope** (§3) — route
  such findings there.
- Bootstrap reflection (`Class.forName` / `Method.invoke`) and `jdoconfig.xml`
  parsing operate on **trusted, operator-supplied configuration** (§6); a finding
  that requires attacker-controlled config or classpath is out of model.
- The `jdoconfig.xml` parser disallows DOCTYPE declarations and is therefore
  XXE-safe (§8).
- JDO is **not** an authorization boundary; JDOQL/SQL injection, credential
  storage, and safe deserialization of JDO objects are downstream
  responsibilities (§9 / §10).
- The TCK (`tck/`) and `exectck/` are test / build artifacts, out of scope (§3).

### Then assess

Route the finding to exactly one disposition in **THREAT_MODEL.md §13**, citing
the section that licenses the call. If it cannot be routed cleanly, it is a
`MODEL-GAP` (§12) — surface it rather than forcing a disposition.
