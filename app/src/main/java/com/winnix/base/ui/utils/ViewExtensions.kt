package com.winnix.base.ui.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import timber.log.Timber

/** Ignores clicks that arrive within [debounceTime] ms of the last handled one. */
fun View.setPreventDoubleClick(debounceTime: Long = 500, action: () -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        val now = SystemClock.elapsedRealtime()
        if (now - lastClickTime < debounceTime) return@setOnClickListener
        lastClickTime = now
        action()
    }
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

/** VISIBLE when [condition] holds, GONE otherwise. */
fun View.showIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

/** GONE when [condition] holds, VISIBLE otherwise. */
fun View.goneIf(condition: Boolean) = showIf(!condition)

/** No-op once the fragment is detached, so late callbacks cannot crash on a null context. */
fun Fragment.displayToast(msg: String) {
    val context = context ?: return
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.displayToast(@StringRes msg: Int) {
    val context = context ?: return
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Float get() = this * Resources.getSystem().displayMetrics.density

fun Context.openBrowser(url: String) {
    val fullUrl = if (url.startsWith("http://") || url.startsWith("https://")) url else "https://$url"
    try {
        startActivity(Intent(Intent.ACTION_VIEW, fullUrl.toUri()))
    } catch (exception: ActivityNotFoundException) {
        Timber.w(exception, "No browser can open %s", fullUrl)
    }
}
