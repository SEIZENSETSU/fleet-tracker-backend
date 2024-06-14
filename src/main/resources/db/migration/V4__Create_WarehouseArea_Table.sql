-- Create WarehouseArea table
CREATE TABLE "WarehouseArea"
(
    warehouse_area_id        SERIAL PRIMARY KEY,
    local_area_id            INT REFERENCES "LocalArea" (local_area_id),
    warehouse_area_name      VARCHAR,
    warehouse_area_latitude  NUMERIC,
    warehouse_area_longitude NUMERIC,
    warehouse_area_radius    NUMERIC
);
