package com.entyr.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val email: String,
    val username: String,
    val profPic: String = "",
    val desc: String = ""
)
