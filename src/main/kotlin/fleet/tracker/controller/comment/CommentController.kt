package fleet.tracker.controller

import fleet.tracker.dto.CommentDTO
import fleet.tracker.dto.CommentPostDTO
import fleet.tracker.service.CommentService
import fleet.tracker.exception.user.UserNotFoundException
import fleet.tracker.exception.database.DatabaseException
import fleet.tracker.exception.comment.CommentNotFoundException
import org.springframework.http.ResponseEntity
import fleet.tracker.exception.warehouse.WarehouseNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestController
class CommentController(private val commentService: CommentService) {

    @GetMapping("/comments")
    fun getComments(@RequestParam("warehouse_id") warehouseId: Int): ResponseEntity<List<CommentDTO>> {
        return try {
            val comments = commentService.getCommentsByWarehouseId(warehouseId)

            ResponseEntity.ok(comments)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @PostMapping("/comment")
    fun createComment(@RequestBody commentPostDTO: CommentPostDTO): ResponseEntity<CommentPostDTO> {
        return try {
            commentService.createComment(commentPostDTO)
            ResponseEntity.status(HttpStatus.CREATED).body(commentPostDTO)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: DatabaseException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @DeleteMapping("/comment")
    fun deleteComment(@RequestParam("comment_id") commentId: Int): ResponseEntity<Void> {
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
