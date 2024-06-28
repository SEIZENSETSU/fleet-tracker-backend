package fleet.tracker.model

import fleet.tracker.dto.DelayTimeDetail

data class SearchSourceWarehouseArea (
    val warehouseAreaId: Int,
    val warehouseAreaName: String,
    val delayTimeDetail: List<DelayTimeDetail?>,
)