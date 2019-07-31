package `in`.dilwar.genericrecyclerviewadapter

import androidx.databinding.ViewDataBinding

/**
 * TODO
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 */
@FunctionalInterface
interface GRecyclerBindingListener<M, B : ViewDataBinding> {
    /**
     * TODO List to tell RecyclerView how to populate Its Child Views
     *
     * @param binding binding layout
     * @param data data
     * @param position postion of the view
     */
    fun populateItemBindingHolder(binding: B, data: M, position: Int)

}