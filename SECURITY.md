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

# Security Policy

## Reporting a Vulnerability

Apache JDO follows the Apache Software Foundation security process. Report
suspected vulnerabilities **privately** to the ASF Security Team at
[security@apache.org](mailto:security@apache.org) (the JDO PMC is reachable via
`private@db.apache.org`). Please do **not** open public GitHub issues or pull
requests for security reports. See <https://www.apache.org/security/> for the
foundation-wide process and disclosure policy.

## Threat Model

Before reporting — and before triaging a tool, fuzzer, or AI-generated finding
— read the project threat model: **[THREAT_MODEL.md](THREAT_MODEL.md)**. It
describes the trust boundaries Apache JDO assumes, what it does and does not
treat as a security issue, what is in and out of scope, and the recurring
non-findings. In particular:

- The published `jdo-api` jar is an **API-definition library**. Query (JDOQL/SQL)
  execution, connection handling, credential management, and persistence are the
  responsibility of the JDO **implementation** (e.g. DataNucleus), **not** this
  project — see THREAT_MODEL.md §3.
- JDO is **not** an authorization boundary. Injection safety, credential storage,
  and safe deserialization of JDO objects are downstream responsibilities — see
  THREAT_MODEL.md §9 and §10.
- Bootstrap configuration (the implementation class, `jdoconfig.xml`, connection
  properties, classpath) is **trusted, operator-supplied** input — see §6.

Findings that fall outside the model (§3 / §9 / §11a) will be closed citing the
relevant section.
