package fleet.tracker.controller.user

import fleet.tracker.application_service.user.UserService
import fleet.tracker.dto.UserDTO
import fleet.tracker.dto.CreateUserDTO
import fleet.tracker.model.User
import fleet.tracker.exeption.user.UserNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.dao.DataIntegrityViolationException

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

    @PostMapping("/user")
    fun createUser(@RequestBody createUserDTO: CreateUserDTO): ResponseEntity<*> {
        return try {
            if (!createUserDTO.isValid()) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request")
            }
            val createdUser = userService.createUser(createUserDTO)
            ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
            
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: DataIntegrityViolationException) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null)
        } 
    }

    @RestController
class UserController(val userService: UserService) {
    @DeleteMapping("/user")
    fun deleteUser(@RequestParam("uid") uid: String): ResponseEntity<Void> {
        return try {
            userService.deleteUserById(uid)
            ResponseEntity.noContent().build()
        } catch (e: UserNotFoundException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }
}
}