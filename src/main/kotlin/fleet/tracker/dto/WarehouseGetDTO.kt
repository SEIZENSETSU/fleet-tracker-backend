package fleet.tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty

data class WarehouseGetDTO(
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("warehouse_area_id")
    val warehouseAreaId: Int,
    @JsonProperty("warehouse_name")
    val warehouseName: String,
    @JsonProperty("warehouse_latitude")
    val warehouseLatitude: Double,
    @JsonProperty("warehouse_longitude")
    val warehouseLongitude: Double
)
