package fleet.tracker.model

import fleet.tracker.dto.DelayTimeDetail

data class SearchSourceWarehouseArea (
    val warehouseAreaId: Int,
    val warehouseAreaName: String,
    val warehouseAreaLatitude: Double,
    val warehouseAreaLongitude: Double,
    val delayTimeDetail: List<DelayTimeDetail?>,
)