package ru.deltadelete.lab13.utils

import android.graphics.PointF
import android.view.View
import android.view.ViewPropertyAnimator
import androidx.core.view.doOnDetach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun View.scale(scale: Float) {
    scaleX = scale
    scaleY = scale
}

fun View.setPivot(point: PointF) {
    pivotX = point.x
    pivotY = point.y
}

fun View.animateWithDetach(): ViewPropertyAnimator {
    doOnDetach { it.animate().cancel() }
    return animate()
}

fun <T> LiveData<T>.observe(
    lifecycleOwner: LifecycleOwner,
    observer: (T) -> Unit
) {
    this.observe(lifecycleOwner, observer)
}

fun RecyclerView.addOnScrolled(func: (view: RecyclerView, dx: Int, dy: Int) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            func(recyclerView, dx, dy)
        }
    })
}

fun <T : Any, R : Any> T.doIf(
    predicate: ((scope: T) -> Boolean),
    action: (T.() -> R)
): Any {
    if (predicate(this)) {
        return this.action()
    }
    return this
}