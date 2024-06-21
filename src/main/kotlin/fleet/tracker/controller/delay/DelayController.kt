package fleet.tracker.controller.delay

import fleet.tracker.application_service.delay.DelayService
import fleet.tracker.dto.DelayPostDTO
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DelayController(val delayService: DelayService) {
    @PostMapping("/delay")
    fun addDelay(@RequestBody delayPostDTO: DelayPostDTO): ResponseEntity<DelayPostDTO> {
        if (!delayPostDTO.isValid()) return ResponseEntity.badRequest().build()

        return try {
            val result = delayService.addDelay(delayPostDTO)
            ResponseEntity.ok(result)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        }
        catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

}