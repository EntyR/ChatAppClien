package com.enty.test.data

import android.os.Message
import com.entyr.model.UserInfo

interface ContactsService {
    suspend fun getAllUsers(token: String): List<UserInfo>

    suspend fun getAllUsersByName(token: String, name: String): List<UserInfo>

    suspend fun getUserInfo(token: String, email: String): UserInfo
}