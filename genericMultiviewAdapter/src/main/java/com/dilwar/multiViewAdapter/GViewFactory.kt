package com.dilwar.multiViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

interface GViewFactory<T> {
    fun getViewType(data: T): Int

    companion object {
        fun getView(parent: ViewGroup, layout: Int) =
            LayoutInflater.from(parent.context).inflate(layout, parent, false)
    }
}
