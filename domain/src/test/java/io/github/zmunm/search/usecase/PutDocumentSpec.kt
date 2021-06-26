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

    describe("put like") {
        val likeSlot = slot<Document>()
        coEvery { documentRepository.putDocument(capture(likeSlot)) } just Runs

        val document: Document = mockk()
        putDocument(document)
        likeSlot.captured shouldBe document

        coVerify(exactly = 1) { documentRepository.putDocument(capture(likeSlot)) }
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
