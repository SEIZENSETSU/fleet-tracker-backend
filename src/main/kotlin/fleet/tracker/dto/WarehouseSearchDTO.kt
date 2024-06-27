package fleet.tracker.dto

import fleet.tracker.model.DelayState

data class WarehouseSearchDTO(
    val isInvading: Boolean,
    val warehouses: List<WarehousesSearchResultDTO?>,
    val favoriteWarehouses: List<WarehousesSearchResultDTO?>,
    val warehouseAreas: List<WarehouseAreaSearchResultDTO?>,
)

data class WarehousesSearchResultDTO(
    val warehouseId: Int,
    val warehouseAreaId: Int,
    val warehouseName: String,
    val averageDelayState: DelayState,
    val delayTimeDetail: List<DelayTimeDetail?>,
    val distance: Double,
)

data class WarehouseAreaSearchResultDTO(
    val warehouseAreaId: Int,
    val warehouseAreaName: String,
    val averageDelayState: DelayState,
    val distance: Double,
)