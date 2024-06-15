package fleet.tracker.model

data class User(
    val uid: String,
    val userName: String,
    val fcmTokenId: String
)