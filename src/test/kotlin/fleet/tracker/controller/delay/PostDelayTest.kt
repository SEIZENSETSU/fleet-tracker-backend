package fleet.tracker.controller.delay

import com.fasterxml.jackson.databind.ObjectMapper
import fleet.tracker.dto.DelayPostDTO
import fleet.tracker.model.DelayState
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import java.awt.PageAttributes

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/warehouse/Insert_Warehouse_Test_Data.sql")
class PostDelayTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return 201 when delay is created`() {
        val delayDTO = DelayPostDTO(
            warehouseId = 1,
            delayState = DelayState.Normal,
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(delayDTO)))

        result.andExpect(status().isCreated)
        result.andExpect(content().json("""
            {
              "warehouse_id": ${delayDTO.warehouseId},
              "delay_state": "${delayDTO.delayState.toValue()}"
            }
        """.trimIndent(), true))
    }

    @Test
    fun `should return 400 when warehouseId is not provided`() {

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "warehouse_id": "",
                  "delay_state": "normal"
                }
            """.trimIndent()))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when delayState is not provided`() {

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "warehouse_id": 1,
                  "delay_state": ""
                }
            """.trimIndent()))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when delayState is invalid`() {

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "warehouse_id": 1,
                  "delay_state": "invalid"
                }
            """.trimIndent()))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when warehouseId is invalid`() {

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "warehouse_id": 0,
                  "delay_state": "normal"
                }
            """.trimIndent()))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when warehouseId is negative`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
            {
              "warehouse_id": -1,
              "delay_state": "normal"
            }
        """.trimIndent()))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when invalid parameter is provided`() {

            val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                    "invalid_param": "invalid_value"
                    }
                """.trimIndent()))

            result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 404 when warehouseId is not found`() {

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/delay")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                  "warehouse_id": 100,
                  "delay_state": "normal"
                }
            """.trimIndent()))

        result.andExpect(status().isNotFound)
    }
}