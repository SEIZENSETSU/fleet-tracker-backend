package fleet.tracker.controller.greeting

import fleet.tracker.application_service.greeting.GreetingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GreetingController(val greetingService: GreetingService) {
    @GetMapping("/hello")
    fun hello(): String {
        return greetingService.getGreeting("World")
    }
}