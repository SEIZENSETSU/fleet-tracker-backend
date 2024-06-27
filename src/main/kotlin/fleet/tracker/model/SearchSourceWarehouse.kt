package fleet.tracker.model

import fleet.tracker.dto.DelayTimeDetail

data class SearchSourceWarehouse (
    val warehouseId: Int,
    val warehouseAreaId: Int,
    val warehouseName: String,
    val warehouseLatitude: Double,
    val warehouseLongitude: Double,
    val delayTimeDetail: List<DelayTimeDetail?>,
)