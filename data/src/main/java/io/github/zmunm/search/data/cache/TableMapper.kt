package io.github.zmunm.search.data.cache

import io.github.zmunm.search.data.cache.table.TableDocument
import io.github.zmunm.search.data.cache.table.TableVisit
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.Visit

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

internal fun TableVisit.toEntity(): Visit = Visit(
    url = url,
    datetime = datetime,
)
