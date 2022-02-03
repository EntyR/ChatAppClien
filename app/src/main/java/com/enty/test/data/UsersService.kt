package com.enty.test.data

import android.content.Context
import com.enty.test.utils.Resource
import com.entyr.model.LoginRequest
import com.entyr.model.RegisterRequest
import com.entyr.model.UserInfo
import io.ktor.client.*

interface UsersService {

    suspend fun registerUser( request: RegisterRequest): Resource<Unit>

    suspend fun loginUser(request: LoginRequest):Resource<Unit>

    suspend fun updateUser(token: String, request: UserInfo)


}