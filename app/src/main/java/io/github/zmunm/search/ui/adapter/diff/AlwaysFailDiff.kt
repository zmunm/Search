package io.github.zmunm.search.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil

class AlwaysFailDiff<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = false
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = false
}
