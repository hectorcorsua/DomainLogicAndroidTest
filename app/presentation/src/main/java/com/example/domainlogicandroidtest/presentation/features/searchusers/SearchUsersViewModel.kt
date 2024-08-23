package com.example.domainlogicandroidtest.presentation.features.searchusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domainlogicandroidtest.R
import com.example.domainlogicandroidtest.domain.usecase.SearchGitHubUsersUseCase
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
class SearchUsersViewModel @Inject constructor(
    private val searchGitHubUsersUseCase: SearchGitHubUsersUseCase
) : ViewModel() {

    private val _searchUsersInfoState = MutableStateFlow<SearchUsersInfoUIState>(
        SearchUsersInfoUIState.Idle
    )
    val searchUsersInfoState: StateFlow<SearchUsersInfoUIState> = _searchUsersInfoState

    private val _searchUsersErrorState = MutableStateFlow<SearchUsersErrorUIState>(
        SearchUsersErrorUIState.Idle
    )
    val searchUsersErrorState: StateFlow<SearchUsersErrorUIState> = _searchUsersErrorState

    private var searchUsersJob: Job? = null

    fun stopJobs() {
        searchUsersJob?.cancel()
        searchUsersJob = null
    }

    fun checkSearchRequirements(search : String){
        if(search.isEmpty()){
            _searchUsersInfoState.update { SearchUsersInfoUIState.EmptySearch}
        } else if(search.count() < 4){
            _searchUsersInfoState.update { SearchUsersInfoUIState.InvalidMinLengthCharacters }
        } else if(search.count() > 20){
            _searchUsersInfoState.update { SearchUsersInfoUIState.InvalidMaxLengthCharacters }
        } else {
            _searchUsersInfoState.update { SearchUsersInfoUIState.EnableSearchButton }
        }
    }

    fun searchGitHubUsers(query: String) {
        searchUsersJob = viewModelScope.launch {
            searchGitHubUsersUseCase.prepare(SearchGitHubUsersUseCase.Input(query,10))
                .transform { response ->
                    val state: SearchGitHubUserOutput = when (response) {
                        SearchGitHubUsersUseCase.Output.Error.UnknownError ->
                            SearchGitHubUserOutput.UnknownError

                        SearchGitHubUsersUseCase.Output.Error.Unauthorized ->
                            SearchGitHubUserOutput.Unauthorized

                        is SearchGitHubUsersUseCase.Output.Success ->
                            SearchGitHubUserOutput.Success(response.gitHubUsers)

                        is SearchGitHubUsersUseCase.Output.Error.Error -> SearchGitHubUserOutput.Error(response.codeError)
                    }
                    emit(state)
                }
                .onStart {
                    emit(SearchGitHubUserOutput.Loading)
                }
                .catch {
                    emit(SearchGitHubUserOutput.UnknownError)
                }
                .collect { response ->
                    when (response) {
                        SearchGitHubUserOutput.Idle -> {}
                        is SearchGitHubUserOutput.Error -> {
                            _searchUsersErrorState.value = SearchUsersErrorUIState.ShowSearchUsersError(
                                ErrorState.SearchUsersError(
                                    ErrorType.Cancel(
                                        R.string.error_title,
                                        R.string.search_user_code_error,
                                        arrayOf(response.errorCode)
                                    )
                                )
                            )
                        }
                        SearchGitHubUserOutput.UnknownError -> {
                            _searchUsersErrorState.value = SearchUsersErrorUIState.ShowSearchUsersError(
                                ErrorState.SearchUsersUnknownError(
                                    ErrorType.Cancel(
                                        R.string.error_title,
                                        R.string.search_user_unknown_error,
                                        null
                                    )
                                )
                            )
                        }

                        SearchGitHubUserOutput.Loading -> {
                            _searchUsersInfoState.value = SearchUsersInfoUIState.Loading
                        }

                        is SearchGitHubUserOutput.Success -> {
                            if (response.gitHubUserList.isEmpty()) {
                                _searchUsersInfoState.value = SearchUsersInfoUIState.UserListEmpty
                            } else {
                                _searchUsersInfoState.value =
                                    SearchUsersInfoUIState.ShowUserList(response.gitHubUserList)
                            }
                        }

                        SearchGitHubUserOutput.Unauthorized -> {
                            _searchUsersErrorState.value = SearchUsersErrorUIState.ShowSearchUsersError(
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

    fun processSearchUsersErrorState() {
        _searchUsersErrorState.update { SearchUsersErrorUIState.Idle }
    }

    fun processSearchUsersInfoState() {
        _searchUsersInfoState.update { SearchUsersInfoUIState.Idle }
    }
}