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
import io.mockk.slot

class SearchPagingSourceSpec : DescribeSpec({
    val option = SearchPagingSource.Option(
        query = "query",
        DocumentType.ALL,
        SortType.TITLE,
    )
    val getDocumentList: GetDocumentList = mockk()

    val source = SearchPagingSource(option, getDocumentList)

    describe("paging") {
        var hasNext: DocumentType? = DocumentType.ALL
        val documents = listOf<Document>(mockk())
        val typeSlot = slot<DocumentType>()
        val pageSlot = mutableListOf<Int?>()

        coEvery {
            getDocumentList(
                documentType = capture(typeSlot),
                sortType = option.sortType,
                query = option.query,
                page = captureNullable(pageSlot),
            )
        } answers {
            DocumentList(documents, hasNext)
        }

        it("start") {
            hasNext = DocumentType.ALL
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = null,
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = null,
                nextKey = SearchPagingSource.PagingParam(
                    blogKey = 2,
                    blogEnd = false,
                    cafeKey = 2,
                    cafeEnd = false
                )
            )
            typeSlot.captured shouldBe DocumentType.ALL
            pageSlot.single() shouldBe 1
            source.getPager()
        }

        it("end") {
            hasNext = null
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = SearchPagingSource.PagingParam(
                        blogKey = 1,
                        blogEnd = true,
                        cafeKey = 10,
                        cafeEnd = true
                    ),
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = SearchPagingSource.PagingParam(
                    blogKey = 1,
                    blogEnd = true,
                    cafeKey = 9,
                    cafeEnd = false
                ),
                nextKey = null
            )
            typeSlot.captured shouldBe DocumentType.ALL
            pageSlot.single() shouldBe 10
            source.getPager()
        }

        it("page blog 5") {
            hasNext = DocumentType.BLOG
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = SearchPagingSource.PagingParam(
                        blogKey = 5,
                        blogEnd = false,
                        cafeKey = 2,
                        cafeEnd = true
                    ),
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = SearchPagingSource.PagingParam(
                    blogKey = 4,
                    blogEnd = false,
                    cafeKey = 2,
                    cafeEnd = true
                ),
                nextKey = SearchPagingSource.PagingParam(
                    blogKey = 6,
                    blogEnd = false,
                    cafeKey = 2,
                    cafeEnd = true
                )
            )
            typeSlot.captured shouldBe DocumentType.BLOG
            pageSlot.single() shouldBe 5
            source.getPager()
        }

        it("page cafe 5") {
            hasNext = DocumentType.CAFE
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = SearchPagingSource.PagingParam(
                        blogKey = 2,
                        blogEnd = true,
                        cafeKey = 5,
                        cafeEnd = false
                    ),
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = SearchPagingSource.PagingParam(
                    blogKey = 2,
                    blogEnd = true,
                    cafeKey = 4,
                    cafeEnd = false
                ),
                nextKey = SearchPagingSource.PagingParam(
                    blogKey = 2,
                    blogEnd = true,
                    cafeKey = 6,
                    cafeEnd = false
                )
            )
            typeSlot.captured shouldBe DocumentType.CAFE
            pageSlot.single() shouldBe 5
            source.getPager()
        }

        it("page all 5") {
            hasNext = DocumentType.ALL
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = SearchPagingSource.PagingParam(
                        blogKey = 5,
                        blogEnd = false,
                        cafeKey = 5,
                        cafeEnd = false
                    ),
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = SearchPagingSource.PagingParam(
                    blogKey = 4,
                    blogEnd = false,
                    cafeKey = 4,
                    cafeEnd = false
                ),
                nextKey = SearchPagingSource.PagingParam(
                    blogKey = 6,
                    blogEnd = false,
                    cafeKey = 6,
                    cafeEnd = false
                )
            )
            typeSlot.captured shouldBe DocumentType.ALL
            pageSlot.single() shouldBe 5
            source.getPager()
        }

        it("page all 5 but blog end") {
            hasNext = DocumentType.CAFE
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = SearchPagingSource.PagingParam(
                        blogKey = 5,
                        blogEnd = false,
                        cafeKey = 5,
                        cafeEnd = false
                    ),
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = SearchPagingSource.PagingParam(
                    blogKey = 4,
                    blogEnd = false,
                    cafeKey = 4,
                    cafeEnd = false
                ),
                nextKey = SearchPagingSource.PagingParam(
                    blogKey = 5,
                    blogEnd = true,
                    cafeKey = 6,
                    cafeEnd = false
                )
            )
            typeSlot.captured shouldBe DocumentType.ALL
            pageSlot.single() shouldBe 5
            source.getPager()
        }

        it("page restore ended blog") {
            hasNext = DocumentType.CAFE
            source.load(
                PagingSource.LoadParams.Refresh(
                    key = SearchPagingSource.PagingParam(
                        blogKey = 5,
                        blogEnd = true,
                        cafeKey = 5,
                        cafeEnd = false
                    ),
                    loadSize = 2,
                    placeholdersEnabled = false
                )
            ) shouldBe PagingSource.LoadResult.Page(
                data = documents,
                prevKey = SearchPagingSource.PagingParam(
                    blogKey = 4,
                    blogEnd = false,
                    cafeKey = 4,
                    cafeEnd = false
                ),
                nextKey = SearchPagingSource.PagingParam(
                    blogKey = 5,
                    blogEnd = true,
                    cafeKey = 6,
                    cafeEnd = false
                )
            )
            typeSlot.captured shouldBe DocumentType.CAFE
            pageSlot.single() shouldBe 5
            source.getPager()
        }

        coVerify {
            getDocumentList(
                documentType = capture(typeSlot),
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
