package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject

class GetDocumentList @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    suspend operator fun invoke(
        type: DocumentType,
        query: String,
        sort: String? = ACCURACY,
        page: Int?,
        size: Int?,
    ): DocumentList = when (type) {
        DocumentType.ALL -> {
            val cafes =
                documentRepository.getBlogs(
                    query = query,
                    sort = sort,
                    page = page,
                    size = size,
                )
            cafes.copy(documentList = cafes.documentList.sortedBy { it.title })
        }
        DocumentType.BLOG ->
            documentRepository.getBlogs(
                query = query,
                sort = sort,
                page = page,
                size = size,
            )
        DocumentType.CAFE ->
            documentRepository.getCafes(
                query = query,
                sort = sort,
                page = page,
                size = size,
            )
    }

    companion object {
        const val ACCURACY = "accuracy"
    }
}
