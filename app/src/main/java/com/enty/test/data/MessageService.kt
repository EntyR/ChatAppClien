package com.enty.test.data

import com.enty.test.utils.Resource
import com.entyr.model.AddMessageRequest
import com.entyr.model.LastMessageModel
import com.entyr.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageService {
    suspend fun getAllMessage(token: String, user: String): List<Message>

    suspend fun observeMessage(token: String, user: String): Flow<Message>

    suspend fun sendMessage(token: String, message: AddMessageRequest)

    suspend fun initSession(token: String): Resource<Unit>

    suspend fun getLastMessages(token: String): List<LastMessageModel>

    suspend fun closeSession()
}