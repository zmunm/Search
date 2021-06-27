package io.github.zmunm.search.ui.adapter.paging

import androidx.paging.PagingSource
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.entity.SortType
import io.github.zmunm.search.usecase.GetDocumentList
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk

class SearchPagingSourceSpec : DescribeSpec({
    val option = SearchPagingSource.Option(
        query = "query",
        DocumentType.ALL,
        SortType.TITLE,
    )
    val getDocumentList: GetDocumentList = mockk()

    val source = SearchPagingSource(option, getDocumentList)

    describe("paging") {
        var isEnd = false
        val documents = listOf<Document>(mockk())
        val pageSlot = mutableListOf<Int?>()

        coEvery {
            getDocumentList(
                documentType = option.documentType,
                sortType = option.sortType,
                query = option.query,
                page = captureNullable(pageSlot),
            )
        } answers {
            DocumentList(documents, isEnd)
        }

        it("start") {
            isEnd = false
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = null,
                nextKey = 2
            )
            pageSlot.single() shouldBe 1
            source.getPager()
        }

        it("end") {
            isEnd = true
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = 10,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = 9,
                nextKey = null
            )
            pageSlot.single() shouldBe 10
            source.getPager()
        }

        it("page 5") {
            isEnd = false
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = 5,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = 4,
                nextKey = 6
            )
            pageSlot.single() shouldBe 5
            source.getPager()
        }

        coVerify {
            getDocumentList(
                documentType = option.documentType,
                sortType = option.sortType,
                query = option.query,
                page = captureNullable(pageSlot),
            )
        }
    }

    afterContainer {
        confirmVerified(getDocumentList)
        clearMocks(getDocumentList)
    }
})
