package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.Recent
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetRecentQuery @Inject constructor(
    private val documentRepository: DocumentRepository,
) {
    operator fun invoke(): Flow<List<Recent>> = documentRepository.getRecent()
}
