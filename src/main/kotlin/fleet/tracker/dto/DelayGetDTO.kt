package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import fleet.tracker.model.DelayState

data class DelayGetDTO(
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("warehouse_name")
    val warehouseName: String,
    @JsonProperty("delay_time_detail")
    val delayTimeDetail: List<DelayTimeDetail?>,
)

data class DelayTimeDetail(
    @JsonProperty("delay_state")
    val delayState: DelayState,
    @JsonProperty("answer_count")
    val answerCount: Int,
)