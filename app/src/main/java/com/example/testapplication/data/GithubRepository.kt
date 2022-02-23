package com.example.testapplication.data

import com.example.testapplication.data.api.GithubApiService
import com.example.testapplication.data.api.model.GithubRepositoryResponse
import com.example.testapplication.utils.ApiResult
import com.example.testapplication.utils.safeApiCall
import javax.inject.Inject

class GithubRepository @Inject constructor(private val githubApiService: GithubApiService) {

    suspend fun getData(q: String, page: Int): ApiResult<GithubRepositoryResponse> = safeApiCall {
        githubApiService.getRepositories(q, page)
    }
}