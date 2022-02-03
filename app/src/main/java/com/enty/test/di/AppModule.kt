package com.enty.test.di

import android.content.Context
import com.enty.test.data.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import io.ktor.client.request.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClient() = HttpClient(CIO) {

        install(WebSockets)
        install(Logging)
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    @Provides
    @Singleton
    fun provideKtor(httpClient: HttpClient): Ktor {
        return Ktor(httpClient)
    }

    @Provides
    @Singleton
    fun provideMessageService(ktor: Ktor): MessageService {
        return MessageServiceImp(ktor)
    }

    @Provides
    @Singleton
    fun provideUserService(ktor: Ktor, @ApplicationContext context: Context): UsersService {
        return UsersServiceImp(ktor, context)
    }

    @Provides
    @Singleton
    fun providePostService(ktor: Ktor): PostService {
        return PostServiceImp(ktor)
    }
    @Provides
    @Singleton
    fun providePhotoService(ktor: Ktor): PhotoService {
        return PhotoServiceImp(ktor)
    }

    @Provides
    @Singleton
    fun provideContactsService(ktor: Ktor): ContactsService {
        return ContactsServiceImp(ktor)
    }




}