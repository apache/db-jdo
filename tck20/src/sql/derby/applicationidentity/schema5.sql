-- SchemaType: application identity orm

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity_orm;
SET SCHEMA applicationidentity_orm;

-------------------------
-- mylib
-------------------------

DROP TABLE PCPoint;

CREATE TABLE PCPoint (
    ID BIGINT NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER,
    CONSTRAINT PCPNT_CONST PRIMARY KEY (ID)
);

