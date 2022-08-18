create function notify_change() returns trigger
    language plpgsql
as
$$
BEGIN
    PERFORM pg_notify('test', TG_TABLE_NAME);
    RETURN NEW;
END;
$$;

alter function notify_change() owner to postgres;

