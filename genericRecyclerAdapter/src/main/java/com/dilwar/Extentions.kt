package com.dilwar

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dilwar.bindingAdapters.GRecyclerBindingAdapter
import com.dilwar.bindingAdapters.GRecyclerBindingListener
import com.dilwar.normalAdapters.GRecyclerNormalAdapter
import com.dilwar.normalAdapters.GRecyclerNormalListener

fun <M, B : ViewDataBinding> RecyclerView.setGenericBindingAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listener: (GRecyclerBindingAdapter.ViewHolder<B>, M, Int) -> Unit
): GRecyclerBindingAdapter<M, B> {
    val gAdapter = GRecyclerBindingAdapter<M, B>(
        layoutRes,
        object : GRecyclerBindingListener<M, B> {
            override fun populateItemBindingHolder(
                holder: GRecyclerBindingAdapter.ViewHolder<B>,
                data: M,
                position: Int
            ) {
                listener(holder, data, position)
            }
        }
    )
    this.adapter = gAdapter.submitList(list)
    return gAdapter
}

fun <M, B : ViewDataBinding> RecyclerView.setGenericBindingAdapter(
    @LayoutRes layoutRes: Int, listener: (GRecyclerBindingAdapter.ViewHolder<B>, M, Int) -> Unit
): GRecyclerBindingAdapter<M, B> {
    val gAdapter = GRecyclerBindingAdapter<M, B>(
        layoutRes,
        object : GRecyclerBindingListener<M, B> {
            override fun populateItemBindingHolder(
                holder: GRecyclerBindingAdapter.ViewHolder<B>,
                data: M,
                position: Int
            ) {
                listener(holder, data, position)
            }
        }
    )
    this.adapter = gAdapter.submitList()
    return gAdapter
}

fun <M, B : ViewDataBinding> RecyclerView.setGenericBindingAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listener: GRecyclerBindingListener<M, B>
): GRecyclerBindingAdapter<M, B> {
    val gAdapter = GRecyclerBindingAdapter(layoutRes, listener).submitList(list)
    this.adapter = gAdapter
    return gAdapter
}


fun <M> RecyclerView.setGenericNormalAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listener: GRecyclerNormalListener<M>
): GRecyclerNormalAdapter<M> {
    val gAdapter =
        GRecyclerNormalAdapter(layoutRes, listener).submitList(list)
    this.adapter = gAdapter
    return gAdapter
}

fun <M> RecyclerView.setGenericNormalAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listener: (GRecyclerNormalAdapter.ViewHolder, M, Int) -> Unit
): GRecyclerNormalAdapter<M> {
    val gAdapter = GRecyclerNormalAdapter(layoutRes, object : GRecyclerNormalListener<M> {
        override fun populateNormalItemHolder(
            viewHolder: GRecyclerNormalAdapter.ViewHolder,
            data: M,
            position: Int
        ) {
            listener(viewHolder, data, position)
        }

    })
    this.adapter = gAdapter.submitList(list)
    return gAdapter
}

fun <M> RecyclerView.setGenericNormalAdapter(
    @LayoutRes layoutRes: Int,
    listener: (GRecyclerNormalAdapter.ViewHolder, M, Int) -> Unit
): GRecyclerNormalAdapter<M> {
    val gAdapter = GRecyclerNormalAdapter(layoutRes, object : GRecyclerNormalListener<M> {
        override fun populateNormalItemHolder(
            viewHolder: GRecyclerNormalAdapter.ViewHolder,
            data: M,
            position: Int
        ) {
            listener(viewHolder, data, position)
        }
    })
    this.adapter = gAdapter
    return gAdapter
}



