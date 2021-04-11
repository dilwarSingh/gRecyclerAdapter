package com.dilwar.multiViewAdapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

interface GViewType<T> {
    fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder, data: T)

    @LayoutRes
    fun layoutId(): Int
}
