package com.dilwar.normalAdapters

import com.dilwar.GRecyclerFilterListener
import com.dilwar.normalAdapters.GRecyclerNormalAdapter.ViewHolder

@FunctionalInterface
interface GRecyclerNormalListener<M> : GRecyclerFilterListener<M> {
    fun populateNormalItemHolder(viewHolder: ViewHolder, data: M, position: Int)

    override fun itemFilter(searchQuery: String, data: M): Boolean {
        return true
    }
}