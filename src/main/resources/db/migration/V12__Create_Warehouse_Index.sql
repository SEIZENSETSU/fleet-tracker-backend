-- LocalAreaテーブルのインデックス
CREATE INDEX idx_local_area_id ON "LocalArea"(local_area_id);

-- WarehouseAreaテーブルのインデックス
CREATE INDEX idx_warehouse_area_id ON "WarehouseArea"(warehouse_area_id);
CREATE INDEX idx_warehouse_area_local_area_id ON "WarehouseArea"(local_area_id);

-- Warehouseテーブルのインデックス
CREATE INDEX idx_warehouse_id ON "Warehouse"(warehouse_area_id);