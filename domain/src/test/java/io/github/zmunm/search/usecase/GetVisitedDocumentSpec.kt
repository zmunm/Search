package io.github.zmunm.search.usecase

import io.github.zmunm.search.repository.DocumentRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk

internal class GetVisitedDocumentSpec : DescribeSpec({
    val documentRepository: DocumentRepository = mockk()
    val getVisitedDocument = GetVisitedDocument(
        documentRepository = documentRepository
    )

    describe("get document by url") {
        val url = "url"
        coEvery {
            documentRepository.getDocument(url)
        } returns mockk()

        getVisitedDocument(url)

        coVerify(exactly = 1) {
            documentRepository.getDocument(url)
        }
    }

    afterContainer {
        confirmVerified(documentRepository)
        clearMocks(documentRepository)
    }
})
