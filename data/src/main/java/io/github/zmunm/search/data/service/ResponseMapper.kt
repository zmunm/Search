package io.github.zmunm.search.data.service

import io.github.zmunm.search.data.service.dao.ResponseBlog
import io.github.zmunm.search.data.service.dao.ResponseCafe
import io.github.zmunm.search.data.service.dao.ResponseList
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.entity.DocumentList
import io.github.zmunm.search.entity.DocumentType

internal fun <T> ResponseList<T>.toEntity(type: DocumentType): DocumentList = DocumentList(
    documents.mapNotNull { responseDocument ->
        when (responseDocument) {
            is ResponseBlog -> responseDocument.toEntity()
            is ResponseCafe -> responseDocument.toEntity()
            else -> null
        }
    },
    if (meta.is_end) null else type
)

internal fun ResponseBlog.toEntity(): Document = Document(
    documentType = DocumentType.BLOG,
    name = blogname,
    contents = contents,
    datetime = datetime,
    thumbnail = thumbnail,
    title = title,
    url = url,
)

internal fun ResponseCafe.toEntity(): Document = Document(
    documentType = DocumentType.CAFE,
    name = cafename,
    contents = contents,
    datetime = datetime,
    thumbnail = thumbnail,
    title = title,
    url = url,
)
