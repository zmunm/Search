package io.github.zmunm.search.data.service

import io.github.zmunm.search.entity.DocumentList

interface DocumentService {
    suspend fun fetchBlogs(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList

    suspend fun fetchCafes(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList
}
