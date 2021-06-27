package io.github.zmunm.search.usecase

import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.entity.SortType
import io.github.zmunm.search.repository.DocumentRepository
import io.github.zmunm.search.usecase.GetDocumentList.Companion.PAGE_SIZE
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk

internal class GetDocumentListSpec : DescribeSpec({
    val documentRepository: DocumentRepository = mockk()
    val getDocumentList = GetDocumentList(
        documentRepository = documentRepository
    )

    describe("get document list by page") {
        val query = "query"
        val pageSlot = mutableListOf<Int?>()
        val documentList = DocumentList(
            listOf(
                mockk {
                    every { title } returns "title"
                }
            ),
            false
        )

        coEvery { documentRepository.putRecent(query) } just Runs

        coEvery {
            documentRepository.getBlogs(query, null, captureNullable(pageSlot), PAGE_SIZE)
        } returns documentList

        it("page null") {
            getDocumentList(DocumentType.BLOG, SortType.TITLE, query, null, null)
            pageSlot.single() shouldBe null
        }

        coVerify(exactly = 1) {
            documentRepository.putRecent(query)
            documentRepository.getBlogs(query, null, captureNullable(pageSlot), PAGE_SIZE)
        }
    }

    afterContainer {
        confirmVerified(documentRepository)
        clearMocks(documentRepository)
    }
})
