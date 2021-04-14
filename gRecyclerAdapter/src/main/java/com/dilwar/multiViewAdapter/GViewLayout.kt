package com.dilwar.multiViewAdapter

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

interface GViewLayout {

    fun getViewType(): Int

    fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder)

}