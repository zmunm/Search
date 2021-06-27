package io.github.zmunm.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.zmunm.search.databinding.ListQueryBinding
import io.github.zmunm.search.entity.Recent
import io.github.zmunm.search.ui.adapter.diff.RecentDiff
import io.github.zmunm.search.ui.adapter.viewholder.RecentViewHolder
import io.github.zmunm.search.viewmodel.SearchViewModel

class RecentAdapter(
    private val viewModel: SearchViewModel,
) : ListAdapter<Recent, RecyclerView.ViewHolder>(RecentDiff()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        RecentViewHolder(
            viewModel,
            ListQueryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecentViewHolder -> holder.bind(getItem(position))
        }
    }
}
