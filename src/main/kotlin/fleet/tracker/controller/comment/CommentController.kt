package fleet.tracker.controller

import fleet.tracker.dto.CommentDTO
import fleet.tracker.service.CommentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus

@RestController
class CommentController(private val commentService: CommentService) {

    @GetMapping("/comments")
    fun getComments(@RequestParam("warehouse_id") warehouseId: Int): ResponseEntity<List<CommentDTO>> {
        val comments = commentService.getCommentsByWarehouseId(warehouseId)
        return if (comments.isEmpty()) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        } else {
            ResponseEntity(comments, HttpStatus.OK)
        }
    }
}
