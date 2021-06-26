package io.github.zmunm.search.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.zmunm.search.Params
import io.github.zmunm.search.R
import io.github.zmunm.search.databinding.FragmentDetailBinding
import io.github.zmunm.search.ui.activity.WebViewActivity
import io.github.zmunm.search.ui.base.BaseFragment
import io.github.zmunm.search.viewmodel.DetailViewModel

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private val viewModel: DetailViewModel by viewModels()

    override val layoutId: Int
        get() = R.layout.fragment_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFragmentManager.commit {
                    remove(this@DetailFragment)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.urlFlow.observe(viewLifecycleOwner) { action ->
            when (action) {
                is DetailViewModel.Action.Visit -> {
                    requireContext().startActivity(
                        Intent(requireContext(), WebViewActivity::class.java)
                            .putExtra(Params.URL, action.document.url)
                            .putExtra(Params.TITLE, action.document.title)
                    )
                }
            }
        }
    }

    companion object {
        fun newInstance(url: String) = DetailFragment().apply {
            arguments = bundleOf(Params.URL to url)
        }
    }
}
