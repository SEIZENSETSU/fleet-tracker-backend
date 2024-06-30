package fleet.tracker.controller.comment

import com.fasterxml.jackson.databind.ObjectMapper
import fleet.tracker.dto.CommentPostDTO
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/user/Insert_User_Test_Data.sql")
class PostCommentTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `valid comment DTO should return 201 Created`() {
        val commentPostDTO = CommentPostDTO(
            uid = "test_user_1",
            warehouseId = 1,
            contents = "Test comment"
        )

        val result = mockMvc.perform(
            post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentPostDTO))
        )

        result.andExpect(status().isCreated)
            .andExpect(content().json("""
                {
                    "uid": "test_user_1",
                    "warehouse_id": 1,
                    "contents": "Test comment"
                }
            """.trimIndent(), true))
    }

    @Test
    fun `create comment with non-existent warehouse should return 404 Not Found`() {
        val commentPostDTO = CommentPostDTO(
            uid = "test_user_1",
            warehouseId = 999,
            contents = "Test comment"
        )

        val result = mockMvc.perform(
            post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentPostDTO))
        )

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `create comment with non-existent user should return 404 Not Found`() {
        val commentPostDTO = CommentPostDTO(
            uid = "nonexistent_user",
            warehouseId = 1,
            contents = "Test comment"
        )

        val result = mockMvc.perform(
            post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentPostDTO))
        )

        result.andExpect(status().isNotFound)
    }
}
