package fleet.tracker.controller.user

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
class UserControllerDeleteTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should delete user successfully`() {
        val validUid = "test_user_1"

        val result = mockMvc.perform(MockMvcRequestBuilders.delete("/user")
            .param("uid", validUid))

        result.andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun `should return 404 when user is not found`() {
        val invalidUid = "non_existing_user"

        val result = mockMvc.perform(MockMvcRequestBuilders.delete("/user")
            .param("uid", invalidUid))

        result.andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
