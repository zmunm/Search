package io.github.zmunm.search.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.github.zmunm.search.R
import io.github.zmunm.search.databinding.FragmentSearchBinding
import io.github.zmunm.search.hideKeyboard
import io.github.zmunm.search.ui.adapter.RecentAdapter
import io.github.zmunm.search.ui.adapter.SearchAdapter
import io.github.zmunm.search.ui.base.BaseFragment
import io.github.zmunm.search.viewmodel.SearchViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var adapter: SearchAdapter

    private val viewModel: SearchViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_search

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.list.adapter = adapter
        val recentAdapter = RecentAdapter(viewModel)
        binding.recentQuery.adapter = recentAdapter

        viewModel.pager.observe(viewLifecycleOwner) { data ->
            hideKeyboard(requireActivity())
            lifecycleScope.launch {
                adapter.submitData(data)
            }
        }

        viewModel.recentQuery.observe(viewLifecycleOwner) {
            recentAdapter.submitList(it)
        }

        binding.search.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.search()
                true
            } else false
        }
    }
}
