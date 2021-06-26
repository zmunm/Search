package io.github.zmunm.search.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.zmunm.search.ApiKey
import io.github.zmunm.search.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object SingletonModule {

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey(): String = BuildConfig.REST_KEY
}
