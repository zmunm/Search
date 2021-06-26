package io.github.zmunm.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.zmunm.search.Params
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.usecase.GetDocument
import io.github.zmunm.search.usecase.PutVisit
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    getDocument: GetDocument,
    private val putVisit: PutVisit,
    savedStateHandle: SavedStateHandle,
    private val documentViewModel: DocumentViewModel,
) : ViewModel() {
    val documentDetail: LiveData<Document> get() = documentViewModel.document

    private val detailFlow = getDocument(
        savedStateHandle.get<String>(Params.URL) ?: error(Params.URL)
    )

    init {
        viewModelScope.launch {
            detailFlow.collectLatest {
                documentViewModel.bindDocument(it)
            }
        }
    }

    fun visit() {
        documentDetail.value?.url?.let {
            viewModelScope.launch {
                putVisit(it)
            }
        }
    }
}
