package io.github.zmunm.search.viewmodel

import androidx.paging.PagingData
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.ui.adapter.paging.SearchPagingSourceFactory
import io.github.zmunm.search.usecase.GetRecentQuery
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class SearchViewModelSpec : DescribeSpec({

    val getRecentQuery: GetRecentQuery = mockk()
    val searchPagingSource: SearchPagingSourceFactory = mockk()

    fun getViewModel() = SearchViewModel(
        getRecentQuery = getRecentQuery,
        searchPagingSource = searchPagingSource,
    )

    val pagingData: PagingData<Document> = PagingData.from(listOf(mockk()))

    describe("search query") {
        val querySlot = slot<String>()

        every {
            searchPagingSource.create(capture(querySlot)).getPager()
        } returns flowOf(pagingData)

        every { getRecentQuery() } returns emptyFlow()

        val viewModel = getViewModel()

        viewModel.pager.observeForever { }
        viewModel.recentQuery.observeForever { }

        it("single query") {
            viewModel.query.value = "query1"

            viewModel.search()

            querySlot.captured shouldBe "query1"
        }

        verify {
            searchPagingSource.create(capture(querySlot)).getPager()
            getRecentQuery()
        }
    }

    afterContainer {
        confirmVerified(
            getRecentQuery,
            searchPagingSource,
        )
        clearMocks(
            getRecentQuery,
            searchPagingSource,
        )
    }
})
