package com.example.testapplication.data.api.model

import com.google.gson.annotations.SerializedName

data class GithubRepositoryResponse(
    val items: List<GithubRepositoryItem>
)

data class GithubRepositoryItem(
    val name: String,
    val owner: GithubRepositoryOwner,
    @SerializedName("watchers_count")
    val watchersCount: Int,
    @SerializedName("total_count")
    val totalCount: Int
)

data class GithubRepositoryOwner(
    @SerializedName("avatar_url")
    val avatarUrl: String
)