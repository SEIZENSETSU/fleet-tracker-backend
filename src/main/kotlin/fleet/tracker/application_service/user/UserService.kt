package fleet.tracker.application_service.user

import fleet.tracker.dto.UserDTO
import fleet.tracker.dto.CreateUserDTO
import fleet.tracker.model.User
import fleet.tracker.exeption.database.DatabaseException
import fleet.tracker.exeption.user.UserNotFoundException
import fleet.tracker.infrastructure.user.UserRepository
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service

interface UserService {
    fun getUserById(uid: String): UserDTO
    fun createUser(createUserDTO: CreateUserDTO): CreateUserDTO
}

@Service
class UserServiceImpl(val userRepository: UserRepository): UserService {
    override fun getUserById(uid: String): UserDTO {
        try {
            val user = userRepository.findByUserOrNull(uid) ?: throw UserNotFoundException("User not found")
            return UserDTO(
                uid = user.uid,
                userName = user.userName
            )
        } catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }

    override fun createUser(createUserDTO: CreateUserDTO): CreateUserDTO {
        try {
            val user = User(
                uid = createUserDTO.uid,
                userName = createUserDTO.userName,
                fcmTokenId = createUserDTO.fcmTokenId
            )
            userRepository.save(user)
            return CreateUserDTO(
                uid = user.uid,
                userName = user.userName,
                fcmTokenId = user.fcmTokenId
            )
        } catch (e: DataAccessException) {
            throw DatabaseException("Database error", e)
        }
    }
}