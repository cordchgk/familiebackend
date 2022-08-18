create function notify_user_changes() returns trigger
    language plpgsql
as
$$
BEGIN
    PERFORM pg_notify(
            'user_changedd',
            json_build_object(
                    'id', TG_OP

                )::text
        );

    RETURN NEW;
END;
$$;

alter function notify_user_changes() owner to postgres;

