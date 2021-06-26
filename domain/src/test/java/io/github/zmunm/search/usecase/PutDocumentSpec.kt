package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.repository.DocumentRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot

internal class PutDocumentSpec : DescribeSpec({
    val documentRepository: DocumentRepository = mockk()
    val putDocument = PutDocument(
        documentRepository = documentRepository
    )

    describe("put document") {
        val documentSlot = slot<Document>()
        coEvery { documentRepository.putDocument(capture(documentSlot)) } just Runs

        val document: Document = mockk()
        putDocument(document)
        documentSlot.captured shouldBe document

        coVerify(exactly = 1) { documentRepository.putDocument(capture(documentSlot)) }
    }

    afterContainer {
        confirmVerified(
            documentRepository
        )
        clearMocks(
            documentRepository
        )
    }
})
