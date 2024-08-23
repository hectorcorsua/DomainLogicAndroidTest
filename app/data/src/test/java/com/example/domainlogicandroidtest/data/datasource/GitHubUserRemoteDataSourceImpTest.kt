package com.example.domainlogicandroidtest.data.datasource

import com.example.domainlogicandroidtest.data.api.GitUsersAPI
import com.example.domainlogicandroidtest.data.datasource.models.GitUserDTO
import com.example.domainlogicandroidtest.data.datasource.models.ResponseDTO
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Response

class GitHubUserRemoteDataSourceImpTest {

    private lateinit var api: GitUsersAPI
    private lateinit var dataSource: GitHubUsersRemoteDataSource

    @Before
    fun setUp() {
        // Initialize mocks
        api = mockk()
        dataSource = GitHubUsersRemoteDataSourceImp(api)
    }

    @After
    fun tearDown() {
        // Clean up mocks
        clearAllMocks()
    }

    @Test
    fun `test searchUsers success`() {
        // Given
        val mockedGitUserDTO = GitUserDTO(
            login = "login",
            id = 0,
            nodeId = "nodeId",
            avatarUrl = "avatarUrl",
            gravatarId = "gravatarId",
            url = "url",
            htmlUrl = "htmlUrl",
            followersUrl = "followersUrl",
            followingUrl = "followingUrl",
            gistsUrl = "gistsUrl",
            starredUrl = "starredUrl",
            subscriptionsUrl = "subscriptionsUrl",
            organizationsUrl = "organizationsUrl",
            reposUrl = "reposUrl",
            eventsUrl = "eventsUrl",
            receivedEventsUrl = "receivedEventsUrl",
            type = "type",
            siteAdmin = false,
            score = 0
        )
        val mockedProductList = listOf(mockedGitUserDTO, mockedGitUserDTO)
        val mockedResponseDTO = ResponseDTO(
            totalCount = 1,
            incompleteResults = false,
            items = mockedProductList
        )
        val expectedResponse = Response.success(mockedResponseDTO)
        val call = mockk<Call<ResponseDTO>>()

        // Mock behavior
        every { api.searchUsers(any(), any()) } returns call
        every { call.execute() } returns expectedResponse

        // When
        val response = dataSource.searchUsers("",10)

        // Then
        assert(response.isSuccessful)
        assertEquals(mockedResponseDTO, response.body())
        verify(exactly = 1) { api.searchUsers(any(), any()) }
    }

    @Test
    fun `test searchUsers error`() {
        // Given
        val errorResponseBody = "Error".toResponseBody("text/plain".toMediaTypeOrNull())
        val expectedResponse = Response.error<ResponseDTO>(400, errorResponseBody)
        val call = mockk<Call<ResponseDTO>>()

        // Mock behavior
        every { api.searchUsers(any(), any()) } returns call
        every { call.execute() } returns expectedResponse

        // When
        val response = dataSource.searchUsers("", 10)

        // Then
        assert(!response.isSuccessful)
        assertEquals(expectedResponse, response)
        verify(exactly = 1) { api.searchUsers(any(), any()) }
    }

    @Test(expected = Exception::class)
    fun `test searchUsers exception`() {
        // Mock behavior
        coEvery { api.searchUsers(any(), any()) } throws Exception()

        // When
        dataSource.searchUsers("", 10)

        // Then
        verify(exactly = 1) { api.searchUsers(any(), any()) }
    }
}