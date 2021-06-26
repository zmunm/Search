package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.Visit
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetVisit @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    operator fun invoke(url: String): Flow<Visit> = documentRepository.getVisit(url)
}
