package fleet.tracker.infrastructure.warehouse

import fleet.tracker.dto.WarehouseGetDTO
import fleet.tracker.model.Warehouse
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface WarehouseRepository {
    fun getByWarehouseIdOrNull(warehouseId: Int): Warehouse?
    fun existsById(warehouseId: Int): Boolean
    fun getByWarehouseAreaId(warehouseAreaId: Int): List<Warehouse?>
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
}