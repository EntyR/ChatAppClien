package com.enty.test.data

import java.io.File

interface PhotoService {

    suspend fun addPhoto(file: ByteArray, token: String): String

}