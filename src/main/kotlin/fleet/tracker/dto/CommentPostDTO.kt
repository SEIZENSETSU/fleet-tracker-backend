package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class CommentPostDTO(
    @JsonProperty("uid")
    val uid: String,
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("contents")
    val contents: String
)
