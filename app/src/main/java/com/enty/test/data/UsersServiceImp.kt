package com.enty.test.data

import android.content.Context
import android.util.Log
import com.enty.test.APP_PREFERENCES
import com.enty.test.utils.Resource
import com.enty.test.utils.getToken
import com.enty.test.utils.putToken
import com.entyr.model.LoginRequest
import com.entyr.model.RegisterRequest
import com.entyr.model.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersServiceImp(val ktor: Ktor, val context: Context): UsersService {
    override suspend fun registerUser(request: RegisterRequest): Resource<Unit> {

        val resource = withContext(Dispatchers.IO) { ktor.registerUser(request) }
        Log.e("TAG", "registerUser: ${resource.data}", )
        return when(resource){
            is Resource.Success -> {
                putToken(context, "Bearer ${resource.data!! }")
                Resource.Success(null)
            }
            is Resource.Error -> {
                Resource.Error(null, resource.message?: "Unknown error occurred")
            }
        }

    }

    override suspend fun loginUser(request: LoginRequest): Resource<Unit> {
        val resource = withContext(Dispatchers.IO) { ktor.loginUser(request)}
        return when(resource){
            is Resource.Success -> {
                putToken(context, "Bearer ${resource.data!!}")
                Resource.Success(null)
            }
            is Resource.Error -> {
                Resource.Error(null, resource.message?: "Unknown error occurred")
            }
        }
    }

    override suspend fun updateUser(token: String, request: UserInfo) {
        return ktor.updateUser(token, request)
    }
}