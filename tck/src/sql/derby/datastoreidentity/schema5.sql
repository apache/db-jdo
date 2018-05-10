-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- SchemaType: datastore identity orm

-- connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA datastoreidentity_orm;
SET SCHEMA datastoreidentity_orm;

-------------------------
-- mylib
-------------------------

DROP TABLE PCPoint;

CREATE TABLE PCPoint (
    DATASTORE_IDENTITY BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    ID BIGINT,
    X INTEGER NOT NULL,
    Y INTEGER,
    CONSTRAINT PCPNT_CONST PRIMARY KEY (DATASTORE_IDENTITY)
);

