package io.github.zmunm.search.data

import io.github.zmunm.search.data.cache.DocumentCache
import io.github.zmunm.search.data.service.DocumentService
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentList
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
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf

internal class DocumentDataSourceSpec : DescribeSpec({
    val documentService: DocumentService = mockk()
    val documentCache: DocumentCache = mockk()
    val documentDataSource = DocumentDataSource(
        documentService = documentService,
        documentCache = documentCache,
    )

    describe("getBlogs") {
        val query = "query"
        val responseDocumentList = listOf(mockk<Document>())
        val list = mockk<DocumentList> {
            every { documentList } returns responseDocumentList
        }

        val pageSlot = mutableListOf<Int?>()

        coEvery {
            documentService.fetchBlogs(query, null, captureNullable(pageSlot), null)
        } returns list

        it("unknown page") {
            documentDataSource.getBlogs(query, null, null, null)
            pageSlot.single() shouldBe null
        }

        it("page 1") {
            documentDataSource.getBlogs(query, null, 1, null)
            pageSlot.single() shouldBe 1
        }

        coVerify {
            documentService.fetchBlogs(query, null, captureNullable(pageSlot), null)
        }
    }

    describe("getCafes") {
        val query = "query"
        val responseDocumentList = listOf(mockk<Document>())
        val list = mockk<DocumentList> {
            every { documentList } returns responseDocumentList
        }

        val pageSlot = mutableListOf<Int?>()

        coEvery {
            documentService.fetchCafes(query, null, captureNullable(pageSlot), null)
        } returns list

        it("unknown page") {
            documentDataSource.getCafes(query, null, null, null)
            pageSlot.single() shouldBe null
        }

        it("page 1") {
            documentDataSource.getCafes(query, null, 1, null)
            pageSlot.single() shouldBe 1
        }

        coVerify {
            documentService.fetchCafes(query, null, captureNullable(pageSlot), null)
        }
    }

    describe("getDocument") {
        val url = "url"
        every {
            documentCache.getDocument(url)
        } returns flowOf(mockk())

        documentDataSource.getDocument(url)

        verify {
            documentCache.getDocument(url)
        }
    }

    describe("putDocument") {
        val documentSlot = slot<Document>()

        coEvery {
            documentCache.insertDocument(capture(documentSlot))
        } just Runs

        it("true") {
            val document: Document = mockk()
            documentDataSource.putDocument(document)
            documentSlot.captured shouldBe document
        }

        coVerify {
            documentCache.insertDocument(capture(documentSlot))
        }
    }

    afterContainer {
        confirmVerified(
            documentService,
            documentCache,
        )
        clearMocks(
            documentService,
            documentCache,
        )
    }
})
