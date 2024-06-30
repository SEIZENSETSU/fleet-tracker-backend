package fleet.tracker.model

data class LocalArea (
    val localAreaId: Int,
    val localAreaName: String,
    val warehouseAreas: List<LocalWarehouseArea>
)

data class LocalWarehouseArea (
    val localAreaId: Int,
    val warehouseAreaId: Int,
    val warehouseAreaName: String,
    val warehouseAreaLatitude: Double,
    val warehouseAreaLongitude: Double,
    val warehouses: List<Warehouse>
)