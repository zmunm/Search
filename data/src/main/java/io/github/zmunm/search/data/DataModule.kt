package io.github.zmunm.search.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.zmunm.search.ApiKey
import io.github.zmunm.search.data.cache.AppDatabase
import io.github.zmunm.search.data.cache.DocumentCache
import io.github.zmunm.search.data.cache.impl.DocumentCacheImpl
import io.github.zmunm.search.data.service.DocumentService
import io.github.zmunm.search.data.service.api.DocumentApi
import io.github.zmunm.search.data.service.impl.DocumentServiceImpl
import io.github.zmunm.search.repository.DocumentRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
    private const val BASE_URL = "https://dapi.kakao.com/v2/"

    @Provides
    @Singleton
    fun provideDocumentService(
        @ApiKey
        apiKey: String,
    ): DocumentService = DocumentServiceImpl(
        InternalInstance.retrofit(BASE_URL, apiKey).create(DocumentApi::class.java)
    )

    @Provides
    @Singleton
    fun provideDocumentCache(
        @ApplicationContext
        context: Context
    ): DocumentCache = DocumentCacheImpl(
        AppDatabase.getInstance(context).documentDao()
    )

    @Provides
    @Singleton
    fun provideDocumentRepository(
        documentService: DocumentService,
        documentCache: DocumentCache,
    ): DocumentRepository = DocumentDataSource(
        documentService,
        documentCache,
    )
}
