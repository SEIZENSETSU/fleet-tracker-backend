package fleet.tracker.model
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class DelayState(private val value: String) {
    Normal("normal"),
    Pause("pause"),
    HalfHour("halfHour"),
    AnHour("anHour"),
    Impossible("impossible");

    @JsonValue
    fun toValue(): String {
        return value
    }

    companion object {
        @JsonCreator
        @JvmStatic
        fun fromValue(value: String): DelayState {
            return entries.first { it.value == value }
        }
    }
}
