package com.example.domainlogicandroidtest.domain.repository

import com.example.domainlogicandroidtest.domain.model.GitHubUser
import com.example.domainlogicandroidtest.domain.model.GitHubUserDetail

interface GitHubUsersRepository {
    fun searchGitHubUsers(query: String, resultsPerPage: Int): Result<List<GitHubUser>>

    fun getGitHubUser(accountId: Int): Result<GitHubUserDetail>
}