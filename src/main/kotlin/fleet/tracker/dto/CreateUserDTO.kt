package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateUserDTO(
    @JsonProperty("uid")
    val uid: String,
    @JsonProperty("user_name")
    val userName: String,
    @JsonProperty("fcm_token_id")
    val fcmTokenId: String
) {
    fun isValid(): Boolean {
        return !(uid.isNullOrBlank() || userName.isNullOrBlank() || fcmTokenId.isNullOrBlank())
    }
}