package com.example.domainlogicandroidtest.presentation.features.userdetail

import androidx.annotation.StringRes
import com.example.domainlogicandroidtest.domain.model.GitHubUserDetail

sealed class UserDetailsInfoUIState {
    data object Idle : UserDetailsInfoUIState()
    data object Loading : UserDetailsInfoUIState()
    data class ShowUseInfo(val gitHubUser: GitHubUserDetail) : UserDetailsInfoUIState()
}

sealed class UserDetailsErrorUIState {
    data object Idle : UserDetailsErrorUIState()
    data class ShowUserDetailsError(val errorState: ErrorState) : UserDetailsErrorUIState()
}

sealed class ErrorState(val errorType: ErrorType) {

    class UnauthorizedError(errorType: ErrorType.Cancel) : ErrorState(errorType)
    class GetUserError(errorType: ErrorType.Cancel) : ErrorState(errorType)
    class GetUserUnknownError(errorType: ErrorType.Cancel) : ErrorState(errorType)
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

sealed class GetUserDetailsOutput {
    data object Idle : GetUserDetailsOutput()
    data object Loading : GetUserDetailsOutput()
    data class Success(val userInfo: GitHubUserDetail) : GetUserDetailsOutput()
    data class Error(val errorCode: String) : GetUserDetailsOutput()
    data object UnknownError : GetUserDetailsOutput()
    data object Unauthorized : GetUserDetailsOutput()
}


//endregion