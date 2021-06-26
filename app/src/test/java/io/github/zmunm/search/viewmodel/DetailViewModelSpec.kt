package io.github.zmunm.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import io.github.zmunm.search.Params
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.usecase.GetVisitedDocument
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class DetailViewModelSpec : DescribeSpec({
    val getVisitedDocument: GetVisitedDocument = mockk()
    val savedStateHandle: SavedStateHandle = mockk()
    val documentViewModel: DocumentViewModel = mockk()

    fun getViewModel() = DetailViewModel(
        getVisitedDocument = getVisitedDocument,
        savedStateHandle = savedStateHandle,
        documentViewModel = documentViewModel,
    )

    describe("no URL") {
        every { savedStateHandle.get<String>(Params.URL) } returns null

        shouldThrow<IllegalStateException> {
            getViewModel()
        }.message shouldBe Params.URL

        verify { savedStateHandle.get<String>(Params.URL) }
    }

    describe("has URL") {
        val url = "url"
        every { savedStateHandle.get<String>(Params.URL) } returns url

        var detailFlow: Flow<Document> = emptyFlow()
        every { getVisitedDocument(url) } answers { detailFlow }

        every { documentViewModel.visit() } just Runs

        it("no data") {
            detailFlow = emptyFlow()

            val viewModel = getViewModel()

            viewModel.toggleLike()
        }

        it("has data") {
            val document: Document = mockk()
            detailFlow = flowOf(document)

            every { documentViewModel.bindDocument(document) } just Runs

            val viewModel = getViewModel()

            verify { documentViewModel.bindDocument(document) }

            viewModel.toggleLike()
        }

        verify {
            savedStateHandle.get<String>(Params.URL)
            getVisitedDocument(url)
            documentViewModel.visit()
        }
    }

    afterContainer {
        confirmVerified(
            getVisitedDocument,
            savedStateHandle,
            documentViewModel,
        )
        clearMocks(
            getVisitedDocument,
            savedStateHandle,
            documentViewModel,
        )
    }
})
