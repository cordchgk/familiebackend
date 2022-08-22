create function notify_Groups() returns trigger
    language plpgsql
as
$$
begin
    perform pg_notify('gid',new.gid::text);
    return new;
end;
$$;

alter function notify_Groups() owner to postgres;





create trigger notify
    after update
    on gruppe
    for each row
execute procedure notify_Groups();



