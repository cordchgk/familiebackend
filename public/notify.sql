create function notify() returns trigger
    language plpgsql
as
$$
begin
    perform pg_notify('userid',new.userid::text);
    return new;
end;
$$;

alter function notify() owner to postgres;

