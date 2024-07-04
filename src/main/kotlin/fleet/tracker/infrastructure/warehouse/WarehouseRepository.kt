package fleet.tracker.infrastructure.warehouse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fleet.tracker.model.LocalArea
import fleet.tracker.model.SearchSourceWarehouse
import fleet.tracker.model.Warehouse
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface WarehouseRepository {
    fun getAllWarehouses(): List<LocalArea>
    fun getByWarehouseIdOrNull(warehouseId: Int): Warehouse?
    fun existsById(warehouseId: Int): Boolean
    fun existsByIds(warehouseIds: List<Int>): Boolean
    fun getByWarehouseAreaId(warehouseAreaId: Int): List<Warehouse?>
    fun getSearchSourceWarehousesByWarehouseAreaId(warehouseAreaId: Int): List<SearchSourceWarehouse>
    fun getSearchSourceWarehousesByWarehouseIds(warehouseIds: List<Int>): List<SearchSourceWarehouse>
}

@Repository
class WarehouseRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): WarehouseRepository {
    override fun existsById(warehouseId: Int): Boolean {
        val sql = """
            SELECT EXISTS( SELECT * FROM "Warehouse" WHERE warehouse_id = :warehouse_id);
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouse_id", warehouseId)

        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Boolean::class.java) ?: false
    }

    override fun existsByIds(warehouseIds: List<Int>): Boolean {
        val sql = """
            SELECT COUNT(*) FROM "Warehouse" WHERE warehouse_id IN (:warehouseIds);
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseIds", warehouseIds)

        return (namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Int::class.java) ?: 0) > 0
    }

    override fun getByWarehouseIdOrNull(warehouseId: Int): Warehouse? {
        val sql = """
            SELECT * from "Warehouse" WHERE warehouse_id = :warehouseId
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseId", warehouseId)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Warehouse(
                warehouseId = rs.getInt("warehouse_id"),
                warehouseAreaId = rs.getInt("warehouse_area_id"),
                warehouseName = rs.getString("warehouse_name"),
                warehouseLatitude = rs.getDouble("warehouse_latitude"),
                warehouseLongitude = rs.getDouble("warehouse_longitude")
            )
        }.firstOrNull()
    }

    override fun getByWarehouseAreaId(warehouseAreaId: Int): List<Warehouse?> {
        val sql = """
            SELECT * from "Warehouse" WHERE warehouse_area_id = :warehouseAreaId
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseAreaId", warehouseAreaId)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Warehouse(
                warehouseId = rs.getInt("warehouse_id"),
                warehouseAreaId = rs.getInt("warehouse_area_id"),
                warehouseName = rs.getString("warehouse_name"),
                warehouseLatitude = rs.getDouble("warehouse_latitude"),
                warehouseLongitude = rs.getDouble("warehouse_longitude")
            )
        }
    }

    override fun getSearchSourceWarehousesByWarehouseAreaId(warehouseAreaId: Int): List<SearchSourceWarehouse> {
        val sql = """
            WITH warehouse_group AS (
                SELECT
                    w.warehouse_id,
                    w.warehouse_area_id,
                    w.warehouse_name,
                    w.warehouse_latitude,
                    w.warehouse_longitude,
                    d.delay_state,
                    COUNT(d.delay_state) AS answer_count
                FROM
                    "Warehouse" w
                LEFT JOIN
                    "DelayInformation" d ON w.warehouse_id = d.warehouse_id
                    AND d.created_at >= NOW() - INTERVAL '12 HOURS'
                WHERE
                    w.warehouse_area_id = :warehouseAreaId
                GROUP BY
                    w.warehouse_id, w.warehouse_name, d.delay_state, w.warehouse_area_id, w.warehouse_latitude, w.warehouse_longitude
            )
            SELECT
                warehouse_id,
                warehouse_name,
                warehouse_area_id,
                warehouse_latitude,
                warehouse_longitude,
                COALESCE(
                    json_agg(
                        json_build_object('delay_state', delay_state, 'answer_count', answer_count)
                    ) FILTER (WHERE delay_state IS NOT NULL),
                    '[]'::json
                ) AS delay_time_detail
            FROM warehouse_group
            GROUP BY warehouse_id, warehouse_area_id,warehouse_name, warehouse_latitude, warehouse_longitude
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseAreaId", warehouseAreaId)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            SearchSourceWarehouse(
                warehouseId = rs.getInt("warehouse_id"),
                warehouseAreaId = rs.getInt("warehouse_area_id"),
                warehouseName = rs.getString("warehouse_name"),
                warehouseLatitude = rs.getDouble("warehouse_latitude"),
                warehouseLongitude = rs.getDouble("warehouse_longitude"),
                delayTimeDetail = jacksonObjectMapper().readValue(rs.getString("delay_time_detail"))
            )
        }
    }

    override fun getSearchSourceWarehousesByWarehouseIds(warehouseIds: List<Int>): List<SearchSourceWarehouse> {
        val sql = """
            WITH warehouse_group AS (
                SELECT
                    w.warehouse_id,
                    w.warehouse_area_id,
                    w.warehouse_name,
                    w.warehouse_latitude,
                    w.warehouse_longitude,
                    d.delay_state,
                    COUNT(d.delay_state) AS answer_count
                FROM
                    "Warehouse" w
                LEFT JOIN
                    "DelayInformation" d ON w.warehouse_id = d.warehouse_id
                    AND d.created_at >= NOW() - INTERVAL '12 HOURS'
                WHERE
                    w.warehouse_id IN (:warehouseIds)
                GROUP BY
                    w.warehouse_id, w.warehouse_name, d.delay_state, w.warehouse_area_id, w.warehouse_latitude, w.warehouse_longitude
                )
                SELECT
                warehouse_id,
                warehouse_name,
                warehouse_area_id,
                warehouse_latitude,
                warehouse_longitude,
                COALESCE(
                    json_agg(
                        json_build_object('delay_state', delay_state, 'answer_count', answer_count)
                    ) FILTER (WHERE delay_state IS NOT NULL),
                    '[]'::json
                ) AS delay_time_detail
                FROM warehouse_group
                GROUP BY warehouse_id, warehouse_area_id, warehouse_name, warehouse_latitude, warehouse_longitude

        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseIds", warehouseIds)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            SearchSourceWarehouse(
                warehouseId = rs.getInt("warehouse_id"),
                warehouseAreaId = rs.getInt("warehouse_area_id"),
                warehouseName = rs.getString("warehouse_name"),
                warehouseLatitude = rs.getDouble("warehouse_latitude"),
                warehouseLongitude = rs.getDouble("warehouse_longitude"),
                delayTimeDetail = jacksonObjectMapper().readValue(rs.getString("delay_time_detail"))
            )
        }
    }

    override fun getAllWarehouses(): List<LocalArea> {
        val sql = """
            SELECT json_agg(local_areas) AS result
            FROM (
                SELECT
                    la.local_area_id AS "localAreaId",
                    la.local_area_name AS "localAreaName",
                    COALESCE(json_agg(warehouse_areas) FILTER (WHERE warehouse_areas IS NOT NULL), '[]') AS "warehouseAreas"
                FROM "LocalArea" la
                LEFT JOIN (
                    SELECT
                        wa.local_area_id AS "localAreaId",
                        wa.warehouse_area_id AS "warehouseAreaId",
                        wa.warehouse_area_name AS "warehouseAreaName",
                        wa.warehouse_area_latitude AS "warehouseAreaLatitude",
                        wa.warehouse_area_longitude AS "warehouseAreaLongitude",
                        COALESCE(json_agg(warehouses) FILTER (WHERE warehouses IS NOT NULL), '[]') AS "warehouses"
                    FROM "WarehouseArea" wa
                    LEFT JOIN (
                        SELECT
                            w.warehouse_area_id AS "warehouseAreaId",
                            w.warehouse_id AS "warehouseId",
                            w.warehouse_name AS "warehouseName",
                            w.warehouse_latitude AS "warehouseLatitude",
                            w.warehouse_longitude AS "warehouseLongitude"
                        FROM "Warehouse" w
                    ) AS warehouses ON wa.warehouse_area_id = warehouses."warehouseAreaId"
                    GROUP BY
                        wa.local_area_id, wa.warehouse_area_id, wa.warehouse_area_name, wa.warehouse_area_latitude, wa.warehouse_area_longitude
                ) AS warehouse_areas ON la.local_area_id = warehouse_areas."localAreaId"
                GROUP BY
                    la.local_area_id, la.local_area_name
            ) AS local_areas;
        """.trimIndent()

        val jsonResult = namedParameterJdbcTemplate.queryForObject(sql, MapSqlParameterSource(), String::class.java)
        return try {
            jsonResult?.let {
                jacksonObjectMapper().readValue(it)
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}