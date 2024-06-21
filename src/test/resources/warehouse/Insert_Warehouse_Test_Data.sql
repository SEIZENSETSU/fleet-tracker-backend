-- Insert data into WarehouseArea
INSERT INTO "WarehouseArea" (local_area_id, warehouse_area_name, warehouse_area_latitude, warehouse_area_longitude,
                             warehouse_area_radius)
VALUES (1, '北海道エリア', 43.0642, 141.3469, 20),
       (2, '宮城県エリア', 38.2682, 140.8694, 15),
       (4, '長野県エリア', 36.6513, 138.1810, 15),
       (5, '大阪府エリア', 34.6937, 135.5023, 15),
       (6, '広島県エリア', 34.3853, 132.4553, 15),
       (7, '香川県エリア', 34.0657, 134.5594, 15),
       (8, '福岡県エリア', 33.5904, 130.4017, 15),
       (9, '沖縄県エリア', 26.2124, 127.6792, 15),
       (3, '神奈川県エリア', 35.362925, 139.6578, 21.5),
       (3, '千葉県エリア', 35.7153, 139.9726, 20.2),
       (3, '東京都エリア', 35.609275, 139.7589, 10.9),
       (3, '埼玉県エリア', 35.90375, 139.674525, 10.4);

-- Insert data into Warehouse
-- 北海道エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (1, '北海道倉庫1', 43.0722, 141.3550),
       (1, '北海道倉庫2', 43.0562, 141.3380);

-- 宮城県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (2, '宮城県倉庫1', 38.2752, 140.8775),
       (2, '宮城県倉庫2', 38.2612, 140.8613);

-- 長野県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (3, '長野県倉庫1', 36.6587, 138.1905),
       (3, '長野県倉庫2', 36.6439, 138.1715);

-- 大阪府エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (4, '大阪府倉庫1', 34.7028, 135.5101),
       (4, '大阪府倉庫2', 34.6846, 135.4945);

-- 広島県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (5, '広島県倉庫1', 34.3911, 132.4607),
       (5, '広島県倉庫2', 34.3796, 132.4500);

-- 香川県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (6, '香川県倉庫1', 34.0723, 134.5638),
       (6, '香川県倉庫2', 34.0591, 134.5550);

-- 福岡県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (7, '福岡県倉庫1', 33.5967, 130.4079),
       (7, '福岡県倉庫2', 33.5840, 130.3960);

-- 沖縄県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (8, '沖縄県倉庫1', 26.2180, 127.6830),
       (8, '沖縄県倉庫2', 26.2070, 127.6750);

-- 神奈川県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (9, 'エルフーズ横須賀工場', 35.2780, 139.6746),
       (9, 'セントラルロジ横須賀物流センター', 35.2800, 139.6766),
       (9, '横浜水産神奈川物流センター', 35.4437, 139.6380),
       (9, '東京冷蔵神奈川物流センター', 35.4500, 139.6400);

-- 千葉県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (10, 'エルフーズ千葉工場', 35.6073, 140.1063),
       (10, 'エルフーズ船橋工場', 35.6940, 139.9824),
       (10, 'セントラルロジ松戸物流センター', 35.7799, 139.9017),
       (10, 'エルフーズ松戸工場', 35.7800, 139.9000);

-- 東京都エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (11, '横浜水産平和島物流センター', 35.5821, 139.7528),
       (11, '全農城南島営業所', 35.5900, 139.7628),
       (11, '東京冷蔵城南島物流センター', 35.5850, 139.7600),
       (11, 'エルフーズ東京工場', 35.6800, 139.7600);

-- 埼玉県エリアの倉庫
INSERT INTO "Warehouse" (warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude)
VALUES (12, '東京冷蔵浦和物流センター', 35.8582, 139.6455),
       (12, '東京冷蔵岩槻物流センター', 35.9483, 139.7076),
       (12, '東北物流岩槻物流センター', 35.9485, 139.7050),
       (12, '博多食品浦和物流センター', 35.8600, 139.6400);
