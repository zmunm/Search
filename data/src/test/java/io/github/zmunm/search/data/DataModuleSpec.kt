package io.github.zmunm.search.data

import io.kotest.core.spec.style.FunSpec
import io.mockk.mockk

internal class DataModuleSpec : FunSpec({
    test("provideDocumentService") {
        DataModule.provideDocumentService("key")
    }

    test("provideDocumentCache") {
        DataModule.provideDocumentCache(mockk())
    }

    test("provideDocumentRepository") {
        DataModule.provideDocumentRepository(mockk(), mockk())
    }
})
