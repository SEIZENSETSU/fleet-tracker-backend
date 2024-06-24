package fleet.tracker.controller.delay

import fleet.tracker.application_service.delay.DelayService
import fleet.tracker.dto.DelayGetDTO
import fleet.tracker.dto.DelayPostDTO
import fleet.tracker.exeption.warehoues_area.WarehouseAreaNotFoundException
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class DelayController(val delayService: DelayService) {
    @GetMapping("/delay")
    fun getDelaysByWarehouseAreaId(@RequestParam("warehouse_area_id") warehouseAreaId: Int): ResponseEntity<List<DelayGetDTO>> {
        return try {
            if (warehouseAreaId <= 0) return ResponseEntity.badRequest().build()

            val delays = delayService.getDelaysByWarehouseAreaId(warehouseAreaId)
            ResponseEntity.ok(delays)
        } catch (e: WarehouseAreaNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @PostMapping("/delay")
    fun addDelay(@RequestBody delayPostDTO: DelayPostDTO): ResponseEntity<DelayPostDTO> {
        if (!delayPostDTO.isValid()) return ResponseEntity.badRequest().build()

        return try {
            val result = delayService.addDelay(delayPostDTO)
            ResponseEntity.status(HttpStatus.CREATED).body(result)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        }
        catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

}