package com.enty.test.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatItem(
    val sendFrom: String,
    val text: String,
    val date: Long,

)
