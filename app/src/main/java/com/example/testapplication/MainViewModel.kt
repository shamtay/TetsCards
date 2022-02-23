package com.example.testapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.data.PagedGithubDataProvider
import com.example.testapplication.data.api.model.GithubRepositoryItem
import com.example.testapplication.utils.ApiResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val pagedGithubDataProvider: PagedGithubDataProvider) :
    ViewModel() {

    private val _searchResultsLiveData = MutableLiveData<DataFetchState<SearchResults>>()
    val searchResultsLiveData: LiveData<DataFetchState<SearchResults>>
        get() = _searchResultsLiveData

    private var currentJob: Job? = null

    fun search(query: String, fromStart: Boolean) {
        _searchResultsLiveData.postValue(DataFetchState.Progress())

        // to cancel pending search requests to avoid data collision
        currentJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }

         currentJob = viewModelScope.launch {
            val result = pagedGithubDataProvider.fetchPagedData(query, fromStart)
            (result as? ApiResult.Success)?.data?.items?.let {
                _searchResultsLiveData.postValue(
                    DataFetchState.Success(SearchResults(query, it))
                )
            } ?: run {
                _searchResultsLiveData.postValue(DataFetchState.Error())
            }
        }
    }
}

data class SearchResults(
    val query: String,
    val repos: List<GithubRepositoryItem>
)

sealed class DataFetchState<out T: Any> {

    data class Success<out T : Any>(val result: T): DataFetchState<T>()
    class Progress<out T : Any>: DataFetchState<T>()
    class Error<out T : Any>: DataFetchState<T>()

}