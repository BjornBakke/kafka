create table dokumentevent
(
    id    int           NOT NULL AUTO_INCREMENT,
    dokumentid  varchar(255)  not null,
    navn  varchar(255)  not null,
    sideantall numeric(5, 2) not null,
    PRIMARY KEY (id),
    UNIQUE (dokumentid)
);