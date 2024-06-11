package fleet.tracker.controller.greeting

import fleet.tracker.application_service.greeting.GreetingService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.mockito.Mockito.`when`

@WebMvcTest(GreetingController::class)
class GreetingControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var greetingService: GreetingService

    @Test
    fun `should return Hello, World!`() {
        // Arrange
        val expectedText = "Hello, World!"
        `when`(greetingService.getGreeting("World")).thenReturn(expectedText)

        // Act
        val result = mockMvc.perform(MockMvcRequestBuilders.get("/hello"))

        // Assert
        result.andExpect(status().isOk)
        result.andExpect(content().string(expectedText))
    }
}
