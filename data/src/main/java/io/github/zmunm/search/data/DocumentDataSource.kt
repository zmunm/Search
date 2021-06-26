package io.github.zmunm.search.data

import io.github.zmunm.search.data.cache.DocumentCache
import io.github.zmunm.search.data.service.DocumentService
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow

internal class DocumentDataSource(
    private val documentService: DocumentService,
    private val documentCache: DocumentCache,
) : DocumentRepository {
    override suspend fun getBlogs(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList = documentService.fetchBlogs(
        query = query,
        sort = sort,
        page = page,
        size = size,
    )

    override suspend fun getCafes(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList = documentService.fetchCafes(
        query = query,
        sort = sort,
        page = page,
        size = size,
    )

    override fun getDocument(url: String): Flow<Document> = documentCache.getDocument(url)

    override suspend fun putDocument(document: Document) {
        documentCache.insertDocument(document)
    }
}
