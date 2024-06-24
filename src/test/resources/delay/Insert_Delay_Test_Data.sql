INSERT INTO "DelayInformation" (warehouse_id, delay_state, created_at, updated_at)
values
    (1, 'normal', now(), now()),
    (1, 'normal', now(), now()),
    (1, 'normal', now() - '25 HOURS'::interval, now() - '25 HOURS'::interval),
    (1, 'pause', now(), now()),
    (1, 'impossible', now(), now());

-- 25 HOURSのデータは参照されないことを想定している
