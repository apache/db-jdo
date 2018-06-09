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

-- SchemaType: datastore identity

-- connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA datastoreidentity12;
SET SCHEMA datastoreidentity12;

DROP SEQUENCE DATASTORE_SEQ;
DROP TABLE KITCHEN;

CREATE SEQUENCE DATASTORE_SEQ AS BIGINT START WITH 1;

CREATE TABLE KITCHEN (
    DATASTORE_ID BIGINT NOT NULL,
    KITCHEN_ID BIGINT NOT NULL,
    OVEN_DISCRIM VARCHAR(128) NOT NULL,
    OVEN_MAKE VARCHAR(255),
    OVEN_MODEL VARCHAR(255),
    MICROWAVE CHAR(1) CHECK (MICROWAVE IN ('Y','N') OR MICROWAVE IS NULL),
    CAPABILITIES VARCHAR(255),

    CONSTRAINT KITCHEN_PK PRIMARY KEY (DATASTORE_ID),
    CONSTRAINT KITCHEN_UK UNIQUE      (KITCHEN_ID)
);
