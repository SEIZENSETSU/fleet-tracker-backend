package fleet.tracker.infrastructure.delay

import fleet.tracker.dto.DelayPostDTO
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface DelayRepository {
    fun save(warehouseId: Int, delayState: String)
}

@Repository
class DelayRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : DelayRepository {

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