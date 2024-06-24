package fleet.tracker.model

data class Warehouse (
    val warehouseId: Int,
    val warehouseAreaId: Int,
    val warehouseName: String,
    val warehouseLatitude: Double,
    val warehouseLongitude: Double,
)