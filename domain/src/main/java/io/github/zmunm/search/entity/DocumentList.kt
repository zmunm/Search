package io.github.zmunm.search.entity

data class DocumentList(
    val documentList: List<Document>,
    val hasNext: DocumentType?,
) {
    operator fun plus(other: DocumentList): DocumentList {
        val nextSet = setOfNotNull(hasNext, other.hasNext)
        val types = DocumentType.values()
        return DocumentList(
            documentList = documentList + other.documentList,
            hasNext = when {
                nextSet.size == types.size -> DocumentType.ALL
                nextSet.isEmpty() -> null
                else -> nextSet.single()
            }
        )
    }
}
