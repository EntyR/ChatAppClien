package com.enty.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enty.test.data.PostService
import com.entyr.model.LoginRequest
import com.entyr.model.Post
import com.entyr.model.PostResponse
import com.entyr.model.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class PostsViewModel @Inject constructor(
    val postService: PostService
): ViewModel() {


    private val _post = MutableLiveData<List<PostResponse>>()
    val post: LiveData<List<PostResponse>> = _post

    fun getAllPost(token: String){
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                postService.getAllPosts(token)
            }
            _post.value = result
        }
    }
//    fun getSubbedPost(token: String){
//        viewModelScope.launch {
//            val result = withContext(Dispatchers.IO) {
//                postService.getSubPosts(token)
//            }
//            _post.value = result
//        }
//    }


}