package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.entity.SortType
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject

class GetDocumentList @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    suspend operator fun invoke(
        documentType: DocumentType,
        sortType: SortType,
        query: String,
        sort: String? = ACCURACY,
        page: Int?,
    ): DocumentList {
        if (query.isNotBlank()) {
            documentRepository.putRecent(query)
        }
        val list = when (documentType) {
            DocumentType.ALL -> {
                val blogs = documentRepository.getBlogs(
                    query = query,
                    sort = sort,
                    page = page,
                    size = PAGE_SIZE,
                )
                val cafes = documentRepository.getCafes(
                    query = query,
                    sort = sort,
                    page = page,
                    size = PAGE_SIZE,
                )

                DocumentList(
                    blogs.documentList + cafes.documentList,
                    blogs.isEnd && cafes.isEnd
                )
            }
            DocumentType.BLOG ->
                documentRepository.getBlogs(
                    query = query,
                    sort = sort,
                    page = page,
                    size = PAGE_SIZE,
                )
            DocumentType.CAFE ->
                documentRepository.getCafes(
                    query = query,
                    sort = sort,
                    page = page,
                    size = PAGE_SIZE,
                )
        }

        return list.copy(
            documentList = when (sortType) {
                SortType.TITLE -> list.documentList.sortedBy { it.title }
                SortType.DATE -> list.documentList.sortedByDescending { it.datetime }
            }
        )
    }

    companion object {
        const val ACCURACY = "accuracy"
        const val PAGE_SIZE = 25
    }
}
