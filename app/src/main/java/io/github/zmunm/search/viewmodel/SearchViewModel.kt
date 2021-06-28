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
import io.github.zmunm.search.SingleLiveEvent
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.entity.Recent
import io.github.zmunm.search.entity.SortType
import io.github.zmunm.search.ui.adapter.paging.SearchPagingSource
import io.github.zmunm.search.ui.adapter.paging.SearchPagingSourceFactory
import io.github.zmunm.search.usecase.GetRecentQuery
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    getRecentQuery: GetRecentQuery,
    private val searchPagingSource: SearchPagingSourceFactory,
) : ViewModel() {
    val query = MutableLiveData<String>()

    val hasFocus = MutableLiveData<Boolean>()

    private val defaultDocumentType = DocumentType.ALL
    private val _documentType = MutableLiveData(defaultDocumentType)
    val documentType: LiveData<DocumentType> get() = _documentType

    private val defaultSort = SortType.TITLE
    private val _sort = MutableLiveData(defaultSort)
    val sort: LiveData<SortType> get() = _sort

    private val _pager = MutableLiveData<SearchPagingSource.Option>()
    val pager: LiveData<PagingData<Document>> = _pager.switchMap { option ->
        getPager(option)
    }

    val recentQuery: LiveData<List<Recent>> = getRecentQuery().asLiveData()

    private val _action = SingleLiveEvent<Action>()
    val action: LiveData<Action> get() = _action

    fun search() {
        hasFocus.value = false
        refreshPager()
    }

    fun searchRecent(query: String) {
        this.query.value = query
        search()
    }

    fun changeFilter() {
        documentType.value?.let {
            _action.setValue(Action.DocumentFilter(it) { result ->
                _documentType.value = result
                refreshPager()
            })
        }
    }

    fun changeSort() {
        sort.value?.let {
            _action.setValue(Action.Sort(it) { result ->
                _sort.value = result
                refreshPager()
            })
        }
    }

    private fun refreshPager() {
        _pager.value = SearchPagingSource.Option(
            query.value ?: "",
            documentType.value ?: defaultDocumentType,
            sort.value ?: defaultSort
        )
    }

    private fun getPager(option: SearchPagingSource.Option): LiveData<PagingData<Document>> =
        searchPagingSource.create(option)
            .getPager()
            .cachedIn(viewModelScope)
            .asLiveData()

    sealed class Action {
        data class DocumentFilter(
            val default: DocumentType,
            val callBack: (DocumentType) -> Unit,
        ) : Action()
        data class Sort(
            val default: SortType,
            val callBack: (SortType) -> Unit,
        ) : Action()
    }
}
