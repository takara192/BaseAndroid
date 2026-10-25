package com.winnix.base.ui.core.base

import androidx.viewbinding.ViewBinding

/**
 * Shared binding access for fragments and dialogs. Use [binding] in code that runs while the view is
 * alive (setup, render); use [withBinding] in callbacks that can fire late (delays, animation ends).
 */
interface ViewBindingOwner<VB : ViewBinding> {
    /** Null before onCreateView and after onDestroyView. */
    val bindingOrNull: VB?

    val binding: VB
        get() = checkNotNull(bindingOrNull) {
            "Binding accessed outside the view lifecycle; use withBinding {} in delayed or animation callbacks"
        }

    /** Runs [block] only while the view exists; a late callback is silently dropped. */
    fun withBinding(block: VB.() -> Unit) {
        bindingOrNull?.block()
    }
}
