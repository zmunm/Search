package io.github.zmunm.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import io.github.zmunm.search.databinding.ListDocumentBinding
import io.github.zmunm.search.entity.Document
import io.github.zmunm.search.ui.adapter.diff.AlwaysFailDiff
import io.github.zmunm.search.ui.adapter.viewholder.DocumentViewHolder
import io.github.zmunm.search.viewmodel.DocumentViewModel
import javax.inject.Inject
import javax.inject.Provider

class SearchAdapter @Inject constructor(
    private val viewModelProvider: Provider<DocumentViewModel>,
) : PagingDataAdapter<Document, RecyclerView.ViewHolder>(AlwaysFailDiff()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        DocumentViewHolder(
            ListDocumentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is DocumentViewHolder -> {
                holder.bind(
                    viewModelProvider.get().apply {
                        getItem(position)?.let(::bindDocument)
                    }
                )
            }
        }
    }
}
