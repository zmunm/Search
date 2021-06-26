package io.github.zmunm.search.data.cache

import io.github.zmunm.search.data.cache.dao.DocumentDao
import io.github.zmunm.search.data.cache.impl.DocumentCacheImpl
import io.github.zmunm.search.data.cache.table.TableDocument
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentType
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.Date
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class DocumentCacheSpec : DescribeSpec({
    val documentDao: DocumentDao = mockk()

    val documentCache: DocumentCache by lazy {
        DocumentCacheImpl(documentDao)
    }

    val date1 = Date()
    val date2 = Date()

    val url1 = "url1"
    val url2 = "url2"

    val tableDocument = TableDocument(
        documentType = DocumentType.ALL,
        name = "name1",
        contents = "contents1",
        datetime = date1,
        thumbnail = "thumbnail1",
        title = "title1",
        url = url1,
    )

    val document = Document(
        documentType = DocumentType.ALL,
        name = "name2",
        contents = "contents2",
        datetime = date2,
        thumbnail = "thumbnail2",
        title = "title2",
        url = url2,
    )

    describe("getDocument") {
        it("empty") {
            every { documentDao.getDocument(url1) } returns emptyFlow()

            documentCache.getDocument(url1).collect {}
        }

        it("document1") {
            every { documentDao.getDocument(url1) } returns flowOf(tableDocument)

            documentCache.getDocument(url1).collect {
                it.url shouldBe url1
            }
        }

        verify { documentDao.getDocument(url1) }
    }

    describe("insertDocument") {
        val slot = slot<TableDocument>()
        coEvery { documentDao.upsert(capture(slot)) } just Runs

        documentCache.insertDocument(document)
        slot.captured.url shouldBe url2

        coVerify { documentDao.upsert(capture(slot)) }
    }

    afterContainer {
        confirmVerified(documentDao)
        clearMocks(documentDao)
    }
})
