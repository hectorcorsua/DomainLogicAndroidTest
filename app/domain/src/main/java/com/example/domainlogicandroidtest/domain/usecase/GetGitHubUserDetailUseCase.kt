package com.example.domainlogicandroidtest.domain.usecase

import com.example.domainlogicandroidtest.domain.core.FlowUseCase
import com.example.domainlogicandroidtest.domain.model.GitHubUserDetail
import com.example.domainlogicandroidtest.domain.repository.GitHubUsersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGitHubUserDetailUseCase @Inject constructor(
    private val repository: GitHubUsersRepository
) : FlowUseCase<GetGitHubUserDetailUseCase.Input, GetGitHubUserDetailUseCase.Output>() {

    data class Input(val accountId: Int)

    sealed class Output{
        data class Success(val gitHubUser: GitHubUserDetail) : Output()
        sealed class Error : Output(){
            data class Error(val codeError: String) : Output.Error()
            data object Unauthorized : Output.Error()
            data object UnknownError : Output.Error()
        }
    }

    override fun executeIOFlow(input: Input): Flow<Output> = flow{
        repository.getGitHubUser(input.accountId)
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