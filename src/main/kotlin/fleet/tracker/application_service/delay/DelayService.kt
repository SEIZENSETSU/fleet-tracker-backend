package fleet.tracker.application_service.delay

import fleet.tracker.dto.DelayGetDTO
import fleet.tracker.dto.DelayPostDTO
import fleet.tracker.dto.DelayTimeDetail
import fleet.tracker.exception.database.DatabaseException
import fleet.tracker.exception.warehoues_area.WarehouseAreaNotFoundException
import fleet.tracker.exception.warehouse.WarehouseNotFoundException
import fleet.tracker.infrastructure.delay.DelayRepository
import fleet.tracker.infrastructure.warehouse.WarehouseRepository
import fleet.tracker.model.DelayState
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
                throw WarehouseAreaNotFoundException("WarehouseArea not found")
            }

            val delays = delayRepository.getByWarehouseAreaId(warehouseAreaId)

            val allStates = DelayState.entries

            return delays.map { delay ->
                val existingDetails = delay.delayTimeDetail.associateBy { it?.delayState }

                val completeDetails = allStates.map { state ->
                    existingDetails[state] ?: DelayTimeDetail(state, 0)
                }

                DelayGetDTO(
                    warehouseId = delay.warehouseId,
                    warehouseName = delay.warehouseName,
                    delayTimeDetail = completeDetails
                )
            }
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