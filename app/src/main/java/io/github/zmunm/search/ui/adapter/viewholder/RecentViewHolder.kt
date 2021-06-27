package io.github.zmunm.search.ui.adapter.viewholder

import io.github.zmunm.search.databinding.ListQueryBinding
import io.github.zmunm.search.entity.Recent
import io.github.zmunm.search.ui.base.BaseViewHolder
import io.github.zmunm.search.viewmodel.SearchViewModel

class RecentViewHolder(
    private val viewModel: SearchViewModel,
    private val binding: ListQueryBinding,
) : BaseViewHolder(binding) {
    init {
        binding.viewModel = viewModel
    }

    fun bind(item: Recent) {
        binding.item = item
    }
}
