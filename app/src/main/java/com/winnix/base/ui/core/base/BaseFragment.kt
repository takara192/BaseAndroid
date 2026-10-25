package com.winnix.base.ui.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.winnix.base.ui.core.WindowInsetStrategy

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment(), ViewBindingOwner<VB> {
    private var _binding: VB? = null
    override val bindingOrNull: VB? get() = _binding

    protected open val insetStrategy: WindowInsetStrategy = WindowInsetStrategy.None

    protected val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding?.root?.let { ViewCompat.setOnApplyWindowInsetsListener(it, null) }
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWindowInsets(binding.root)

        setupView()
        setUpListener()
        setupObserver()
    }

    open fun setupView() {}
    open fun setUpListener() {}
    open fun setupObserver() {}

    private fun setupWindowInsets(rootView: View) {
        val strategy = insetStrategy
        if (strategy is WindowInsetStrategy.None) return
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, windowInsets ->
            when (strategy) {
                is WindowInsetStrategy.AutoHandleView -> {
                    val requestedTypes = safeAreaTypes(strategy.listenToKeyboard)
                    val insets = if (strategy.ignoreSystemBarVisibility && !strategy.listenToKeyboard) {
                        windowInsets.getInsetsIgnoringVisibility(requestedTypes)
                    } else {
                        windowInsets.getInsets(requestedTypes)
                    }

                    updateInsetHeight(strategy.topInsetView, insets.top)
                    updateInsetHeight(strategy.bottomInsetView, insets.bottom)
                }

                is WindowInsetStrategy.ManualHandle -> {
                    val insets = windowInsets.getInsets(safeAreaTypes(strategy.listenToKeyboard))
                    strategy.onInsetsReceived(insets.top, insets.bottom)
                }

                WindowInsetStrategy.None -> Unit
            }
            windowInsets
        }
        ViewCompat.requestApplyInsets(rootView)
    }

    private fun safeAreaTypes(listenToKeyboard: Boolean): Int {
        val types = WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
        return if (listenToKeyboard) types or WindowInsetsCompat.Type.ime() else types
    }

    private fun updateInsetHeight(view: View?, height: Int) {
        val params = view?.layoutParams ?: return
        if (params.height != height) {
            params.height = height
            view.layoutParams = params
        }
    }
}
