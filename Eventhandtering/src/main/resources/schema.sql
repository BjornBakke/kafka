CREATE TABLE dokumentevent
(
    id         SERIAL PRIMARY KEY,
    dokumentid BIGINT       NOT NULL,
    navn       VARCHAR(255) NOT NULL,
    sideantall INT          NOT NULL,
    CONSTRAINT unique_dokumentid UNIQUE (dokumentid)
);