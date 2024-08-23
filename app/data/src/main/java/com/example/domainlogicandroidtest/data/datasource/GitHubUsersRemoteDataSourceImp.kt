package com.example.domainlogicandroidtest.data.datasource

import com.example.domainlogicandroidtest.data.api.GitUsersAPI
import com.example.domainlogicandroidtest.data.datasource.models.GitUserDetailDTO
import com.example.domainlogicandroidtest.data.datasource.models.ResponseDTO
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubUsersRemoteDataSourceImp @Inject constructor(private val gitUsersAPI: GitUsersAPI) :
    GitHubUsersRemoteDataSource {

    override fun searchUsers(query: String, resultsPerPage: Int): Response<ResponseDTO> {
        return gitUsersAPI.searchUsers(query, resultsPerPage).execute()
    }

    override fun getUserInfo(accountId: Int): Response<GitUserDetailDTO> {
        return gitUsersAPI.getUserInfo(accountId).execute()
    }
}