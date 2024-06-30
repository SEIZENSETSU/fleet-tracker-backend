package fleet.tracker.service

import fleet.tracker.model.Comment
import fleet.tracker.dto.CommentDTO
import fleet.tracker.dto.CommentPostDTO
import fleet.tracker.repository.CommentRepository
import fleet.tracker.infrastructure.warehouse.WarehouseRepository
import fleet.tracker.infrastructure.user.UserRepository
import fleet.tracker.exception.database.DatabaseException
import fleet.tracker.exception.comment.CommentNotFoundException
import fleet.tracker.exception.user.UserNotFoundException
import fleet.tracker.exception.warehouse.WarehouseNotFoundException
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val warehouseRepository: WarehouseRepository,
    private val userRepository: UserRepository
    ) {

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

    fun createComment(commentPostDTO: CommentPostDTO): CommentDTO {
        if (!warehouseRepository.existsById(commentPostDTO.warehouseId)) {
            throw WarehouseNotFoundException("Warehouse not found")
        }

        if (!userRepository.isUserExists(commentPostDTO.uid)) {
            throw UserNotFoundException("User not found")
        }

        val comment = Comment(
            commentId = 0, // 自動生成される場合
            uid = commentPostDTO.uid,
            warehouseId = commentPostDTO.warehouseId,
            contents = commentPostDTO.contents,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        return try {
            commentRepository.save(comment)
            CommentDTO(
                commentId = comment.commentId,
                uid = comment.uid,
                warehouseId = comment.warehouseId,
                contents = comment.contents,
                createdAt = comment.createdAt
            )
        } catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }

    fun deleteCommentById(commentId: Int) {
        if (!commentRepository.isCommentExists(commentId)) {
            throw CommentNotFoundException("Comment not found")
        }
        try {
            commentRepository.deleteByCommentId(commentId)
        } catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }
}
