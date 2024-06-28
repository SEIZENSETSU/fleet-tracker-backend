package fleet.tracker.controller

import fleet.tracker.dto.CommentDTO
import fleet.tracker.service.CommentService
import org.springframework.http.ResponseEntity
import fleet.tracker.exception.warehouse.WarehouseNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
}
