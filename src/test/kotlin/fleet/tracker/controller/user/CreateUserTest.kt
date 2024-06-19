package fleet.tracker.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import fleet.tracker.dto.CreateUserDTO
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql("/user/Insert_User_Test_Data.sql")
class UserControllerPostTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create user`() {
        val createUserDTO = CreateUserDTO(
            uid = "test_user_2",
            userName = "test_user_2",
            fcmTokenId = "test_user_2"
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createUserDTO)))

        result.andExpect(status().isCreated)
        result.andExpect(content().json("""
            {
                "uid": "${createUserDTO.uid}",
                "user_name": "${createUserDTO.userName}",
                "fcm_token_id": "${createUserDTO.fcmTokenId}"
            }
        """.trimIndent()))
    }

    @Test
    fun `should return 400 when createUserDTO is invalid`() {
        val invalidCreateUserDTO = CreateUserDTO(
            uid = "",
            userName = "",
            fcmTokenId = ""
        )

    val result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(invalidCreateUserDTO)))

    result.andExpect(MockMvcResultMatchers.status().isBadRequest)
}

    @Test
    fun `should return 500 when database error occurs`() {
        val createUserDTO = CreateUserDTO(
            uid = "test_user_1",
            userName = "test_user_1",
            fcmTokenId = "test_user_1"
        )

        val result = mockMvc.perform(MockMvcRequestBuilders.post("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createUserDTO)))

        result.andExpect(status().isInternalServerError)
    }
}
