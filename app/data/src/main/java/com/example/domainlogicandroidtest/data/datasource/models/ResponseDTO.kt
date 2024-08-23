package com.example.domainlogicandroidtest.data.datasource.models

import com.example.domainlogicandroidtest.data.datasource.models.GitUserDTO

data class ResponseDTO(
    val totalCount: Int = 0,
    val incompleteResults: Boolean = false,
    val items: List<GitUserDTO>?
)
