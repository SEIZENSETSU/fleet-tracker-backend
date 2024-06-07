package fleet.tracker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FleetTrackerApplication

fun main(args: Array<String>) {
    runApplication<FleetTrackerApplication>(*args)
}
