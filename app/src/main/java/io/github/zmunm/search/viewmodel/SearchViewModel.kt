package io.github.zmunm.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.zmunm.search.debounce
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.ui.adapter.paging.SearchPagingSourceFactory
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPagingSource: SearchPagingSourceFactory,
    dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val debounceQuery = debounce<String>(SEARCH_DEBOUNCE, dispatcher) { query ->
        if (query.isNotBlank()) {
            this.query.postValue(query)
        }
    }

    private val query = MutableLiveData<String>()

    val pager: LiveData<PagingData<Document>> = query.switchMap { query ->
        getPager(query)
    }

    fun onQueryChange(query: String) {
        debounceQuery(query)
    }

    private fun getPager(query: String): LiveData<PagingData<Document>> =
        searchPagingSource.create(query)
            .getPager()
            .cachedIn(viewModelScope)
            .asLiveData()

    companion object {
        private const val SEARCH_DEBOUNCE = 1000L
    }
}
