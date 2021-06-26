package io.github.zmunm.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import io.github.zmunm.search.Params
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.usecase.GetDocument
import io.github.zmunm.search.usecase.PutVisit
import io.kotest.assertions.throwables.shouldThrow
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
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class DetailViewModelSpec : DescribeSpec({
    val getDocument: GetDocument = mockk()
    val putVisit: PutVisit = mockk()
    val savedStateHandle: SavedStateHandle = mockk()
    val documentViewModel: DocumentViewModel = mockk()

    fun getViewModel() = DetailViewModel(
        getDocument = getDocument,
        putVisit = putVisit,
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
        every { getDocument(url) } answers { detailFlow }

        it("no data") {
            detailFlow = emptyFlow()
            every { documentViewModel.document } answers { detailFlow.asLiveData() }

            val viewModel = getViewModel()
            viewModel.documentDetail.observeForever { }

            viewModel.visit()
        }

        it("has data") {
            val document: Document = mockk {
                val doc = this
                every { doc.url } returns "url"
            }
            detailFlow = flowOf(document)
            every { documentViewModel.document } returns MutableLiveData(document)

            every { documentViewModel.bindDocument(document) } just Runs

            val viewModel = getViewModel()
            viewModel.documentDetail.observeForever { }

            verify { documentViewModel.bindDocument(document) }

            coEvery { putVisit(url) } just Runs

            viewModel.visit()

            coVerify { putVisit(url) }
        }

        verify {
            savedStateHandle.get<String>(Params.URL)
            getDocument(url)
            documentViewModel.document
        }
    }

    afterContainer {
        confirmVerified(
            getDocument,
            putVisit,
            savedStateHandle,
            documentViewModel,
        )
        clearMocks(
            getDocument,
            putVisit,
            savedStateHandle,
            documentViewModel,
        )
    }
})
