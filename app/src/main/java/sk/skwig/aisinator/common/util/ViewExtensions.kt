package sk.skwig.aisinator.common.util

import android.view.LayoutInflater
import android.view.View
import android.widget.ViewAnimator

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun ViewAnimator.showChild(view: View) {
    this.displayedChild = indexOfChild(view)
}