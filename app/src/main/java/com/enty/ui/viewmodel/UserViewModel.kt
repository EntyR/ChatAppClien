package com.enty.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enty.test.data.UsersService
import com.enty.test.data.UsersServiceImp
import com.enty.test.utils.Resource
import com.entyr.model.LoginRequest
import com.entyr.model.RegisterRequest
import com.entyr.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class UserViewModel @Inject constructor(
    val userService: UsersService
): ViewModel() {

    private val _response = MutableLiveData<Resource<Unit>>()
    val response: LiveData<Resource<Unit>> = _response

    fun registry(registerRequest: RegisterRequest){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                userService.registerUser(registerRequest)
            }
            _response.value = result
        }
    }
    fun login(loginRequest: LoginRequest){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                userService.loginUser(loginRequest)
            }
            _response.value = result
        }





    }
    fun updateUser(token: String, userInfo: UserInfo){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                userService.updateUser(token, userInfo)
            }
        }
    }

}