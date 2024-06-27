package fleet.tracker.model

import java.time.LocalDateTime

data class Comment(
    val commentId: Int,
    val uid: String,
    val warehouseId: Int,
    val contents: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
