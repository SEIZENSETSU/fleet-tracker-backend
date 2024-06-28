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

    fun getWeight(): Int {
        return when (this) {
            Normal -> 1
            Pause -> 2
            HalfHour -> 3
            AnHour -> 4
            Impossible -> 5
        }
    }
}

fun getDelayStateByWeight(weight: Int): DelayState {
    return when (weight) {
        1 -> DelayState.Normal
        2 -> DelayState.Pause
        3 -> DelayState.HalfHour
        4 -> DelayState.AnHour
        5 -> DelayState.Impossible
        else -> throw IllegalArgumentException("Unexpected weight: $weight")
    }
}

