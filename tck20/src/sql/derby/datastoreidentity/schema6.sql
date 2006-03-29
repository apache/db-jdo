-- SchemaType: datastore identity pkg

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA datastoreidentity_pkg;
SET SCHEMA datastoreidentity_pkg;

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

