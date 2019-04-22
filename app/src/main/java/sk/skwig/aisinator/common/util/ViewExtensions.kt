package sk.skwig.aisinator.common.util

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ViewAnimator

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun ViewAnimator.showChild(view: View) {
    indexOfChild(view)
        .takeIf { it != -1 }
        ?.also { displayedChild = it }
}

fun MenuItem.dontDismissOnClick(context: Context?) {
    setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
    actionView = View(context)
    setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
            return false
        }

        override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
            return false
        }
    })
}