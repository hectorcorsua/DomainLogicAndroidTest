package com.example.domainlogicandroidtest.domain.usecase

import com.example.domainlogicandroidtest.domain.model.GitHubUser
import com.example.domainlogicandroidtest.domain.repository.GitHubUsersRepository
import com.example.domainlogicandroidtest.domain.usecase.SearchGitHubUsersUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class SearchGitHubUsersUseCaseTest {
    // Mocks
    private lateinit var repository: GitHubUsersRepository
    private lateinit var useCase: SearchGitHubUsersUseCase
    private lateinit var result: SearchGitHubUsersUseCase.Output

    @Before
    fun setup() {
        // Initialize mocks
        repository = mockk()
        useCase = SearchGitHubUsersUseCase(repository)
    }

    @After
    fun tearDown() {
        // Clean up mocks
        unmockkAll()
    }

    @Test
    fun `test getProductList repository returns products`() = runBlocking {
        // Given
        val input = SearchGitHubUsersUseCase.Input("test", 10)
        val gitHubUserLists = listOf(
            GitHubUser(
                "123",
                1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                0
            ),
            GitHubUser(
                "456",
                2,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                0
            )
        )

        // Mock behavior
        coEvery { repository.searchGitHubUsers(any(), any()) } returns Result.success(
            gitHubUserLists
        )

        // When
        useCase.prepare(input).collect { result = it }

        // Then
        assert(result is SearchGitHubUsersUseCase.Output.Success)
        val successResult = result as SearchGitHubUsersUseCase.Output.Success
        assert(successResult.gitHubUsers == gitHubUserLists)
        verify(exactly = 1) { repository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun `test getProductList repository returns 401 error`() = runBlocking {
        // Given
        val input = SearchGitHubUsersUseCase.Input("test", 10)
        val errorMessage = "401"

        // Mock behavior
        coEvery { repository.searchGitHubUsers(any(), any()) } returns Result.failure(
            Throwable(
                errorMessage
            )
        )

        // When
        useCase.prepare(input).collect { result = it }

        // Then
        assert(result is SearchGitHubUsersUseCase.Output.Error.Unauthorized)
        verify(exactly = 1) { repository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun `test getProductList repository returns other error`() = runBlocking {
        // Given
        val input = SearchGitHubUsersUseCase.Input("test", 10)
        val errorCode = "400"

        // Mock behavior
        coEvery { repository.searchGitHubUsers(any(), any()) } returns Result.failure(
            Throwable(
                errorCode
            )
        )

        // When
        useCase.prepare(input).collect { result = it }

        // Then
        assert(result is SearchGitHubUsersUseCase.Output.Error.Error)
        val errorResult = result as SearchGitHubUsersUseCase.Output.Error.Error
        assert(errorResult.codeError == errorCode)
        verify(exactly = 1) { repository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun `test getProductList repository returns null error message`() =
        runBlocking {
            val input = SearchGitHubUsersUseCase.Input("test", 10)
            // Mock behavior
            coEvery {
                repository.searchGitHubUsers(
                    any(),
                    any()
                )
            } returns Result.failure(Throwable())

            // When
            useCase.prepare(input).collect { result = it }

            // Then
            assert(result is SearchGitHubUsersUseCase.Output.Error.UnknownError)
            verify(exactly = 1) { repository.searchGitHubUsers(any(), any()) }
        }
}