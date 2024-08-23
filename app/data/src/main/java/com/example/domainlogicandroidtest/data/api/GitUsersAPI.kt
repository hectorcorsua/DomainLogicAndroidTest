package com.example.domainlogicandroidtest.data.api

import com.example.domainlogicandroidtest.data.datasource.models.GitUserDetailDTO
import com.example.domainlogicandroidtest.data.datasource.models.ResponseDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitUsersAPI {
    @GET("/search/users")
    fun searchUsers(
        @Query("q") query: String,
        @Query("per_page") resultsPerPage: Int
    ): Call<ResponseDTO>

    @GET("/user/{account_id}")
    fun getUserInfo(
        @Path("account_id") accountId: Int
    ): Call<GitUserDetailDTO>
}