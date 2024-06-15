package fleet.tracker.exeption.database


class DatabaseException(message: String, cause: Throwable) : RuntimeException(message, cause)