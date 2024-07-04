package fleet.tracker.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun convertUtcTokyoDateTime(utcDateTime: LocalDateTime): LocalDateTime {
    val utcZoneId = ZoneId.of("UTC")
    val tokyoZoneId = ZoneId.of("Asia/Tokyo")
    val utcZonedDateTime = ZonedDateTime.of(utcDateTime, utcZoneId)
    val tokyoZonedDateTime = utcZonedDateTime.withZoneSameInstant(tokyoZoneId)
    return tokyoZonedDateTime.toLocalDateTime()
}