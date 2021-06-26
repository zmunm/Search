package io.github.zmunm.search.usecase

import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject

class PutVisit @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    suspend operator fun invoke(url: String) {
        documentRepository.putVisit(url)
    }
}
