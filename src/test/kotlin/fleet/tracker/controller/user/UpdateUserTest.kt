package fleet.tracker.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import fleet.tracker.dto.UpdateUserDTO
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
class UserControllerUpdateTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should update user successfully`() {
        val uid = "test_user_1"
        val updateUserDTO = UpdateUserDTO(
            uid = uid,
            userName = "updated user",
            fcmTokenId = "updated_fcm_token"
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.put("/user")
            .param("uid", uid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateUserDTO)))

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("""
                {
                    "user_name": "${updateUserDTO.userName}",
                    "fcm_token_id": "${updateUserDTO.fcmTokenId}"
                }
            """.trimIndent()))
    }

    @Test
    fun `should update user successfully when user_name is not provided`() {
        val uid = "test_user_1"
        val updateUserDTO = UpdateUserDTO(
            uid = uid,
            userName = null,
            fcmTokenId = "updated_fcm_token"
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.put("/user")
            .param("uid", uid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateUserDTO)))

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("""
                {
                    "fcm_token_id": "${updateUserDTO.fcmTokenId}"
                }
            """.trimIndent()))
    }

    @Test
    fun `should update user successfully when fcm_token_id is not provided`() {
        val uid = "test_user_1"
        val updateUserDTO = UpdateUserDTO(
            uid = uid,
            userName = "updated_user_name",
            fcmTokenId = null
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.put("/user")
            .param("uid", uid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateUserDTO)))

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().json("""
                {
                    "user_name": "${updateUserDTO.userName}"
                }
            """.trimIndent()))
    }

    @Test
    fun `should return 404 when updating non-existing user`() {
        val uid = "non_existing_user"
        val updateUserDTO = UpdateUserDTO(
            uid = uid,
            userName = "updated user",
            fcmTokenId = "updated_fcm_token"
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.put("/user")
            .param("uid", uid)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateUserDTO)))

        result.andExpect(MockMvcResultMatchers.status().isNotFound)
    }
}
