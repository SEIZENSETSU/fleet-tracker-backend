package fleet.tracker.controller.comment

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fleet.tracker.dto.CommentDTO
import org.junit.jupiter.api.Assertions.assertEquals
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
import java.time.LocalDateTime

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

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/comments")
            .param("warehouse_id", warehouseId.toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val content = result.response.contentAsString
        val actualResponse: List<CommentDTO> = objectMapper.readValue(content, object : TypeReference<List<CommentDTO>>() {})
        val expectedResponse = listOf(
            CommentDTO(commentId = 2, uid = "test_user_1", warehouseId = 1, contents = "test_comment_2", createdAt = LocalDateTime.of(2022, 7, 3, 0, 0)),
            CommentDTO(commentId = 1, uid = "test_user_1", warehouseId = 1, contents = "test_comment_1", createdAt = LocalDateTime.of(2022, 7, 2, 0, 0))
        )

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `should return 404 when no comments found for a given warehouse id`() {
        val warehouseId = 999

        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
            .param("warehouse_id", warehouseId.toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun `should return 404 when warehouse id is not valid`() {
        val warehouseId = -1

        mockMvc.perform(MockMvcRequestBuilders.get("/comments")
            .param("warehouse_id", warehouseId.toString())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
