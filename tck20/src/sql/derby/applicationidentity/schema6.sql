-- SchemaType: application identity pkg

connect 'jdbc:derby:jdotckdb;create=true' user 'tckuser' password 'tckuser';

CREATE SCHEMA applicationidentity_pkg;
SET SCHEMA applicationidentity_pkg;

-------------------------
-- mylib
-------------------------

DROP TABLE PCRect;
DROP TABLE PCPoint;

CREATE TABLE PCPoint (
    ID BIGINT NOT NULL,
    X INTEGER NOT NULL,
    Y INTEGER,
    CONSTRAINT PCPNT_CONST PRIMARY KEY (ID)
);

CREATE TABLE PCRect (
    ID BIGINT NOT NULL,
    UPPER_LEFT BIGINT REFERENCES PCPoint NOT NULL,
    LOWER_RIGHT BIGINT REFERENCES PCPoint NOT NULL,
    CONSTRAINT PCRCT_CONST PRIMARY KEY (ID)
);
