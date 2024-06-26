package fleet.tracker.controller.warehouse

import fleet.tracker.application_service.warehouse.WarehouseService
import fleet.tracker.dto.WarehouseGetDTO
import fleet.tracker.exeption.warehouse.WarehouseNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WarehouseController(val warehouseService: WarehouseService) {

    @GetMapping("/warehouse")
    fun getWarehouseByWarehouseId(@RequestParam("warehouse_id") warehouseId: Int): ResponseEntity<WarehouseGetDTO> {
        return try {
            val warehouse = warehouseService.getByWarehouseId(warehouseId)
            ResponseEntity.ok(warehouse)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}