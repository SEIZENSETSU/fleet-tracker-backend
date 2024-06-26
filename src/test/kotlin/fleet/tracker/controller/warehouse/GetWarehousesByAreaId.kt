package fleet.tracker.controller.warehouse

import fleet.tracker.dto.WarehouseGetDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class GetWarehousesByAreaId {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return warehouses when warehouse area exists`() {
        val warehouses: List<WarehouseGetDTO> = listOf(
            WarehouseGetDTO(
                warehouseId = 1,
                warehouseAreaId = 1,
                warehouseName = "北海道倉庫1",
                warehouseLatitude = 43.0722,
                warehouseLongitude = 141.355
            ),
            WarehouseGetDTO(
                warehouseId = 2,
                warehouseAreaId = 1,
                warehouseName = "北海道倉庫2",
                warehouseLatitude = 43.0562,
                warehouseLongitude = 141.338
            )
        )
        val result  = mockMvc.perform(MockMvcRequestBuilders.get("/warehouses").param("warehouse_area_id", warehouses.first().warehouseAreaId.toString()))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            [
                {
                    "warehouse_id": ${warehouses.first().warehouseId},
                    "warehouse_name": "${warehouses.first().warehouseName}",
                    "warehouse_area_id": ${warehouses.first().warehouseAreaId},
                    "warehouse_latitude": ${warehouses.first().warehouseLatitude},
                    "warehouse_longitude": ${warehouses.first().warehouseLongitude}
                },
                {
                    "warehouse_id": ${warehouses.last().warehouseId},
                    "warehouse_name": "${warehouses.last().warehouseName}",
                    "warehouse_area_id": ${warehouses.last().warehouseAreaId},
                    "warehouse_latitude": ${warehouses.last().warehouseLatitude},
                    "warehouse_longitude": ${warehouses.last().warehouseLongitude}
                }
            ]
        """.trimIndent()))
    }

    @Test
    fun `should return 404 when warehouse area does not exist`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/warehouses").param("warehouse_area_id", "100"))

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 400 when warehouse_area_id is not provided`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/warehouses"))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when invalid parameter is provided`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/warehouses").param("invalid_param", "invalid_value"))

        result.andExpect(status().isBadRequest)
    }
}