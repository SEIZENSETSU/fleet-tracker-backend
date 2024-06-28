package fleet.tracker.infrastructure.delay

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fleet.tracker.dto.DelayGetDTO
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface DelayRepository {
    fun getByWarehouseAreaId(warehouseAreaId: Int): List<DelayGetDTO>
    fun save(warehouseId: Int, delayState: String)
}

@Repository
class DelayRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : DelayRepository {

    override fun getByWarehouseAreaId(warehouseAreaId: Int): List<DelayGetDTO> {
        val sql = """
            WITH delay_counts AS (
                SELECT
                    w.warehouse_id,
                    w.warehouse_name,
                    d.delay_state,
                    COUNT(d.delay_state) AS answer_count
                FROM
                    "Warehouse" w
                LEFT JOIN
                    "DelayInformation" d ON w.warehouse_id = d.warehouse_id
                    AND d.created_at >= NOW() - INTERVAL '24 HOURS'
                WHERE
                    w.warehouse_area_id = :warehouseAreaId
                GROUP BY
                    w.warehouse_id, w.warehouse_name, d.delay_state
            )
            SELECT
                warehouse_id,
                warehouse_name,
                COALESCE(
                    json_agg(
                        json_build_object('delay_state', delay_state, 'answer_count', answer_count)
                    ) FILTER (WHERE delay_state IS NOT NULL),
                    '[]'::json
                ) AS delay_time_detail
            FROM
                delay_counts
            GROUP BY
                warehouse_id, warehouse_name;
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseAreaId", warehouseAreaId)

        try {
            return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
                DelayGetDTO(
                    warehouseId = rs.getInt("warehouse_id"),
                    warehouseName = rs.getString("warehouse_name"),
                    delayTimeDetail = jacksonObjectMapper().readValue(rs.getString("delay_time_detail"))
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override fun save(warehouseId: Int, delayState: String) {
        val sql = """
            INSERT INTO "DelayInformation" (warehouse_id, delay_state) VALUES (:warehouseId, :delayState::warehousedelaystate)
        """.trimIndent()
        val sqlParams = MapSqlParameterSource()
            .addValue("warehouseId", warehouseId)
            .addValue("delayState", delayState)

        try {
            namedParameterJdbcTemplate.update(sql, sqlParams)
        } catch (e: Exception) {
            throw e
        }
    }
}

class DatabaseOperationException(message: String, cause: Throwable) : RuntimeException(message, cause)