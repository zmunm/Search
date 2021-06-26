package io.github.zmunm.search.usecase

import io.github.zmunm.search.repository.DocumentRepository
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk

internal class GetDocumentSpec : DescribeSpec({
    val documentRepository: DocumentRepository = mockk()
    val getDocument = GetDocument(
        documentRepository = documentRepository
    )

    describe("get document by url") {
        val url = "url"
        coEvery {
            documentRepository.getDocument(url)
        } returns mockk()

        getDocument(url)

        coVerify(exactly = 1) {
            documentRepository.getDocument(url)
        }
    }

    afterContainer {
        confirmVerified(documentRepository)
        clearMocks(documentRepository)
    }
})
