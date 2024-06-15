package fleet.tracker.infrastructure.user

import fleet.tracker.model.User
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface UserRepository {
    fun findByUserOrNull(uid: String): User?
}

@Repository
class UserRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : UserRepository {
    override fun findByUserOrNull(uid: String): User? {
        val sql = """
            SELECT uid, user_name, fcm_token_id FROM "User" WHERE uid=:uid
        """.trimIndent()
        val sqlParams = MapSqlParameterSource()
            .addValue("uid", uid)

        return try { namedParameterJdbcTemplate.queryForObject(sql, sqlParams) { rs, _ ->
            User(
                uid = rs.getString("uid"),
                userName = rs.getString("user_name"),
                fcmTokenId = rs.getString("fcm_token_id")
            )
        }} catch (e: EmptyResultDataAccessException) {
            null
        }
    }
}