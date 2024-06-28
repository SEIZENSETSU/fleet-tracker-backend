package fleet.tracker.controller

import fleet.tracker.dto.CommentDTO
import fleet.tracker.service.CommentService
import fleet.tracker.exeption.user.UserNotFoundException
import fleet.tracker.exeption.database.DatabaseException
import fleet.tracker.exeption.comment.CommentNotFoundException
import org.springframework.http.ResponseEntity
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.DeleteMapping

@RestController
class CommentController(private val commentService: CommentService) {

    @GetMapping("/comments")
    fun getComments(@RequestParam("warehouse_id") warehouseId: Int): ResponseEntity<List<CommentDTO>> {
        return try {
            val comments = commentService.getCommentsByWarehouseId(warehouseId)
            if (comments.isEmpty()) {
                ResponseEntity(HttpStatus.NOT_FOUND)
            } else {
                ResponseEntity.ok(comments)
            }
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("/comment")
    fun deleteComment(
        @RequestParam("comment_id") commentId: Int
    ): ResponseEntity<Void> {
        return try {
            commentService.deleteCommentById(commentId)
            ResponseEntity.noContent().build()
        } catch (e: CommentNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: DatabaseException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
