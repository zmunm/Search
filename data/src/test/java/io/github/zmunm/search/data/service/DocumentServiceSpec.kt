package io.github.zmunm.search.data.service

import io.github.zmunm.search.data.InternalInstance
import io.github.zmunm.search.data.service.api.DocumentApi
import io.github.zmunm.search.data.service.impl.DocumentServiceImpl
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestContext
import okhttp3.mockwebserver.MockWebServer

private const val CODE_UNAUTHORIZED = 401

internal class DocumentServiceSpec : DescribeSpec({
    val server = MockWebServer()

    val documentService: DocumentService by lazy {
        DocumentServiceImpl(
            InternalInstance.retrofit(
                server.url("/v2/").toString(),
                "KEY",
            ).create(DocumentApi::class.java),
        )
    }

    beforeSpec {
        server.start()
    }

    describe("fetch blogs") {
        it("fetchBlogs1") {
            server.enqueue(getResponseFromResource().toMockResponse())
            documentService.fetchBlogs(
                query = "query",
                sort = null,
                page = null,
                size = null,
            )
        }

        it("error") {
            server.enqueue(getResponseFromResource().toMockResponse(CODE_UNAUTHORIZED))
            documentService.fetchBlogs(
                query = "query",
                sort = null,
                page = null,
                size = null,
            )
        }
    }

    afterSpec {
        server.shutdown()
    }
})

private fun TestContext.getResponseFromResource(): String {
    val resource = javaClass.classLoader
        ?.getResourceAsStream("${testCase.displayName}.json") ?: error(testCase.displayName)
    return resource.bufferedReader().readText()
}
