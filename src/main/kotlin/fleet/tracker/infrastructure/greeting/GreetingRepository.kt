package fleet.tracker.infrastructure.greeting

import org.springframework.stereotype.Repository

interface GreetingRepository {
    fun getGreeting(name: String): String
}

@Repository
class GreetingRepositoryImpl: GreetingRepository {
    override fun getGreeting(name: String): String {
        return "Hello, $name!"
    }
}