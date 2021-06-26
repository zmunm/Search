package io.github.zmunm.search.viewmodel

import androidx.paging.PagingData
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.ui.adapter.paging.SearchPagingSourceFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher

class SearchViewModelSpec : DescribeSpec({

    val searchPagingSource: SearchPagingSourceFactory = mockk()
    val testDispatcher = TestCoroutineDispatcher()

    val viewModel = SearchViewModel(
        searchPagingSource = searchPagingSource,
        dispatcher = testDispatcher,
    )

    viewModel.pager.observeForever {}

    val pagingData: PagingData<Document> = PagingData.from(listOf(mockk()))

    describe("search query") {
        val querySlot = slot<String>()

        every {
            searchPagingSource.create(capture(querySlot)).getPager()
        } returns flowOf(pagingData)

        it("single query") {
            viewModel.onQueryChange("query1")

            testDispatcher.advanceUntilIdle()

            querySlot.captured shouldBe "query1"
        }

        it("double query") {
            viewModel.onQueryChange("query1")
            viewModel.onQueryChange("query2")

            testDispatcher.advanceUntilIdle()

            querySlot.captured shouldBe "query2"
        }

        verify {
            searchPagingSource.create(capture(querySlot)).getPager()
        }
    }

    describe("skip search") {
        it("empty") {
            viewModel.onQueryChange("")
        }

        it("blank") {
            viewModel.onQueryChange(" ")
        }

        it("end with empty") {
            viewModel.onQueryChange("query1")
            viewModel.onQueryChange("")
        }
    }

    afterContainer {
        testDispatcher.advanceUntilIdle()
        confirmVerified(
            searchPagingSource,
        )
        clearMocks(
            searchPagingSource,
        )
        testDispatcher.cleanupTestCoroutines()
    }
})
