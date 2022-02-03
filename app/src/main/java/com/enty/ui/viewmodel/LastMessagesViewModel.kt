package com.enty.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enty.test.data.ContactsService
import com.enty.test.data.MessageService
import com.entyr.model.LastMessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LastMessagesViewModel @Inject constructor(
    val postService: MessageService,
    val contactsService: ContactsService,
) : ViewModel() {


    private val _post = MutableLiveData<List<LastMessageModel>>()
    val messages: LiveData<List<LastMessageModel>> = _post


    fun getAllContact(token: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                postService.getLastMessages(token)
            }
            Log.e("TAG", "getAllContact: ${result.size}")
            _post.value = result
        }
    }


}