package com.enty.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.enty.test.data.ContactsService
import com.enty.test.data.PostService
import com.enty.test.utils.getUser
import com.entyr.model.Message
import com.entyr.model.Post
import com.entyr.model.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ContactViewModel @Inject constructor(
    val postService: ContactsService,
    val context: Application
): AndroidViewModel(context) {


    private val _post = MutableLiveData<List<UserInfo>>()
    val messages: LiveData<List<UserInfo>> = _post

    fun getAllContact(token: String){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                postService.getAllUsers(token)
            }

            Log.e("TAG", "getAllContact: ${result.size}")
            _post.value = result
        }
    }
    fun getUserByName(token: String){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                postService.getAllUsersByName(token, "")
            }
            Log.e("TAG", "getAllContact: ${result.size}")
            _post.value = result
        }
    }



}