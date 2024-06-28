package fleet.tracker.repository

import fleet.tracker.model.Comment
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class CommentRepository(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    fun findAllByWarehouseId(warehouseId: Int): List<Comment> {
        val sql = """
            SELECT comment_id, uid, warehouse_id, contents, created_at, updated_at
            FROM "Comment"
            WHERE warehouse_id = :warehouseId
            ORDER BY created_at DESC
        """.trimIndent()

        val sqlParams = MapSqlParameterSource()
        sqlParams.addValue("warehouseId", warehouseId)

        return namedParameterJdbcTemplate.query(sql, sqlParams) { rs, _ ->
            Comment(
                commentId = rs.getInt("comment_id"),
                uid = rs.getString("uid"),
                warehouseId = rs.getInt("warehouse_id"),
                contents = rs.getString("contents"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime(),
                updatedAt = rs.getTimestamp("updated_at").toLocalDateTime()
            )
        }
    }

    fun isCommentExists(commentId: Int): Boolean {
        val sql = "SELECT EXISTS(SELECT 1 FROM \"Comment\" WHERE comment_id = :commentId)"
        val sqlParams = MapSqlParameterSource().addValue("commentId", commentId)
        return namedParameterJdbcTemplate.queryForObject(sql, sqlParams, Boolean::class.java) ?: false
    }

    fun deleteByCommentId(commentId: Int) {
        val sql = "DELETE FROM Comment WHERE comment_id = :commentId"
        val sqlParams = MapSqlParameterSource().addValue("commentId", commentId)
        namedParameterJdbcTemplate.update(sql, sqlParams)
    }
}
