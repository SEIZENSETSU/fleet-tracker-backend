package fleet.tracker.controller.delay

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/delay/Insert_Delay_Test_Data.sql")
class GetDelayTest {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @Test
    fun `should return delay information when delay information exists`() {
        val warehouse1 = DelayInformationWarehouse(
            warehouseId = 1,
            warehouseAreaId = 1,
            warehouseName = "北海道倉庫1",
        )
        val warehouse2 = DelayInformationWarehouse(
            warehouseId = 2,
            warehouseAreaId = 1,
            warehouseName = "北海道倉庫2",
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/delay").param("warehouse_area_id", warehouse1.warehouseAreaId.toString()))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            [
              {
                "warehouse_id": ${warehouse2.warehouseId},
                "warehouse_name": ${warehouse2.warehouseName},
                "delay_time_detail": []
              },
              {
                "warehouse_id": ${warehouse1.warehouseId},
                "warehouse_name": ${warehouse1.warehouseName},
                "delay_time_detail": [
                  {
                    "delay_state": "normal",
                    "answer_count": 2
                  },
                  {
                    "delay_state": "pause",
                    "answer_count": 1
                  },
                  {
                    "delay_state": "impossible",
                    "answer_count": 1
                  }
                ]
              }
            ]
        """.trimIndent()))
    }

    @Test
    fun `should return 404 when delay information does not exist`() {
        val warehouseAreaId = 300

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/delay").param("warehouse_area_id", warehouseAreaId.toString()))

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 400 when warehouseAreaId is not provided`() {

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/delay"))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when warehouseAreaId is invalid`() {
        val warehouseAreaId = 0

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/delay").param("warehouse_area_id", warehouseAreaId.toString()))

        result.andExpect(status().isBadRequest)
    }
}

data class DelayInformationWarehouse (
    val warehouseId: Int,
    val warehouseAreaId: Int,
    val warehouseName: String,
)

