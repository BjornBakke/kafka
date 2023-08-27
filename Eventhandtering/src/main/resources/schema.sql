create table dokumentevent
(
    id    bigint           NOT NULL AUTO_INCREMENT,
    dokumentid  varchar(255)  not null,
    navn  varchar(255)  not null,
    sideantall int not null,
    PRIMARY KEY (id),
    UNIQUE (dokumentid)
);