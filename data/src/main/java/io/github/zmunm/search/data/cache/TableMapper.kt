package io.github.zmunm.search.data.cache

import io.github.zmunm.search.data.cache.table.TableDocument
import io.github.zmunm.search.entity.Document

internal fun Document.toTable(): TableDocument = TableDocument(
    url = url,
    documentType = documentType,
    name = name,
    contents = contents,
    datetime = datetime,
    thumbnail = thumbnail,
    title = title,
)

internal fun TableDocument.toEntity(): Document = Document(
    url = url,
    documentType = documentType,
    name = name,
    contents = contents,
    datetime = datetime,
    thumbnail = thumbnail,
    title = title,
)
