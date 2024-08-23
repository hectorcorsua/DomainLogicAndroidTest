package com.example.domainlogicandroidtest.domain.usecase

import com.example.domainlogicandroidtest.domain.core.FlowUseCase
import com.example.domainlogicandroidtest.domain.model.GitHubUser
import com.example.domainlogicandroidtest.domain.repository.GitHubUsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchGitHubUsersUseCase @Inject constructor(
    private val repository: GitHubUsersRepository
) : FlowUseCase<SearchGitHubUsersUseCase.Input, SearchGitHubUsersUseCase.Output>() {

    data class Input(val query: String, val resultsPerPage: Int)

    sealed class Output{
        data class Success(val gitHubUsers: List<GitHubUser>) : Output()
        sealed class Error : Output(){
            data class Error(val codeError: String) : Output.Error()
            data object Unauthorized : Output.Error()
            data object UnknownError : Output.Error()
        }
    }

    override fun executeIOFlow(input: Input): Flow<Output> = flow{
        repository.searchGitHubUsers(input.query, input.resultsPerPage)
            .onSuccess {
                emit(Output.Success(it))
            }
            .onFailure { error ->
                emit(error.message?.let {
                    if (it == "401") {
                        Output.Error.Unauthorized
                    } else {
                        Output.Error.Error(it)
                    }
                } ?: Output.Error.UnknownError
                )
            }
    }
}