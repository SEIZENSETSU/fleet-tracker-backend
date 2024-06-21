package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import fleet.tracker.model.DelayState

data class DelayPostDTO (
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("delay_state")
    val delayState: DelayState
) {
    @JsonIgnore
    fun isValid(): Boolean {
        return  warehouseId > 0 && warehouseId.toString().isNotEmpty() && delayState.toString().isNotEmpty()
    }
}