package fleet.tracker.controller.user

import fleet.tracker.application_service.user.UserService
import fleet.tracker.dto.UserDTO
import fleet.tracker.exeption.user.UserNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(val userService: UserService) {
    @GetMapping("/user")
    fun getUserById(@RequestParam("uid") uid: String): ResponseEntity<UserDTO> {
        return try {
            val user = userService.getUserById(uid)
            ResponseEntity.ok(user)
        } catch (e: UserNotFoundException) {
            ResponseEntity.notFound().build()
        } catch (e: Exception) {
            ResponseEntity.internalServerError().build()
        }
    }
}