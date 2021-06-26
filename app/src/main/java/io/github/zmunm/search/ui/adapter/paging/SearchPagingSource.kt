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
import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.usecase.GetDocumentList
import kotlinx.coroutines.flow.Flow

class SearchPagingSource @AssistedInject constructor(
    @Assisted private val query: String,
    private val getDocumentList: GetDocumentList,
) : PagingSource<Int, Document>() {

    private val pagingConfig = PagingConfig(pageSize = PAGE_SIZE)

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Document> {
        val page = params.key ?: 1
        return getDocumentList(
            type = DocumentType.ALL,
            query = query,
            page = page,
            size = PAGE_SIZE
        ).toPage(page)
    }

    override fun getRefreshKey(state: PagingState<Int, Document>): Int? =
        state.anchorPosition

    private fun DocumentList.toPage(now: Int) = LoadResult.Page(
        data = documentList,
        prevKey = if (now == 1) null else now - 1,
        nextKey = if (isEnd) null else now + 1
    )

    fun getPager(): Flow<PagingData<Document>> = Pager(pagingConfig) { this }.flow

    companion object {
        private const val PAGE_SIZE = 25
    }
}

@AssistedFactory
interface SearchPagingSourceFactory {
    fun create(query: String): SearchPagingSource
}
