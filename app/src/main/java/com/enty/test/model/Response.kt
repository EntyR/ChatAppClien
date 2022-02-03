package com.enty.test.model

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val isSuccessful: Boolean,
    val message: String

)
