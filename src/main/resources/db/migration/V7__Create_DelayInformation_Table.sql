-- Create DelayInformation table
CREATE TABLE "DelayInformation"
(
    delay_information_id SERIAL PRIMARY KEY,
    warehouse_id         INT REFERENCES "Warehouse" (warehouse_id),
    delay_state          WarehouseDelayState,
    created_at           TIMESTAMP,
    updated_at           TIMESTAMP
);
