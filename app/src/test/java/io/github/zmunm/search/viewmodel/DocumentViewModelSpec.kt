package io.github.zmunm.search.viewmodel

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.usecase.GetVisitedDocument
import io.github.zmunm.search.usecase.PutDocument
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher

class DocumentViewModelSpec : DescribeSpec({
    val getVisitedDocument: GetVisitedDocument = mockk()
    val putDocument: PutDocument = mockk()
    val testDispatcher = TestCoroutineDispatcher()

    val viewModel = DocumentViewModel(
        getVisitedDocument = getVisitedDocument,
        putDocument = putDocument,
        dispatcher = testDispatcher,
    )

    viewModel.document.observeForever { }
    viewModel.visit.observeForever { }

    describe("no bind") {
        it("visit") {
            viewModel.visit()
        }
    }

    describe("visit") {
        val givenUrl = "url"
        val document: Document = mockk {
            every { url } returns givenUrl
        }

        var visitFlow: Flow<Document> = emptyFlow()
        every {
            getVisitedDocument(givenUrl)
        } answers {
            visitFlow
        }

        val visitedDocument = slot<Document>()

        coEvery { putDocument(capture(visitedDocument)) } just Runs

        it("did like") {
            visitFlow = flowOf(mockk())

            viewModel.bindDocument(document)
            viewModel.visit.value shouldNotBe null

            viewModel.visit()
            visitedDocument.captured shouldNotBe null
        }

        coVerify { putDocument(capture(visitedDocument)) }

        verify {
            getVisitedDocument(givenUrl)
        }
    }

    afterContainer {
        confirmVerified(
            getVisitedDocument,
            putDocument,
        )
        clearMocks(
            getVisitedDocument,
            putDocument,
        )
    }

    afterSpec {
        testDispatcher.cleanupTestCoroutines()
    }
})
