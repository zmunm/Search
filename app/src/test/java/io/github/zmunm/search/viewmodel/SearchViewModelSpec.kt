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

class SearchViewModelSpec : DescribeSpec({

    val searchPagingSource: SearchPagingSourceFactory = mockk()

    val viewModel = SearchViewModel(
        searchPagingSource = searchPagingSource,
    )

    viewModel.pager.observeForever {}

    val pagingData: PagingData<Document> = PagingData.from(listOf(mockk()))

    describe("search query") {
        val querySlot = slot<String>()

        every {
            searchPagingSource.create(capture(querySlot)).getPager()
        } returns flowOf(pagingData)

        it("single query") {
            viewModel.query.value = "query1"

            viewModel.search()

            querySlot.captured shouldBe "query1"
        }

        verify {
            searchPagingSource.create(capture(querySlot)).getPager()
        }
    }

    afterContainer {
        confirmVerified(
            searchPagingSource,
        )
        clearMocks(
            searchPagingSource,
        )
    }
})
