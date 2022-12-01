-- Licensed to the Apache Software Foundation (ASF) under one or more
-- contributor license agreements.  See the NOTICE file distributed with
-- this work for additional information regarding copyright ownership.
-- The ASF licenses this file to You under the Apache License, Version 2.0
-- (the "License"); you may not use this file except in compliance with
-- the License.  You may obtain a copy of the License at
--
--     https://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

-- SchemaType: application identity

-- connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity12;
SET SCHEMA applicationidentity12;

DROP TABLE KITCHEN;

CREATE TABLE KITCHEN (
    KITCHEN_ID BIGINT NOT NULL,
    OVEN_DISCRIM VARCHAR(128) NOT NULL,
    OVEN_MAKE VARCHAR(255),
    OVEN_MODEL VARCHAR(255),
    MICROWAVE CHAR(1) CHECK (MICROWAVE IN ('Y','N') OR MICROWAVE IS NULL),
    CAPABILITIES VARCHAR(255),

    CONSTRAINT KITCHEN_PK PRIMARY KEY (KITCHEN_ID)
);
