package com.entyr.model

import kotlinx.serialization.Serializable

@Serializable
data class AddMessageRequest(
    val sendToUser: String,
    val text: String,
    val timestamp: Long,
    val xCord: Long,
    val yCord: Long,
    val url: String = "",
    var reed: Boolean
)