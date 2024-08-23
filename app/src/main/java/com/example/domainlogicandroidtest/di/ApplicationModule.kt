package com.example.domainlogicandroidtest.di

import com.example.domainlogicandroidtest.data.api.GitUsersAPI
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule{

    @Singleton
    @Provides
    fun getApi() : GitUsersAPI {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder : Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(
                OkHttpClient().newBuilder()
                    .addInterceptor(httpLoggingInterceptor)
                    .build())
            .build()
        return builder.create(GitUsersAPI::class.java)
    }
}