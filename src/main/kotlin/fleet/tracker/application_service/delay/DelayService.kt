package fleet.tracker.application_service.delay

import fleet.tracker.dto.DelayGetDTO
import fleet.tracker.dto.DelayPostDTO
import fleet.tracker.exeption.database.DatabaseException
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import fleet.tracker.infrastructure.delay.DelayRepository
import fleet.tracker.infrastructure.warehouse.WarehouseRepository
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

interface DelayService {
    fun getDelaysByWarehouseAreaId(warehouseAreaId: Int): List<DelayGetDTO>
    fun addDelay(delayPostDTO: DelayPostDTO): DelayPostDTO
}

@Service
class DelayServiceImpl(val delayRepository: DelayRepository, val warehouseRepository: WarehouseRepository) :
    DelayService {

    override fun getDelaysByWarehouseAreaId(warehouseAreaId: Int): List<DelayGetDTO> {
        try {
            if (!warehouseRepository.existsById(warehouseAreaId)) {
                throw WarehouseNotFoundException("WarehouseArea not found")
            }

            return delayRepository.getByWarehouseAreaId(warehouseAreaId)
        } catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }
    override fun addDelay(delayPostDTO: DelayPostDTO): DelayPostDTO {
        try {
            if (!warehouseRepository.existsById(delayPostDTO.warehouseId)) {
                throw WarehouseNotFoundException("Warehouse not found")
            }

            delayRepository.save(delayPostDTO.warehouseId, delayPostDTO.delayState.toValue())

            return DelayPostDTO(
                warehouseId = delayPostDTO.warehouseId,
                delayState = delayPostDTO.delayState
            )
        }  catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }
}