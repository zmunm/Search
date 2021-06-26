package io.github.zmunm.search.data

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.Date
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

internal object InternalInstance {
    private const val TAG = "OkHTTP"

    val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(Date::class.java, DateAdapter())
            .build()
    }

    fun retrofit(
        baseUrl: String,
        apiKey: String,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(
            OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    chain.proceed(
                        originalRequest.newBuilder()
                            .addHeader("Authorization", "KakaoAK $apiKey")
                            .build()
                    )
                }
                .addInterceptor(
                    HttpLoggingInterceptor {
                        Timber.tag(TAG).d(it)
                    }.setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .build()
}
