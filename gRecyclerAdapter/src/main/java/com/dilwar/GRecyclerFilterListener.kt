package com.dilwar

/**
 * TODO
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 */
@FunctionalInterface
interface GRecyclerFilterListener<M> {
    fun itemFilter(searchQuery: String, data: M): Boolean
}