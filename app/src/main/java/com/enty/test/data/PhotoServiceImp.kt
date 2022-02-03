package com.enty.test.data

import java.io.File

class PhotoServiceImp(val ktor: Ktor): PhotoService {

    override suspend fun addPhoto(file: ByteArray, token: String): String {
        return ktor.uploadPhoto(file, token)
    }
}