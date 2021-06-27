package io.github.zmunm.search.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import io.github.zmunm.search.entity.Recent

class RecentDiff : DiffUtil.ItemCallback<Recent>() {
    override fun areItemsTheSame(oldItem: Recent, newItem: Recent): Boolean =
        oldItem.query == newItem.query

    override fun areContentsTheSame(oldItem: Recent, newItem: Recent): Boolean =
        oldItem == newItem
}
