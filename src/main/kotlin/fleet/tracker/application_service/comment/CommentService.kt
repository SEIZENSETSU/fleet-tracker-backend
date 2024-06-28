package fleet.tracker.service

import fleet.tracker.dto.CommentDTO
import fleet.tracker.repository.CommentRepository
import fleet.tracker.exeption.database.DatabaseException
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentRepository: CommentRepository) {

    fun getCommentsByWarehouseId(warehouseId: Int): List<CommentDTO> {
        try {
            val comments = commentRepository.findAllByWarehouseId(warehouseId)
            return comments.map { comment ->
                CommentDTO(
                    commentId = comment.commentId,
                    uid = comment.uid,
                    warehouseId = comment.warehouseId,
                    contents = comment.contents,
                    createdAt = comment.createdAt
                )
            }
        } catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }
}
