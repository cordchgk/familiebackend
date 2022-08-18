create table items
(
    itemid    serial
        primary key,
    seller_id smallint,
    name      varchar
);

alter table items
    owner to postgres;

