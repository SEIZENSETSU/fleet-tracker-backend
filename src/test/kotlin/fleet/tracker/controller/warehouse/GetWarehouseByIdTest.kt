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
class GetWarehouseByIdTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return warehouse when warehouse exists`() {
        val warehouseGetDTO = WarehouseGetDTO(
            warehouseId = 1,
            warehouseAreaId = 1,
            warehouseName = "北海道倉庫1",
            warehouseLatitude = 43.0722,
            warehouseLongitude = 141.355
        )

        val result  = mockMvc.perform(MockMvcRequestBuilders.get("/warehouse").param("warehouse_id", warehouseGetDTO.warehouseId.toString()))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "warehouse_id": ${warehouseGetDTO.warehouseId},
                "warehouse_name": "${warehouseGetDTO.warehouseName}",
                "warehouse_area_id": ${warehouseGetDTO.warehouseAreaId},
                "warehouse_latitude": ${warehouseGetDTO.warehouseLatitude},
                "warehouse_longitude": ${warehouseGetDTO.warehouseLongitude}
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 404 when warehouse does not exist`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/warehouse").param("warehouse_id", "100"))

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 400 when warehouse_id is not provided`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/warehouse"))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when invalid parameter is provided`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/warehouse").param("invalid_param", "invalid_value"))

        result.andExpect(status().isBadRequest)
    }

}