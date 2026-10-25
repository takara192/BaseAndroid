package com.winnix.base.ui.main

import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.winnix.base.R
import com.winnix.base.databinding.FragmentMainBinding
import com.winnix.base.ui.core.WindowInsetStrategy
import com.winnix.base.ui.core.base.BaseFragment
import com.winnix.base.ui.utils.repeatOnView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val viewModel: MainViewModel by viewModels()

    private val adapter = SampleItemAdapter()

    override val insetStrategy: WindowInsetStrategy by lazy {
        WindowInsetStrategy.ManualHandle { top, bottom -> binding.rvItems.updatePadding(top = top, bottom = bottom) }
    }

    override fun setupView() {
        binding.rvItems.adapter = adapter
    }

    override fun onDestroyView() {
        // The adapter outlives the view; detaching it lets the old RecyclerView be collected.
        binding.rvItems.adapter = null
        super.onDestroyView()
    }

    override fun setupObserver() {
        repeatOnView {
            launch { viewModel.uiState.collect(::render) }
            launch { viewModel.events.collect(::handleEvent) }
        }
    }

    private fun render(state: MainUiState) {
        adapter.submitList(state.items)
        binding.progress.isVisible = state.isRefreshing && state.items.isEmpty()
        binding.tvEmpty.isVisible = state.isEmpty
    }

    private fun handleEvent(event: MainEvent) = when (event) {
        is MainEvent.ShowError -> Snackbar.make(binding.root, event.message.resolve(requireContext()), Snackbar.LENGTH_LONG)
            .setAction(R.string.action_retry) { viewModel.refresh() }
            .show()
    }
}
