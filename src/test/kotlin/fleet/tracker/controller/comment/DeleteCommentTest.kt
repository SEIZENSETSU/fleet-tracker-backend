package fleet.tracker.controller.comment

import com.fasterxml.jackson.databind.ObjectMapper
import fleet.tracker.exeption.comment.CommentNotFoundException
import fleet.tracker.service.CommentService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import org.mockito.Mockito.`when`

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/user/Insert_User_Test_Data.sql")
@Sql("/comment/Insert_Comment_Test_Data.sql")
class DeleteCommentTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var commentService: CommentService

    @Test
    fun `deleteComment should return no content when comment is deleted`() {
        val commentId = 1

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment")
            .param("comment_id", commentId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `deleteComment should return not found when comment is not found`() {
        val commentId = 999

        `when`(commentService.deleteCommentById(commentId))
            .thenThrow(CommentNotFoundException("Comment not found"))

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment")
            .param("comment_id", commentId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
