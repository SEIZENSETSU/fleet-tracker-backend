package fleet.tracker.controller.user

import fleet.tracker.dto.UserDTO
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
@Sql("/user/Insert_User_Test_Data.sql")
class GetUserByIdTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should return user when user exists`() {
        val userDTO = UserDTO(
            uid = "test_user_1",
            userName = "test_user_1"
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.get("/user").param("uid", userDTO.uid))

        result.andExpect(status().isOk)
        result.andExpect(content().json("""
            {
                "uid": "${userDTO.uid}",
                "user_name": "${userDTO.userName}"
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 404 when user does not exist`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/user").param("uid", "non_existing_user"))

        result.andExpect(status().isNotFound)
    }

    @Test
    fun `should return 400 when uid is not provided`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/user"))

        result.andExpect(status().isBadRequest)
    }

    @Test
    fun `should return 400 when invalid parameter is provided`() {
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/user").param("invalid_param", "invalid_value"))

        result.andExpect(status().isBadRequest)
    }
}