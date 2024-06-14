-- Create Warehouse table
CREATE TABLE "Warehouse"
(
    warehouse_id        SERIAL PRIMARY KEY,
    warehouse_area_id   INT REFERENCES "WarehouseArea" (warehouse_area_id),
    warehouse_name      VARCHAR,
    warehouse_latitude  NUMERIC,
    warehouse_longitude NUMERIC
);
