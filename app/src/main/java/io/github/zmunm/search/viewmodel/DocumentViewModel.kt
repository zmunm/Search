package io.github.zmunm.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.usecase.GetVisitedDocument
import io.github.zmunm.search.usecase.PutDocument
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DocumentViewModel @Inject constructor(
    getVisitedDocument: GetVisitedDocument,
    private val putDocument: PutDocument,
    private val dispatcher: CoroutineDispatcher
) {
    private val _document = MutableLiveData<Document>()
    val document: LiveData<Document> get() = _document
    val visit: LiveData<Document> = _document.switchMap { document ->
        getVisitedDocument(document.url).asLiveData()
    }

    fun bindDocument(document: Document) {
        _document.value = document
    }

    fun visit() {
        CoroutineScope(dispatcher).launch {
            putDocument(
                document.value ?: return@launch
            )
        }
    }
}
