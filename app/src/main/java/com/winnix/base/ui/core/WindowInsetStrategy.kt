package com.winnix.base.ui.core

import android.view.View

sealed interface WindowInsetStrategy {

    data class AutoHandleView(
        val topInsetView: View? = null,
        val bottomInsetView: View? = null,
        val listenToKeyboard: Boolean = false,
        val ignoreSystemBarVisibility: Boolean = false,
    ) : WindowInsetStrategy

    data class ManualHandle(
        val listenToKeyboard: Boolean = false,
        val onInsetsReceived: (topPadding: Int, bottomPadding: Int) -> Unit,
    ) : WindowInsetStrategy

    object None : WindowInsetStrategy
}
