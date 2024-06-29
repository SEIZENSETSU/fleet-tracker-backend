package fleet.tracker.controller.warehouse

import fleet.tracker.application_service.warehouse.WarehouseService
import fleet.tracker.dto.LocalAreasDTO
import fleet.tracker.dto.WarehouseGetDTO
import fleet.tracker.dto.WarehouseSearchDTO
import fleet.tracker.exception.warehouse.WarehouseNotFoundException
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

    @GetMapping("/warehouses")
    fun getWarehouseByWarehouseAreaId(@RequestParam("warehouse_area_id") warehouseAreaId: Int): ResponseEntity<List<WarehouseGetDTO>> {
        return try {
            val warehouses = warehouseService.getByWarehouseAreaId(warehouseAreaId)
            ResponseEntity.ok(warehouses)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/warehouses/search")
    fun getSearchWarehouses(
        @RequestParam("favorite_warehouse_ids") favoriteWarehouseIds: List<Int>?,
        @RequestParam("user_latitude") userLatitude: Double,
        @RequestParam("user_longitude") userLongitude: Double,
    ): ResponseEntity<WarehouseSearchDTO> {
        return try {
            val result = warehouseService.getSearchWarehouses(
                favoriteWarehouseIds,
                userLatitude,
                userLongitude
            )
            ResponseEntity.ok(result)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("/areas/warehouses")
    fun getAllWarehouses(): ResponseEntity<List<LocalAreasDTO>> {
        return try {
            val warehouses = warehouseService.getAllWarehouses()
            ResponseEntity.ok(warehouses)
        } catch (e: WarehouseNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}