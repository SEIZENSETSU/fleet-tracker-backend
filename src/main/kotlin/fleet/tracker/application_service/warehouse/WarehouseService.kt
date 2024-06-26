package fleet.tracker.application_service.warehouse

import fleet.tracker.dto.WarehouseGetDTO
import fleet.tracker.exeption.database.DatabaseException
import fleet.tracker.exeption.warehoues_area.WarehouseAreaNotFoundException
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import fleet.tracker.infrastructure.warehouse.WarehouseRepository
import org.springframework.stereotype.Service

interface WarehouseService {
    fun getByWarehouseId(warehouseId: Int): WarehouseGetDTO
    fun getByWarehouseAreaId(warehouseAreaId: Int): List<WarehouseGetDTO>
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

    override fun getByWarehouseAreaId(warehouseAreaId: Int): List<WarehouseGetDTO> {
        try {
            val warehouses = warehouseRepository.getByWarehouseAreaId(warehouseAreaId)

            if (warehouses.isEmpty()) throw WarehouseNotFoundException("Warehouse area not found")

            return warehouses.filterNotNull().map {
                WarehouseGetDTO(
                    warehouseId = it.warehouseId,
                    warehouseName = it.warehouseName,
                    warehouseAreaId = it.warehouseAreaId,
                    warehouseLatitude = it.warehouseLatitude,
                    warehouseLongitude = it.warehouseLongitude
                )
            }
        } catch (e: DatabaseException) {
            throw DatabaseException("Database error", e)
        }
    }
}