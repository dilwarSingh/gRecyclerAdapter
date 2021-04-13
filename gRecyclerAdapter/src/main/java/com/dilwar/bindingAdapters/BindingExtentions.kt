package com.dilwar.bindingAdapters

import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * function to be used with databinding calling from XML
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 * @param rv Your RecyclerView
 * @param list List of Data-Model
 * @param layoutRes Layout Resource from Displaying RecyclerView
 * @param populateListener List to tell RecyclerView how to populate Its Child Views
 */
@BindingAdapter(value = ["dataList", "layoutRes", "populateListener"], requireAll = true)
fun <M, B : ViewDataBinding> setCustomAdapter(
    rv: RecyclerView,
    list: List<M>?,
    @LayoutRes layoutRes: Int,
    populateListener: GRecyclerBindingListener<M, B>
) {
    val adapter = GRecyclerBindingAdapter(layoutRes, populateListener)
    rv.adapter = adapter.submitList(list)
}

/**
 * function to be used with databinding calling from XML
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 * @param rv Your RecyclerView
 * @param list List of Data-Model
 * @param layoutRes Layout Resource from Displaying RecyclerView
 * @param brRes List to tell RecyclerView how to populate Its Child Views
 */
@BindingAdapter(value = ["dataList", "layoutRes", "brRes"], requireAll = true)
fun <M, B : ViewDataBinding> setCustomAdapter(
    rv: RecyclerView, list: List<M>?, @LayoutRes layoutRes: Int, brRes: Int
) {
    val adapter = GRecyclerBindingAdapter<M, ViewDataBinding>(layoutRes, brRes)
    rv.adapter = adapter.submitList(list)
}

/**
 * function to be used with databinding calling from XML
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 * @param rv Your RecyclerView
 * @param list List of Data-Model
 * @param layoutRes Layout Resource from Displaying RecyclerView
 * @param brRes List to tell RecyclerView how to populate Its Child Views
 */
@BindingAdapter(value = ["dataList", "layoutRes"], requireAll = true)
fun <M, B : ViewDataBinding> setCustomAdapter(
    rv: RecyclerView, list: List<M>?, @LayoutRes layoutRes: Int
) {
    val adapter = GRecyclerBindingAdapter<M, ViewDataBinding>(layoutRes, -1)
    rv.adapter = adapter.submitList(list)
}

