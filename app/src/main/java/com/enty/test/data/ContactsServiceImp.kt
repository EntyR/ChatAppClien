package com.enty.test.data

import com.entyr.model.Message
import com.entyr.model.User
import com.entyr.model.UserInfo

class ContactsServiceImp(val ktor: Ktor): ContactsService {

    override suspend fun getAllUsers(token: String): List<UserInfo> {
        return ktor.getAllUser(token)
    }

    override suspend fun getAllUsersByName(token: String, name: String): List<UserInfo> {
        return ktor.getAllUserByName(token, name)
    }

    override suspend fun getUserInfo(token: String, email: String): UserInfo {
        return ktor.getUserInfo(token, email)
    }
}