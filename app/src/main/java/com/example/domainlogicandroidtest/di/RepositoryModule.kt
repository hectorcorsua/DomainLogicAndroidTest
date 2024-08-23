package com.example.domainlogicandroidtest.di

import com.example.domainlogicandroidtest.data.api.GitUsersAPI
import com.example.domainlogicandroidtest.data.datasource.GitHubUsersRemoteDataSource
import com.example.domainlogicandroidtest.data.datasource.GitHubUsersRemoteDataSourceImp
import com.example.domainlogicandroidtest.data.repository.GitHubUsersRepositoryImp
import com.example.domainlogicandroidtest.domain.repository.GitHubUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    //region DataSource

    @Singleton
    @Provides
    fun provideProductsRemoteDataSource(gitUsersAPI: GitUsersAPI): GitHubUsersRemoteDataSource =
        GitHubUsersRemoteDataSourceImp(gitUsersAPI)


    //endregion

    //region Repository

    @Singleton
    @Provides
    fun provideProductsRepository(
        gitHubUsersRemoteDataSource: GitHubUsersRemoteDataSource
    ): GitHubUsersRepository =
        GitHubUsersRepositoryImp(gitHubUsersRemoteDataSource)


    //endregion
}