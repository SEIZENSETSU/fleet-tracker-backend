package fleet.tracker.application_service.warehouse

import fleet.tracker.dto.WarehouseGetDTO
import fleet.tracker.exeption.database.DatabaseException
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import fleet.tracker.infrastructure.warehouse.WarehouseRepository
import org.springframework.stereotype.Service

interface WarehouseService {
    fun getByWarehouseId(warehouseId: Int): WarehouseGetDTO
}

@Service
class WarehouseServiceImpl(val warehouseRepository: WarehouseRepository) : WarehouseService {
    override fun getByWarehouseId(warehouseId: Int): WarehouseGetDTO {
        try {
            val warehouse = warehouseRepository.getByWarehouseIdOrNull(warehouseId)
                ?: throw WarehouseNotFoundException("Warehouse not found")

            return WarehouseGetDTO(
                warehouseId = warehouse.warehouseId,
                warehouseName = warehouse.warehouseName,
                warehouseAreaId = warehouse.warehouseAreaId,
                warehouseLatitude = warehouse.warehouseLatitude,
                warehouseLongitude = warehouse.warehouseLongitude
            )
        } catch (e: DatabaseException) {
            throw DatabaseException("Database error", e)
        }
    }
}