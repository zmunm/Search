package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetDocument @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    operator fun invoke(url: String): Flow<Document> = documentRepository.getDocument(url)
}
