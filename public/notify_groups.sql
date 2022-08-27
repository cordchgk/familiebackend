create function notify_Messages() returns trigger
    language plpgsql
as
$$
begin
    perform pg_notify('mid',new.mid::text);
    return new;
end;
$$;

alter function notify_Messages() owner to postgres;





create trigger notify_messages
    after update or insert
    on messages
    for each row
execute procedure notify_Messages();



