package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject

class PutDocument @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    suspend operator fun invoke(document: Document) {
        documentRepository.putDocument(document)
    }
}
