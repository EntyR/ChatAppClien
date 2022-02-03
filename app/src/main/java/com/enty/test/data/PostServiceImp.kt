package com.enty.test.data

import com.entyr.model.Post
import com.entyr.model.PostRequest
import com.entyr.model.PostResponse

class PostServiceImp(val ktor: Ktor): PostService {
    override suspend fun getAllPosts(token: String): List<PostResponse> {
        return ktor.getAllPosts(token)
    }

    override suspend fun getSubPosts(token: String): List<Post> {
        return ktor.getSubPosts(token)
    }

    override suspend fun createPost(token: String, post: PostRequest) {
        ktor.createPost(token, post)
    }
}