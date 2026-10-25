package com.winnix.base.ui.utils

import android.animation.Animator
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
 * Work started from a fragment's view must die with that view, not with the fragment: the fragment
 * outlives its view on the back stack, so anything tied to `lifecycleScope` can touch a destroyed binding.
 * Call these between onViewCreated and onDestroyView.
 */

/** Launches in the view's scope; cancelled (delays included) when the view is destroyed. */
fun Fragment.launchOnView(block: suspend CoroutineScope.() -> Unit): Job =
    viewLifecycleOwner.lifecycleScope.launch(block = block)

/** Runs [block] each time the view reaches [state] and cancels it below; the usual way to collect flows. */
fun Fragment.repeatOnView(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend CoroutineScope.() -> Unit,
): Job = launchOnView { viewLifecycleOwner.repeatOnLifecycle(state, block) }

/** postDelayed replacement: never runs after the view is destroyed; cancel the returned job to drop it sooner. */
fun Fragment.postDelayedOnView(delayMillis: Long, action: () -> Unit): Job = launchOnView {
    delay(delayMillis)
    action()
}

/** Runs [action] once when the current view is destroyed, while its binding is still set. */
fun Fragment.doOnViewDestroy(action: () -> Unit) {
    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) = action()
    })
}

/** Cancels the animator with the view; listeners are removed first so their end callbacks never fire late. */
fun <T : Animator> T.cancelOnViewDestroy(fragment: Fragment): T = apply {
    fragment.doOnViewDestroy {
        removeAllListeners()
        cancel()
    }
}

/** Same as [Animator.cancelOnViewDestroy] for Lottie, whose listeners outlive the view otherwise. */
fun LottieAnimationView.cancelOnViewDestroy(fragment: Fragment): LottieAnimationView = apply {
    fragment.doOnViewDestroy {
        removeAllAnimatorListeners()
        removeAllUpdateListeners()
        cancelAnimation()
    }
}
