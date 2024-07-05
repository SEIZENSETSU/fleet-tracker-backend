package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty
import fleet.tracker.model.DelayState

data class WarehouseSearchDTO(
    @JsonProperty("is_invading")
    val isInvading: Boolean,
    @JsonProperty("warehouses")
    val warehouses: List<WarehousesSearchResultDTO?>,
    @JsonProperty("favorite_warehouses")
    val favoriteWarehouses: List<WarehousesSearchResultDTO?>,
    @JsonProperty("warehouse_areas")
    val warehouseAreas: List<WarehouseAreaSearchResultDTO?>,
)

data class WarehousesSearchResultDTO(
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("warehouse_area_id")
    val warehouseAreaId: Int,
    @JsonProperty("warehouse_name")
    val warehouseName: String,
    @JsonProperty("average_delay_state")
    val averageDelayState: DelayState,
    @JsonProperty("delay_time_detail")
    val delayTimeDetail: List<DelayTimeDetail?>,
    @JsonProperty("distance")
    val distance: Double,
)

data class WarehouseAreaSearchResultDTO(
    @JsonProperty("warehouse_area_id")
    val warehouseAreaId: Int,
    @JsonProperty("warehouse_area_name")
    val warehouseAreaName: String,
    @JsonProperty("warehouse_area_latitude")
    val warehouseAreaLatitude: Double,
    @JsonProperty("warehouse_area_longitude")
    val warehouseAreaLongitude: Double,
    @JsonProperty("average_delay_state")
    val averageDelayState: DelayState,
    @JsonProperty("distance")
    val distance: Double,
)