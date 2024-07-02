package fleet.tracker.infrastructure.user

import fleet.tracker.model.User
import fleet.tracker.dto.UserDTO
import fleet.tracker.dto.CreateUserDTO
import fleet.tracker.repository.CommentRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

interface UserRepository {
    fun findByUserOrNull(uid: String): User?
    fun save(user: User): User
    fun isUserExists(uid: String): Boolean
    fun deleteByUserId(uid: String)
    fun update(user: User): User
}

@Repository
class UserRepositoryImpl(val namedParameterJdbcTemplate: NamedParameterJdbcTemplate, private val commentRepository: CommentRepository) : UserRepository {
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
            SELECT EXISTS(SELECT 1 FROM "User" WHERE uid = :uid)
        """.trimIndent()
        val sqlParams = MapSqlParameterSource().addValue("uid", uid)

        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Boolean::class.java) ?: false
    }

    override fun deleteByUserId(uid: String) {
        if (commentRepository.isCommentExistsByUid(uid)) {
            commentRepository.deleteAllByUid(uid)
        }
        
        val sql = """
            DELETE FROM "User" WHERE uid=:uid
        """.trimIndent()
        
        val sqlParams = MapSqlParameterSource().addValue("uid", uid)
    
        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
    
    

    override fun update(user: User): User {
        val sql = """
            UPDATE "User" SET user_name=:userName, fcm_token_id=:fcmTokenId WHERE uid=:uid
        """.trimIndent()
        val sqlParams = MapSqlParameterSource()
            .addValue("uid", user.uid)
            .addValue("userName", user.userName)
            .addValue("fcmTokenId", user.fcmTokenId)

        namedParameterJdbcTemplate.update(sql, sqlParams)
        return user
    }
}