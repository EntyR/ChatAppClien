package com.enty.test.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseX(
    val isSuccessful: Boolean,
    val message: String
)