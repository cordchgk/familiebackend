create function notify_update_user() returns trigger
    language plpgsql
as
$$
declare
begin
    if (tg_op = 'UPDATE') then

        perform pg_notify('user_changed',
                          json_build_object(
                                  'id', NEW.userid

                              )::text);
    end if;

    return null;
end
$$;

alter function notify_update_user() owner to postgres;

