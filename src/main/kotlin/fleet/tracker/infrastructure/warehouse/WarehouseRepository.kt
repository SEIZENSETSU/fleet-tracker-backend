package fleet.tracker.infrastructure.warehouse

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface WarehouseRepository {
    fun existsById(warehouseId: Int): Boolean
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
}