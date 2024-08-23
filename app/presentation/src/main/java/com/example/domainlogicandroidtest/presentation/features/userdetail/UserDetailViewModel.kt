package com.example.domainlogicandroidtest.presentation.features.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domainlogicandroidtest.R
import com.example.domainlogicandroidtest.domain.usecase.GetGitHubUserDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val getUserDetailUseCase: GetGitHubUserDetailUseCase
) : ViewModel() {

    private val _userDetailsInfoState = MutableStateFlow<UserDetailsInfoUIState>(
        UserDetailsInfoUIState.Idle
    )
    val userDetailsInfoState: StateFlow<UserDetailsInfoUIState> = _userDetailsInfoState

    private val _userDetailsErrorState = MutableStateFlow<UserDetailsErrorUIState>(
        UserDetailsErrorUIState.Idle
    )
    val userDetailsErrorState: StateFlow<UserDetailsErrorUIState> = _userDetailsErrorState

    private var getUserInfoJob: Job? = null

    fun stopJobs() {
        getUserInfoJob?.cancel()
        getUserInfoJob = null
    }

    fun getGitHubUserDetail(accountId: Int) {
        getUserInfoJob = viewModelScope.launch {
            getUserDetailUseCase.prepare(GetGitHubUserDetailUseCase.Input(accountId))
                .transform { response ->
                    val state: GetUserDetailsOutput = when (response) {
                        is GetGitHubUserDetailUseCase.Output.Success -> {
                            GetUserDetailsOutput.Success(response.gitHubUser)
                        }

                        is GetGitHubUserDetailUseCase.Output.Error.Error ->
                            GetUserDetailsOutput.Error(response.codeError)

                        GetGitHubUserDetailUseCase.Output.Error.Unauthorized ->
                            GetUserDetailsOutput.Unauthorized

                        GetGitHubUserDetailUseCase.Output.Error.UnknownError ->
                            GetUserDetailsOutput.UnknownError
                    }
                    emit(state)
                }
                .onStart {
                    emit(GetUserDetailsOutput.Loading)
                }
                .catch {
                    emit(GetUserDetailsOutput.UnknownError)
                }
                .collect { response ->
                    when (response) {
                        GetUserDetailsOutput.Idle -> {}
                        is GetUserDetailsOutput.Error -> {
                            _userDetailsErrorState.value =
                                UserDetailsErrorUIState.ShowUserDetailsError(
                                    ErrorState.GetUserError(
                                        ErrorType.Cancel(
                                            R.string.error_title,
                                            R.string.get_user_info_code_error,
                                            arrayOf(response.errorCode)
                                        )
                                    )
                                )
                        }

                        GetUserDetailsOutput.UnknownError -> {
                            _userDetailsErrorState.value =
                                UserDetailsErrorUIState.ShowUserDetailsError(
                                    ErrorState.GetUserUnknownError(
                                        ErrorType.Cancel(
                                            R.string.error_title,
                                            R.string.get_user_info_unknown_error,
                                            null
                                        )
                                    )
                                )
                        }

                        GetUserDetailsOutput.Loading -> {
                            _userDetailsInfoState.value = UserDetailsInfoUIState.Loading
                        }

                        is GetUserDetailsOutput.Success -> {
                            _userDetailsInfoState.value =
                                UserDetailsInfoUIState.ShowUseInfo(response.userInfo)
                        }

                        GetUserDetailsOutput.Unauthorized -> {
                            _userDetailsErrorState.value =
                                UserDetailsErrorUIState.ShowUserDetailsError(
                                    ErrorState.UnauthorizedError(
                                        ErrorType.Cancel(
                                            R.string.error_title,
                                            R.string.unauthorized_error,
                                            null
                                        )
                                    )
                                )
                        }
                    }
                }

        }
    }

    fun processUserDetailsErrorState() {
        _userDetailsErrorState.update { UserDetailsErrorUIState.Idle }
    }

    fun processUserDetailsInfoState() {
        _userDetailsInfoState.update { UserDetailsInfoUIState.Idle }
    }
}