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

alter table users
    owner to postgres;

create trigger table_change
    after insert or update or delete
    on users
    for each row
execute procedure notify_change();

alter table users
    disable trigger table_change;

create trigger users
    after insert or update or delete
    on users
    for each row
execute procedure notify_change();

alter table users
    disable trigger users;

create trigger after_insert_item
    after insert
    on users
    for each row
execute procedure notify_update_user();

alter table users
    disable trigger after_insert_item;

create trigger after_update_user
    after update
    on users
    for each row
execute procedure notify_update_user();

alter table users
    disable trigger after_update_user;

create trigger user_changed
    after insert or update
    on users
    for each row
execute procedure notify_user_changes();

alter table users
    disable trigger user_changed;

create trigger user_changedd
    after insert or update
    on users
    for each row
execute procedure notify_user_changes();

create trigger user_change
    after insert or update or delete
    on users
    for each row
execute procedure notify_change();

create trigger notify
    after update
    on users
    for each row
execute procedure notify();

