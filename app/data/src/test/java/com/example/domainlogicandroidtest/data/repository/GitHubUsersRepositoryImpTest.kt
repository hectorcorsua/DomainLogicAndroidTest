package com.example.domainlogicandroidtest.data.repository

import com.example.domainlogicandroidtest.data.datasource.GitHubUsersRemoteDataSource
import com.example.domainlogicandroidtest.data.datasource.models.GitUserDTO
import com.example.domainlogicandroidtest.data.datasource.models.ResponseDTO
import com.example.domainlogicandroidtest.domain.model.GitHubUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class GitHubUsersRepositoryImpTest {

    private lateinit var gitHubUsersRepository: GitHubUsersRepositoryImp
    private lateinit var gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource

    @Before
    fun setUp() {
        // Initialize mocks
        gitHubUsersRemoteDataSource = mockk()
        gitHubUsersRepository = GitHubUsersRepositoryImp(gitHubUsersRemoteDataSource)
    }

    @After
    fun tearDown() {
        // Clean up mocks
        unmockkAll()
    }

    @Test
    fun `getProductList returns success`() {
        // Given
        val userList = listOf(
            GitUserDTO(
                "1",
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
            GitUserDTO(
                "2",
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
        val expectedGitHubUsers = listOf(
            GitHubUser(
                "1",
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
                "2",
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
        val responseDto = ResponseDTO(2, false, userList)
        val response = Response.success(responseDto)

        // Mock behavior
        every { gitHubUsersRemoteDataSource.searchUsers(any(), any()) } returns response

        // When
        val result = gitHubUsersRepository.searchGitHubUsers("", 10)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedGitHubUsers, result.getOrNull())
    }

    @Test
    fun `searchGitHubUsers returns failure when response is unsuccessful`() {
        // Given
        val response = Response.error<ResponseDTO>(404, mockk(relaxed = true))

        // Mock behavior
        every { gitHubUsersRemoteDataSource.searchUsers(any(), any()) } returns response

        // When
        val result = gitHubUsersRepository.searchGitHubUsers("", 10)

        // Then
        assertTrue(result.isFailure)
    }

}
