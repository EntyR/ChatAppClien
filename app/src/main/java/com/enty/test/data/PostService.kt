package com.enty.test.data

import com.entyr.model.Post
import com.entyr.model.PostRequest
import com.entyr.model.PostResponse

interface PostService {

    suspend fun getAllPosts(token: String): List<PostResponse>

    suspend fun getSubPosts(token: String): List<Post>

    suspend fun createPost(token: String, post: PostRequest)
}