package fleet.tracker.application_service.greeting

import fleet.tracker.infrastructure.greeting.GreetingRepository
import org.springframework.stereotype.Service

interface GreetingService {
    fun getGreeting(name: String): String
}

@Service
class GreetingServiceImpl(val greetingRepository: GreetingRepository): GreetingService {
    override fun getGreeting(name: String): String {
        return greetingRepository.getGreeting(name)
    }
}