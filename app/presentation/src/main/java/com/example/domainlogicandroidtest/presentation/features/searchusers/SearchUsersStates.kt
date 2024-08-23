package com.example.domainlogicandroidtest.presentation.features.searchusers

import androidx.annotation.StringRes
import com.example.domainlogicandroidtest.domain.model.GitHubUser

sealed class SearchUsersInfoUIState {
    data object Idle : SearchUsersInfoUIState()
    data object Loading : SearchUsersInfoUIState()
    data object UserListEmpty : SearchUsersInfoUIState()
    data object EmptySearch : SearchUsersInfoUIState()
    data object InvalidMinLengthCharacters : SearchUsersInfoUIState()
    data object InvalidMaxLengthCharacters : SearchUsersInfoUIState()
    data object EnableSearchButton : SearchUsersInfoUIState()
    data class ShowUserList(val gitHubUserList: List<GitHubUser>) : SearchUsersInfoUIState()
}

sealed class SearchUsersErrorUIState {
    data object Idle : SearchUsersErrorUIState()
    data class ShowSearchUsersError(val errorState: ErrorState) : SearchUsersErrorUIState()
}

sealed class ErrorState(val errorType: ErrorType) {

    class UnauthorizedError(errorType: ErrorType.Cancel) : ErrorState(errorType)
    class SearchUsersError(errorType: ErrorType.Cancel) : ErrorState(errorType)
    class SearchUsersUnknownError(errorType: ErrorType.Cancel) : ErrorState(errorType)
}

sealed class ErrorType(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val descriptionArguments: Array<out Any>? = null
) {
    class Retry(
        @StringRes title: Int,
        @StringRes description: Int,
        descriptionArguments: Array<out Any>?
    ) : ErrorType(title, description, descriptionArguments)

    class Cancel(
        @StringRes title: Int,
        @StringRes description: Int,
        descriptionArguments: Array<out Any>?
    ) : ErrorType(title, description, descriptionArguments)
}


//region Outputs states

sealed class SearchGitHubUserOutput {
    data object Idle : SearchGitHubUserOutput()
    data object Loading : SearchGitHubUserOutput()
    data class Success(val gitHubUserList: List<GitHubUser>) : SearchGitHubUserOutput()
    data class Error(val errorCode: String) : SearchGitHubUserOutput()
    data object UnknownError : SearchGitHubUserOutput()
    data object Unauthorized : SearchGitHubUserOutput()
}


//endregion