package fleet.tracker.controller.comment

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/user/Insert_User_Test_Data.sql")
@Sql("/comment/Insert_Comment_Test_Data.sql")
class GetCommentsTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
    }

    @Test
    fun `should return comments for a given warehouse id`() {
        val warehouseId = 1
        val expectedJson = """
            [
              {
                "comment_id" : 2,
                "uid" : "test_user_1",
                "warehouse_id" : 1,
                "contents" : "test_comment_2",
                "created_at" : "2022-07-03 09:00:00"
              },
              {
                "comment_id" : 1,
                "uid" : "test_user_1",
                "warehouse_id" : 1,
                "contents" : "test_comment_1",
                "created_at" : "2022-07-02 09:00:00"
              }
            ]
        """.trimIndent()

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/comments")
                .param("warehouse_id", warehouseId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )

        result.andExpect(MockMvcResultMatchers.status().isOk)
        result.andExpect(MockMvcResultMatchers.content().json(expectedJson))
    }

    @Test
    fun `should return 404 when no comments found for a given warehouse id`() {
        val warehouseId = 999

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/comments")
                .param("warehouse_id", warehouseId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )

        result.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should return 404 when warehouse id is not valid`() {
        val warehouseId = -1

        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/comments")
                .param("warehouse_id", warehouseId.toString())
                .accept(MediaType.APPLICATION_JSON)
        )

        result.andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
