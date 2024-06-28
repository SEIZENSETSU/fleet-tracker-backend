package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateUserDTO(
    @JsonProperty("uid", required = true)
    val uid: String,
    @JsonProperty("user_name")
    val userName: String?,
    @JsonProperty("fcm_token_id")
    val fcmTokenId: String?
)