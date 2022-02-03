package com.entyr.model

import kotlinx.serialization.Serializable

@Serializable
data class PostRequest(
    val url: String,
    val date: Long,
    val text: String,
    val title: String,
    val where: String,
    val xCord: Long,
    val yCord: Long
)
