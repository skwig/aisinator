package sk.skwig.aisinator.util

import android.view.LayoutInflater
import android.view.View

val View.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(context)