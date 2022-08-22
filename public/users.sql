create table users
(
    userid           serial
        primary key,
    email            varchar(50) not null
        unique,
    password         char(96)    not null,
    firstname        varchar(30) not null,
    surname          varchar(30) not null,
    address          varchar(100),
    verificationhash varchar(100),
    accountstatus    boolean,
    sessioncookie    varchar,
    apitoken         varchar,
    birthday         date
);


create trigger notify
    after update
    on users
    for each row
execute procedure notify();

