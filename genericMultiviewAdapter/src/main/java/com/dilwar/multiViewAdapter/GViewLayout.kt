package com.dilwar.multiViewAdapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

interface GViewLayout {
    @LayoutRes
    fun getLayoutId(): Int

    fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder)

}