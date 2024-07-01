package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateUserDTO(
    @JsonProperty("user_name", required = false)
    val userName: String?,
    @JsonProperty("fcm_token_id", required = false)
    val fcmTokenId: String?
)