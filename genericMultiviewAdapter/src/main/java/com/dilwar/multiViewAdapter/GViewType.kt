package com.dilwar.multiViewAdapter

import androidx.recyclerview.widget.RecyclerView

interface GViewType<T> {
    fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder, data: T)

}
