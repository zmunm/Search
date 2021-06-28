package io.github.zmunm.search.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.github.zmunm.search.databinding.ListFilterBinding
import io.github.zmunm.search.entity.DocumentType

class FilterListAdapter internal constructor(
    private val list: List<DocumentType>,
    private val onClick: (DocumentType) -> Unit
) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(position: Int): DocumentType = list[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val holder: ViewHolder
        if (convertView == null) {
            val itemBinding = ListFilterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            holder = ViewHolder(itemBinding)
            holder.binding.root.setOnClickListener {
                holder.binding.document?.let {
                    onClick(it)
                }
            }
            holder.binding.root.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.binding.document = getItem(position)
        return holder.binding.root
    }

    private class ViewHolder constructor(
        val binding: ListFilterBinding
    )
}
