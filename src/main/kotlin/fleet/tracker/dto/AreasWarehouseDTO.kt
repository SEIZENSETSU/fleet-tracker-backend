package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class LocalAreasDTO (
    @JsonProperty("local_area_id")
    val localAreaId: Int,
    @JsonProperty("local_area_name")
    val localAreaName: String,
    @JsonProperty("warehouse_areas")
    val warehouseAreas: List<LocalWarehouseAreaDTO>
)

data class LocalWarehouseAreaDTO (
    @JsonProperty("warehouse_area_id")
    val warehouseAreaId: Int,
    @JsonProperty("warehouse_area_name")
    val warehouseAreaName: String,
    @JsonProperty("warehouse_area_latitude")
    val warehouseAreaLatitude: Double,
    @JsonProperty("warehouse_area_longitude")
    val warehouseAreaLongitude: Double,
    @JsonProperty("warehouses")
    val warehouses: List<LocalWarehouseDTO>
)

data class LocalWarehouseDTO (
    @JsonProperty("warehouse_id")
    val warehouseId: Int,
    @JsonProperty("warehouse_name")
    val warehouseName: String,
    @JsonProperty("warehouse_latitude")
    val warehouseLatitude: Double,
    @JsonProperty("warehouse_longitude")
    val warehouseLongitude: Double
)





