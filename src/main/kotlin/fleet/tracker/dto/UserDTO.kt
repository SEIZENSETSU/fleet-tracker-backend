package fleet.tracker.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDTO(
    @JsonProperty("uid")
    val uid: String,
    @JsonProperty("user_name")
    val userName: String,
)