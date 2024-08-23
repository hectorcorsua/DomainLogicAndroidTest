package com.example.domainlogicandroidtest.data.datasource

import com.example.domainlogicandroidtest.data.datasource.models.GitUserDetailDTO
import com.example.domainlogicandroidtest.data.datasource.models.ResponseDTO
import retrofit2.Response

interface GitHubUsersRemoteDataSource {
    fun searchUsers(query: String, resultsPerPage: Int): Response<ResponseDTO>

    fun getUserInfo(accountId: Int): Response<GitUserDetailDTO>
}