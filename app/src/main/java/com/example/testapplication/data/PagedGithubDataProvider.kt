package com.example.testapplication.data

import android.util.Log
import com.example.testapplication.data.api.model.GithubRepositoryResponse
import com.example.testapplication.utils.ApiResult
import javax.inject.Inject

// Just a wrapper around the repo to handle the data paging
// It keeps the current page that view model returned to the view
// We assume that there will no race conditions issues as viewModel
// cancels current job in case new job is scheduled
class PagedGithubDataProvider @Inject constructor(private val githubRepository: GithubRepository) {

    private var currentPageNum = 0
    private var lastSearchQuery: String? = null

    suspend fun fetchPagedData(q: String, fromStart: Boolean): ApiResult<GithubRepositoryResponse> {
        updatePagedData(fromStart, q)

        Log.d("PagedDataProvider::", "q = $q, page = $currentPageNum")
        val result = githubRepository.getData(q, currentPageNum)
        if (result !is ApiResult.Success) {
            currentPageNum--
        }
        return result
    }

    @Synchronized
    private fun updatePagedData(fromStart: Boolean, q: String) {
        if (fromStart) {
            lastSearchQuery = null
            currentPageNum = 0
        }

        lastSearchQuery?.let {

            if (q == lastSearchQuery) {
                currentPageNum++
            } else {
                currentPageNum = 1
                lastSearchQuery = q
            }

        } ?: run {
            currentPageNum = 1
            lastSearchQuery = q
        }
    }


}