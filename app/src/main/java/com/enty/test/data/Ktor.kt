package com.enty.test.data

import android.util.Log
import com.enty.test.model.Response
import com.enty.test.utils.Resource
import com.entyr.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import kotlinx.serialization.encodeToString
import io.ktor.client.engine.android.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.util.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.isActive
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.lang.Exception
import java.util.logging.Logger



class Ktor(val client: HttpClient) {


    private var socket: WebSocketSession? = null



    suspend fun registerUser(user: RegisterRequest): Resource<String> {
        return try {
            val response: HttpResponse = client.post("http://mysterious-springs-65239.herokuapp.com/users/register")  {
                contentType(ContentType.Application.Json)

                body = user
            }
            val token = response.receive<Response>()
            Resource.Success(token.message)
        } catch (e: ClientRequestException){
            Resource.Error(null, message = "Email already Exist")
        } catch (e: Exception){
            Resource.Error(null, e.message?: "Some problem occurred")
        }
    }

    suspend fun loginUser(user: LoginRequest): Resource<String> {
        return try {
            val response = client.post<HttpResponse>("http://mysterious-springs-65239.herokuapp.com/users/login") {
                contentType(ContentType.Application.Json)

                body = user
            }
            val token = response.receive<Response>()
            Resource.Success(token.message)
        } catch (e: ClientRequestException){
            Resource.Error(null, message = "Incorrect Email")
        } catch (e: Exception){
            Resource.Error(null, e.message?: "Some problem occurred")
        }





    }

    suspend fun getAllUser(token: String): List<UserInfo> {
        val users = client.get<List<UserInfo>>("http://mysterious-springs-65239.herokuapp.com/user-list") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        return users
    }

    suspend fun getAllUserByName(token: String, name: String): List<UserInfo> {
        val posts = client.get<List<UserInfo>>("http://mysterious-springs-65239.herokuapp.com/userbyname"){
            headers {
                append(HttpHeaders.Authorization, token)
            }
            body = name
        }
        return posts
    }

    suspend fun getUserInfo(token: String, email: String): UserInfo {
        val posts = client.get<UserInfo>("http://mysterious-springs-65239.herokuapp.com/userinfo"){
            headers {
                append(HttpHeaders.Authorization, token)
            }
            body = email
        }
        return posts
    }


    suspend fun getAllPosts(token: String): List<PostResponse> {
        return client.get<List<PostResponse>>("http://mysterious-springs-65239.herokuapp.com/get-posts") {
                headers {
                    append(HttpHeaders.Authorization, token)
                }
                }



        }


    suspend fun getSubPosts(token: String): List<Post> {
        return client.get<List<Post>>() {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }

    }

    suspend fun createPost(token: String, post: PostRequest) {
        client.post<HttpResponse>("http://mysterious-springs-65239.herokuapp.com/create-post") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, token)
            }
            body = post
        }
    }


    suspend fun initSession(token: String): Resource<Unit> {
        return try {
            socket = client.webSocketSession {
                url("ws://mysterious-springs-65239.herokuapp.com/chat-socket")
                headers {
                    append(HttpHeaders.Authorization, token)
                }

            }
            if (socket?.isActive == true) {
                Resource.Success(Unit)
            } else Resource.Error(null,"Couldn't establish a connection.")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(null, e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun sendMessage(message: AddMessageRequest) {
        try {
            socket?.send(Frame.Text(Json.encodeToString(message)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun observeMessages(): Flow<Message> {
        return try {
            socket?.incoming
                ?.receiveAsFlow()
                ?.filter { it is Frame.Text }
                ?.map {
                    val json = (it as? Frame.Text)?.readText() ?: ""
                    val messageDto = Json.decodeFromString<Message>(json)
                    messageDto
                } ?: flow { }
        } catch (e: Exception) {
            e.printStackTrace()
            flow { }
        }
    }

    suspend fun closeSession() {
        socket?.close()
    }

    suspend fun getAllMessages(token: String, userName: String): List<Message> {
        return client.get<List<Message>>("http://mysterious-springs-65239.herokuapp.com/user-messages-fromuser") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
            body = userName
        }
    }

    suspend fun getLastMessages(token: String): List<LastMessageModel> {
        return client.get<List<LastMessageModel>>("http://mysterious-springs-65239.herokuapp.com/user-messages-list") {
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
    }

    suspend fun uploadPhoto(file: ByteArray, token: String): String {
        val responce:HttpResponse = client.submitFormWithBinaryData(
            formData = formData {
                append("description", generateNonce())
                append("image", file, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=ktor_logo.png")
                })
            }, url = "http://mysterious-springs-65239.herokuapp.com/upload-image"
        ){
            headers {
                append(HttpHeaders.Authorization, token)
            }
        }
        Log.e("Response", "uploadPhoto: ${responce.status}", )

        val url = responce.receive<Response>().message
        return url
    }

    suspend fun updateUser(token: String, userInfo: UserInfo) {
        client.post<HttpResponse>("http://mysterious-springs-65239.herokuapp.com/update-user") {
            contentType(ContentType.Application.Json)
            headers {
                append(HttpHeaders.Authorization, token)
            }
            body = userInfo
        }
    }



}