package com.entyr.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val hashPassword: String,
    val username: String
)