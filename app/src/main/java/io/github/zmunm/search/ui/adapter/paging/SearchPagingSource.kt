package io.github.zmunm.search.ui.adapter.paging

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.entity.SortType
import io.github.zmunm.search.usecase.GetDocumentList
import io.github.zmunm.search.usecase.GetDocumentList.Companion.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class SearchPagingSource @AssistedInject constructor(
    @Assisted private val option: Option,
    private val getDocumentList: GetDocumentList,
) : PagingSource<SearchPagingSource.PagingParam, Document>() {

    private val pagingConfig = PagingConfig(pageSize = PAGE_SIZE)

    override suspend fun load(
        params: LoadParams<PagingParam>
    ): LoadResult<PagingParam, Document> {
        val page = params.key ?: when (option.documentType) {
            DocumentType.ALL -> PagingParam(1, false, 1, false)
            DocumentType.BLOG -> PagingParam(1, false, 0, true)
            DocumentType.CAFE -> PagingParam(0, true, 1, false)
        }

        val list = getDocumentList(
            documentType = page.getDocumentType(),
            sortType = option.sortType,
            query = option.query,
            page = page.getPage(),
        )
        return LoadResult.Page(
            data = list.documentList,
            prevKey = page.prevPage(),
            nextKey = page.nextPage(list.hasNext)
        )
    }

    override fun getRefreshKey(state: PagingState<PagingParam, Document>): PagingParam? =
        state.anchorPosition?.let {
            state.closestPageToPosition(it)
        }?.let {
            it.prevKey ?: it.nextKey
        }

    fun getPager(): Flow<PagingData<Document>> = Pager(pagingConfig) { this }.flow

    data class PagingParam(
        val blogKey: Int,
        val blogEnd: Boolean,
        val cafeKey: Int,
        val cafeEnd: Boolean,
    ) {
        fun getDocumentType(): DocumentType = when {
            !blogEnd && cafeEnd -> DocumentType.BLOG
            blogEnd && !cafeEnd -> DocumentType.CAFE
            else -> DocumentType.ALL
        }

        fun getPage(): Int = maxOf(blogKey, cafeKey)

        fun nextPage(documentType: DocumentType?): PagingParam? = when (documentType) {
            DocumentType.ALL -> copy(
                blogKey = if (blogEnd) blogKey else blogKey + 1,
                cafeKey = if (cafeEnd) cafeKey else cafeKey + 1,
            )
            DocumentType.BLOG -> copy(
                blogKey = blogKey + 1,
                cafeEnd = true,
            )
            DocumentType.CAFE -> copy(
                blogEnd = true,
                cafeKey = cafeKey + 1,
            )
            null -> null
        }

        fun prevPage(): PagingParam? {
            val page = getPage()
            if (page == 1) return null
            return PagingParam(
                blogKey = if (page != blogKey) blogKey else blogKey - 1,
                blogEnd = page != blogKey,
                cafeKey = if (page != cafeKey) cafeKey else cafeKey - 1,
                cafeEnd = page != cafeKey,
            )
        }
    }

    data class Option(
        val query: String,
        val documentType: DocumentType,
        val sortType: SortType,
    )
}

@AssistedFactory
interface SearchPagingSourceFactory {
    fun create(option: SearchPagingSource.Option): SearchPagingSource
}
