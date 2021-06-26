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
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.ui.adapter.paging.SearchPagingSourceFactory
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchPagingSource: SearchPagingSourceFactory,
) : ViewModel() {
    val query = MutableLiveData<String>()

    private val _pager = MutableLiveData<String>()
    val pager: LiveData<PagingData<Document>> = _pager.switchMap { query ->
        getPager(query)
    }

    fun search() {
        query.value?.let {
            _pager.value = it
        }
    }

    private fun getPager(query: String): LiveData<PagingData<Document>> =
        searchPagingSource.create(query)
            .getPager()
            .cachedIn(viewModelScope)
            .asLiveData()
}
