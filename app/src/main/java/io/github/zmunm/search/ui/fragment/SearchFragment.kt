package io.github.zmunm.search.ui.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.ListPopupWindow
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.github.zmunm.search.R
import io.github.zmunm.search.databinding.FragmentSearchBinding
import io.github.zmunm.search.entity.DocumentType
import io.github.zmunm.search.entity.SortType
import io.github.zmunm.search.hideKeyboard
import io.github.zmunm.search.ui.adapter.FilterListAdapter
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

        viewModel.action.observe(viewLifecycleOwner) { action ->
            when (action) {
                is SearchViewModel.Action.DocumentFilter ->
                    showFilterListPopup(binding.type, action)
                is SearchViewModel.Action.Sort ->
                    showSortDialog(action)
            }
        }
    }

    private fun showFilterListPopup(
        anchorView: View,
        action: SearchViewModel.Action.DocumentFilter
    ) {
        val popup = ListPopupWindow(requireContext())
        popup.setAdapter(FilterListAdapter(DocumentType.values().toList()) {
            action.callBack(it)
            popup.dismiss()
        })
        popup.anchorView = anchorView
        popup.show()
    }

    private fun showSortDialog(sortAction: SearchViewModel.Action.Sort) {
        var sort = sortAction.default
        val list = SortType.values()
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.sort_title)
            .setSingleChoiceItems(
                list.map { it.name }.toTypedArray(),
                list.indexOf(sort)
            ) { _, which ->
                sort = list[which]
            }
            .setPositiveButton(R.string.sort_cancel, null)
            .setNegativeButton(R.string.sort_ok) { _, which ->
                sortAction.callBack(sort)
            }
            .show()
    }
}
