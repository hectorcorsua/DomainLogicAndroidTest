package com.example.domainlogicandroidtest.data.repository

import com.example.domainlogicandroidtest.data.datasource.GitHubUsersRemoteDataSource
import com.example.domainlogicandroidtest.data.mapper.mapToDomain
import com.example.domainlogicandroidtest.domain.model.GitHubUser
import com.example.domainlogicandroidtest.domain.model.GitHubUserDetail
import com.example.domainlogicandroidtest.domain.repository.GitHubUsersRepository
import javax.inject.Inject

class GitHubUsersRepositoryImp @Inject constructor(
    private val gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource
) : GitHubUsersRepository {
    override fun searchGitHubUsers(query: String, resultsPerPage: Int): Result<List<GitHubUser>> {
        val result = gitHubUsersRemoteDataSource.searchUsers(query, resultsPerPage)
        val domainResult = result.body()?.items?.mapToDomain()
        return if (result.isSuccessful && domainResult != null) {
            Result.success(domainResult)
        } else {
            Result.failure(
                Throwable(
                    result.code().toString()
                )
            )
        }
    }

    override fun getGitHubUser(accountId: Int): Result<GitHubUserDetail> {
        val result = gitHubUsersRemoteDataSource.getUserInfo(accountId)
        val domainResult = result.body()?.mapToDomain()
        return if (result.isSuccessful && domainResult != null) {
            Result.success(domainResult)
        } else {
            Result.failure(
                Throwable(
                    result.code().toString()
                )
            )
        }
    }

}