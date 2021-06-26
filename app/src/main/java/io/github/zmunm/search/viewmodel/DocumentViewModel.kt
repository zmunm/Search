package io.github.zmunm.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.Visit
import io.github.zmunm.search.usecase.GetVisit
import io.github.zmunm.search.usecase.PutDocument
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DocumentViewModel @Inject constructor(
    getVisit: GetVisit,
    private val putDocument: PutDocument,
    private val dispatcher: CoroutineDispatcher
) {
    private val _document = MutableLiveData<Document>()
    val document: LiveData<Document> get() = _document
    val visit: LiveData<Visit> = _document.switchMap { document ->
        getVisit(document.url).asLiveData()
    }

    fun bindDocument(document: Document) {
        _document.value = document
    }

    fun putDocument() {
        CoroutineScope(dispatcher).launch {
            putDocument(
                document.value ?: return@launch
            )
        }
    }
}
