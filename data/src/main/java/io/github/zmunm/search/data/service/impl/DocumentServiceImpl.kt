package io.github.zmunm.search.data.service.impl

import android.annotation.SuppressLint
import io.github.zmunm.search.data.InternalInstance
import io.github.zmunm.search.data.service.DocumentService
import io.github.zmunm.search.data.service.api.DocumentApi
import io.github.zmunm.search.data.service.dao.ResponseError
import io.github.zmunm.search.data.service.toEntity
import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.repository.KnownThrowable
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

@SuppressLint("TimberExceptionLogging")
internal class DocumentServiceImpl(
    private val documentApi: DocumentApi,
) : DocumentService {

    private val errorConverter = InternalInstance.moshi.adapter(ResponseError::class.java)

    override suspend fun fetchBlogs(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList = documentApi.fetchBlogs(
        query = query,
        sort = sort,
        page = page,
        size = size,
    ).toResult { it.toEntity() }
        .onFailure { Timber.i(it.message) }
        .getOrDefault(DocumentList(emptyList(), true))

    override suspend fun fetchCafes(
        query: String,
        sort: String?,
        page: Int?,
        size: Int?,
    ): DocumentList = documentApi.fetchCafes(
        query = query,
        sort = sort,
        page = page,
        size = size,
    ).toResult { it.toEntity() }
        .onFailure { Timber.i(it.message) }
        .getOrDefault(DocumentList(emptyList(), true))

    private fun <T, R> Response<T>.toResult(mapper: (T) -> R): Result<R> =
        if (isSuccessful) {
            body()?.let {
                Result.success(mapper(it))
            } ?: Result.failure(NullPointerException(headers().toString()))
        } else {
            Result.failure(
                errorBody()?.source()
                    ?.let(errorConverter::fromJson)
                    ?.let {
                        KnownThrowable(
                            it.message,
                            HttpException(this)
                        )
                    } ?: HttpException(this)
            )
        }
}
