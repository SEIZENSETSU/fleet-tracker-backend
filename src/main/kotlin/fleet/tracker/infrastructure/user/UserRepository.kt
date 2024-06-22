package fleet.tracker.infrastructure.user

import fleet.tracker.model.User
import fleet.tracker.dto.UserDTO
import fleet.tracker.dto.CreateUserDTO
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface UserRepository {
    fun findByUserOrNull(uid: String): User?
    fun save(user: User): User
    fun isUserExists(uid: String): Boolean
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

    override fun save(user: User): User {
        val sql = """
            INSERT INTO "User" (uid, user_name, fcm_token_id) 
            VALUES (:uid, :userName, :fcmTokenId)
        """.trimIndent()
        val sqlParams = MapSqlParameterSource()
            .addValue("uid", user.uid)
            .addValue("userName", user.userName)
            .addValue("fcmTokenId", user.fcmTokenId)
        
        namedParameterJdbcTemplate.update(sql, sqlParams)
        return user
    }

    override fun isUserExists(uid: String): Boolean {
        val sql = """
            SELECT COUNT(*) FROM "User" WHERE uid=:uid
        """.trimIndent()
        val sqlParams = MapSqlParameterSource()
            .addValue("uid", uid)

        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Int::class.java) ?: 0 > 0
    }
}