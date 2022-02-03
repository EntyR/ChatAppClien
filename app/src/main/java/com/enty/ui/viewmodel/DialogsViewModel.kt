package com.enty.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enty.test.data.MessageService
import com.enty.test.data.PhotoService
import com.enty.test.utils.Resource
import com.entyr.model.AddMessageRequest
import com.entyr.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DialogsViewModel @Inject constructor(
    val messageService: MessageService,
    val photoService: PhotoService
) : ViewModel() {


    private val _post = MutableLiveData<List<Message>>()
    val post: LiveData<List<Message>> = _post


    fun connectToChat(token: String, user: String) {


        viewModelScope.launch {
            val result = messageService.initSession(token)
            getAllMessage(token, user)
            when (result) {
                is Resource.Success -> {
                    messageService.observeMessage(token, "")
                        .onEach { message ->
                            Log.e("TAG", "connectToChat: newMessage")
                            val currentMessage = post.value

                            val endList = currentMessage?.toMutableList() ?: mutableListOf()
                            endList.add(message)
                            Log.e("EndList", "connectToChat: $endList")
                            _post.value = endList.toList()
                        }.launchIn(viewModelScope)
                }
                is Resource.Error -> {

                }
            }
        }
    }



    fun sendMessage(message: AddMessageRequest, token: String) {
        viewModelScope.launch {
            messageService.sendMessage(token, message)

        }
    }

    fun getAllMessage(token: String, user: String) {
        viewModelScope.launch {
            val list = messageService.getAllMessage(token, user)
            _post.value = list
        }
    }

     fun clearSocket() {

        viewModelScope.launch {
            Log.e("Cleare", "onCleared: ", )
            messageService.closeSession()
        }
    }

}






