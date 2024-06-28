package fleet.tracker.infrastructure.warehouse_area

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fleet.tracker.model.SearchSourceWarehouseArea
import fleet.tracker.model.WarehouseArea
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface WarehouseAreaRepository {
    fun getAllWarehouseArea(): List<WarehouseArea>
    fun existsById(warehouseId: Int): Boolean
    fun getSearchSourceWarehouseAreaByWarehouseAreaIds(warehouseAreaId:List<Int>): List<SearchSourceWarehouseArea>
}

@Repository
class WarehouseAreaRepositoryImpl(
    val namedParameterJdbcTemplate: NamedParameterJdbcTemplate,
): WarehouseAreaRepository{
    override fun existsById(warehouseId: Int): Boolean {
        val sql = """
            SELECT EXISTS( SELECT * FROM "WarehouseArea" WHERE warehouse_area_id = :warehouse_id);
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouse_id", warehouseId)

        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Boolean::class.java) ?: false
    }

    override fun getAllWarehouseArea(): List<WarehouseArea> {
        val sql = """
            SELECT * from "WarehouseArea"
        """.trimIndent()

        return namedParameterJdbcTemplate.query(sql) { rs, _ ->
            WarehouseArea(
                warehouseAreaId = rs.getInt("warehouse_area_id"),
                warehouseAreaName = rs.getString("warehouse_area_name"),
                warehouseAreaLatitude = rs.getDouble("warehouse_area_latitude"),
                warehouseAreaLongitude = rs.getDouble("warehouse_area_longitude"),
                warehouseAreaRadius = rs.getDouble("warehouse_area_radius")
            )
        }
    }

    override fun getSearchSourceWarehouseAreaByWarehouseAreaIds(warehouseAreaId: List<Int>): List<SearchSourceWarehouseArea> {
        val sql = """
            WITH delay_counts AS (
                SELECT
                    a.warehouse_area_id,
                    a.warehouse_area_name,
                    d.delay_state,
                    COUNT(d.delay_state) AS answer_count
                FROM "WarehouseArea" a
                LEFT JOIN
                    "Warehouse" w ON a.warehouse_area_id = w.warehouse_area_id
                LEFT JOIN
                    "DelayInformation" d ON w.warehouse_id = d.warehouse_id
                    AND d.created_at >= NOW() - INTERVAL '24 HOURS'
                WHERE
                    w.warehouse_area_id IN (:warehouseAreaId)
                GROUP BY
                    a.warehouse_area_id, a.warehouse_area_name, d.delay_state
            )
            SELECT
                warehouse_area_id,
                warehouse_area_name,
                COALESCE(
                    json_agg(
                        json_build_object('delay_state', delay_state, 'answer_count', answer_count)
                    ) FILTER (WHERE delay_state IS NOT NULL),
                    '[]'::json
                ) AS delay_time_detail
            FROM
                delay_counts
            GROUP BY
                warehouse_area_id, warehouse_area_name;
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseAreaId", warehouseAreaId)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            SearchSourceWarehouseArea(
                warehouseAreaId = rs.getInt("warehouse_area_id"),
                warehouseAreaName = rs.getString("warehouse_area_name"),
                delayTimeDetail = jacksonObjectMapper().readValue(rs.getString("delay_time_detail"))
            )
        }
    }
}