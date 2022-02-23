package com.example.testapplication.data.api

import com.example.testapplication.data.api.model.GithubRepositoryResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/repositories")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun getRepositories(@Query("q") q: String, @Query("page") page: Int): GithubRepositoryResponse

}