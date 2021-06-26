package io.github.zmunm.search.viewmodel

import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.Visit
import io.github.zmunm.search.usecase.GetVisit
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
    val getVisit: GetVisit = mockk()
    val putDocument: PutDocument = mockk()
    val testDispatcher = TestCoroutineDispatcher()

    val viewModel = DocumentViewModel(
        getVisit = getVisit,
        putDocument = putDocument,
        dispatcher = testDispatcher,
    )

    viewModel.document.observeForever { }
    viewModel.visit.observeForever { }

    describe("no bind") {
        it("click") {
            viewModel.putDocument()
        }
    }

    describe("visit") {
        val givenUrl = "url"
        val document: Document = mockk {
            every { url } returns givenUrl
        }

        var visitFlow: Flow<Visit> = emptyFlow()
        every {
            getVisit(givenUrl)
        } answers {
            visitFlow
        }

        val visitedDocument = slot<Document>()

        coEvery { putDocument(capture(visitedDocument)) } just Runs

        visitFlow = flowOf(mockk())

        viewModel.bindDocument(document)
        viewModel.visit.value shouldNotBe null

        viewModel.putDocument()
        visitedDocument.captured shouldNotBe null

        coVerify { putDocument(capture(visitedDocument)) }

        verify {
            getVisit(givenUrl)
        }
    }

    afterContainer {
        confirmVerified(
            getVisit,
            putDocument,
        )
        clearMocks(
            getVisit,
            putDocument,
        )
    }

    afterSpec {
        testDispatcher.cleanupTestCoroutines()
    }
})
