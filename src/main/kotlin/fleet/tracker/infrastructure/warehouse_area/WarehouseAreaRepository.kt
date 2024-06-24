package fleet.tracker.infrastructure.warehouse_area

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface WarehouseAreaRepository {
    fun existsById(warehouseId: Int): Boolean
}

@Repository
class WarehouseAreaRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): WarehouseAreaRepository{
    override fun existsById(warehouseId: Int): Boolean {
        val sql = """
            SELECT EXISTS( SELECT * FROM "WarehouseArea" WHERE warehouse_area_id = :warehouse_id);
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
            .addValue("warehouse_id", warehouseId)

        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Boolean::class.java) ?: false
    }
}