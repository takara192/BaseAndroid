package com.winnix.base.ui.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/** Shows [dialog] once per [tag]; ignored when the state is already saved, so it never throws. */
fun Fragment.showDialog(dialog: DialogFragment, tag: String) {
    if (!isAdded || isDetached) return
    childFragmentManager.showOnce(dialog, tag)
}

fun Fragment.dismissDialog(tag: String) {
    if (!isAdded || isDetached) return
    childFragmentManager.dismissByTag(tag)
}

/** Shows [dialog] once per [tag]; ignored when the state is already saved, so it never throws. */
fun FragmentActivity.showDialog(dialog: DialogFragment, tag: String) {
    if (isFinishing || isDestroyed) return
    supportFragmentManager.showOnce(dialog, tag)
}

fun FragmentActivity.dismissDialog(tag: String) {
    if (isDestroyed) return
    supportFragmentManager.dismissByTag(tag)
}

private fun FragmentManager.showOnce(dialog: DialogFragment, tag: String) {
    if (isStateSaved || isDestroyed || findFragmentByTag(tag) != null) return
    dialog.show(this, tag)
}

private fun FragmentManager.dismissByTag(tag: String) {
    (findFragmentByTag(tag) as? DialogFragment)?.dismissAllowingStateLoss()
}
