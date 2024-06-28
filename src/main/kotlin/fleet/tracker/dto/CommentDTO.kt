package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CommentDTO(
    @JsonProperty("comment_id")
    val commentId: Int,
    @JsonProperty("uid")
    val uid: String,
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("contents")
    val contents: String,
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val createdAt: LocalDateTime
)
