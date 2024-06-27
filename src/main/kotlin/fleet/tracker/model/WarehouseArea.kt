package fleet.tracker.model

data class WarehouseArea (
    val warehouseAreaId: Int,
    val warehouseAreaName: String,
    val warehouseAreaLatitude: Double,
    val warehouseAreaLongitude: Double,
    val warehouseAreaRadius: Double
)