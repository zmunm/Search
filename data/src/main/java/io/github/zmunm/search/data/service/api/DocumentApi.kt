package io.github.zmunm.search.data.service.api

import io.github.zmunm.search.data.service.dao.ResponseBlog
import io.github.zmunm.search.data.service.dao.ResponseCafe
import io.github.zmunm.search.data.service.dao.ResponseList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

internal interface DocumentApi {
    @GET("search/blog")
    suspend fun fetchBlogs(
        @Query(QUERY)
        query: String,
        @Query(SORT)
        sort: String?,
        @Query(PAGE)
        page: Int?,
        @Query(SIZE)
        size: Int?,
    ): Response<ResponseList<ResponseBlog>>

    @GET("search/cafe")
    suspend fun fetchCafes(
        @Query(QUERY)
        query: String,
        @Query(SORT)
        sort: String?,
        @Query(PAGE)
        page: Int?,
        @Query(SIZE)
        size: Int?,
    ): Response<ResponseList<ResponseCafe>>

    companion object {
        private const val QUERY = "query"
        private const val SORT = "sort"
        private const val PAGE = "page"
        private const val SIZE = "size"
    }
}
