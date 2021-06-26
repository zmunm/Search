package io.github.zmunm.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.zmunm.search.Params
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.usecase.GetVisitedDocument
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    getVisitedDocument: GetVisitedDocument,
    savedStateHandle: SavedStateHandle,
    private val documentViewModel: DocumentViewModel,
) : ViewModel() {
    val documentDetail: LiveData<Document> get() = documentViewModel.document

    private val detailFlow = getVisitedDocument(
        savedStateHandle.get<String>(Params.URL) ?: error(Params.URL)
    )

    init {
        viewModelScope.launch {
            detailFlow.collectLatest {
                documentViewModel.bindDocument(it)
            }
        }
    }

    fun toggleLike() {
        documentViewModel.visit()
    }
}
