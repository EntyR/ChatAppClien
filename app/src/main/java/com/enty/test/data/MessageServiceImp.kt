package com.enty.test.data

import com.enty.test.utils.Resource
import com.entyr.model.AddMessageRequest
import com.entyr.model.LastMessageModel
import com.entyr.model.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class MessageServiceImp(val ktor: Ktor): MessageService{
     override suspend fun getAllMessage(token: String, user: String): List<Message> {
         return ktor.getAllMessages(token, user)
     }

    override suspend fun observeMessage(token: String, user: String): Flow<Message> {
        return ktor.observeMessages()
    }

    override suspend fun sendMessage(token: String, message: AddMessageRequest) {
         ktor.sendMessage(message)
     }

     override suspend fun initSession(token: String): Resource<Unit> {
         return ktor.initSession(token)
     }

    override suspend fun getLastMessages(token: String): List<LastMessageModel> {
        return ktor.getLastMessages(token)
    }

    override suspend fun closeSession() {
        ktor.closeSession()
    }
}
