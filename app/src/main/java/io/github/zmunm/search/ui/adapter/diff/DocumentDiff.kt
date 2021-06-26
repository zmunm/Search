package io.github.zmunm.search.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.zmunm.search.entity.Document

class DocumentDiff : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean =
        oldItem.url == newItem.url

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean =
        oldItem == newItem
}
