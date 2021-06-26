package io.github.zmunm.search.data.service

import okhttp3.mockwebserver.MockResponse

internal fun String.toMockResponse(code: Int = 200): MockResponse = MockResponse()
    .addHeader("Content-Type", "application/json; charset=utf-8")
    .setResponseCode(code)
    .setBody(this)
