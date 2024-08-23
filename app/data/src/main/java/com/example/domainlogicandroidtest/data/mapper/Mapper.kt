package com.example.domainlogicandroidtest.data.mapper

import com.example.domainlogicandroidtest.data.datasource.models.GitUserDTO
import com.example.domainlogicandroidtest.data.datasource.models.GitUserDetailDTO
import com.example.domainlogicandroidtest.domain.model.GitHubUser
import com.example.domainlogicandroidtest.domain.model.GitHubUserDetail

fun GitUserDTO.mapToDomain() = GitHubUser(
    login = login,
    id = id,
    nodeId = nodeId,
    avatarUrl = avatarUrl,
    gravatarId = gravatarId,
    url = url,
    htmlUrl = htmlUrl,
    followersUrl = followersUrl,
    followingUrl = followingUrl,
    gistsUrl = gistsUrl,
    starredUrl = starredUrl,
    subscriptionsUrl = subscriptionsUrl,
    organizationsUrl = organizationsUrl,
    reposUrl = reposUrl,
    eventsUrl = eventsUrl,
    receivedEventsUrl = receivedEventsUrl,
    type = type,
    siteAdmin = siteAdmin,
    score = score
)

@JvmName("productDTOListToDomain")
fun List<GitUserDTO>.mapToDomain() = map { it.mapToDomain() }.toList()

fun GitUserDetailDTO.mapToDomain() = GitHubUserDetail(
    login = login,
    id = id,
    nodeId = nodeId,
    avatarUrl = avatarUrl,
    gravatarId = gravatarId,
    url = url,
    htmlUrl = htmlUrl,
    followersUrl = followersUrl,
    followingUrl = followingUrl,
    gistsUrl = gistsUrl,
    starredUrl = starredUrl,
    subscriptionsUrl = subscriptionsUrl,
    organizationsUrl = organizationsUrl,
    reposUrl = reposUrl,
    eventsUrl = eventsUrl,
    receivedEventsUrl = receivedEventsUrl,
    type = type,
    siteAdmin = siteAdmin,
    name = name,
    company = company,
    blog = blog,
    location = location,
    email = email,
    hireable = hireable,
    bio = bio,
    twitterUsername = twitterUsername,
    publicRepos = publicRepos,
    publicGists = publicGists,
    followers = followers,
    following = following,
    createdAt = createdAt,
    updatedAt = updatedAt
)

