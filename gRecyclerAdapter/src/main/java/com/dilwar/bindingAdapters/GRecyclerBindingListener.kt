package com.dilwar.bindingAdapters

import androidx.databinding.ViewDataBinding
import com.dilwar.GRecyclerFilterListener
import com.dilwar.bindingAdapters.GRecyclerBindingAdapter.ViewHolder

/**
 * TODO
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 */
interface GRecyclerBindingListener<M, B : ViewDataBinding> : GRecyclerFilterListener<M> {

    fun populateItemBindingHolder(holder: ViewHolder<B>, data: M, position: Int)

    override fun itemFilter(searchQuery: String, data: M): Boolean {
        return true
    }
}