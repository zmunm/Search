package io.github.zmunm.search.ui.adapter.viewholder

import androidx.fragment.app.commit
import io.github.zmunm.search.R
import io.github.zmunm.search.databinding.ListDocumentBinding
import io.github.zmunm.search.getActivity
import io.github.zmunm.search.hideKeyboard
import io.github.zmunm.search.ui.base.BaseViewHolder
import io.github.zmunm.search.ui.fragment.DetailFragment
import io.github.zmunm.search.viewmodel.DocumentViewModel

class DocumentViewHolder(
    private val binding: ListDocumentBinding,
) : BaseViewHolder(binding) {
    init {
        binding.setClickListener { view ->
            binding.viewModel?.let { viewModel ->
                viewModel.document.value?.let {
                    viewModel.putDocument()
                    val activity = view.getActivity()
                    hideKeyboard(activity)
                    activity.supportFragmentManager.commit {
                        add(
                            R.id.fragment_container,
                            DetailFragment.newInstance(it.url)
                        ).addToBackStack(DetailFragment::class.simpleName)
                    }
                }
            }
        }
    }

    fun bind(viewModel: DocumentViewModel) {
        binding.viewModel = viewModel
    }
}
