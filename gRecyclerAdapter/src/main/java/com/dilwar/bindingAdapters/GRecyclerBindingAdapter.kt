package com.dilwar.bindingAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dilwar.BR
import com.dilwar.GRecyclerFilterListener
import com.dilwar.normalAdapters.ListData

class GRecyclerBindingAdapter<M, B : ViewDataBinding> private constructor(
    @LayoutRes val layoutRes: Int,
    override val listener: GRecyclerFilterListener<M>?,
    override val primaryDataList: MutableList<M>,
    override val filterList: MutableList<M> = mutableListOf(),
    override var filterText: String = ""
) :
    RecyclerView.Adapter<GRecyclerBindingAdapter.ViewHolder<B>>(), ListData<M> {
    override val adapter: RecyclerView.Adapter<*>
        get() = this

    private var populateListener: GRecyclerBindingListener<M, B>? = null
    private var brRes: Int = 0

    constructor(
        @LayoutRes layoutRes: Int,
        populateListener: GRecyclerBindingListener<M, B>?
    ) : this(
        layoutRes, populateListener as? GRecyclerFilterListener<M>, mutableListOf()
    ) {
        this.populateListener = populateListener
    }

    constructor(@LayoutRes layoutRes: Int, brRes: Int) : this(layoutRes, null) {
        this.brRes = brRes
    }

    constructor(@LayoutRes layoutRes: Int, brRes: Int, primaryDataList: List<M>?) : this(
        layoutRes, brRes
    ) {
        submitList(primaryDataList)
    }

    fun submitList(list: List<M>? = emptyList()): GRecyclerBindingAdapter<M, B> {
        primaryDataList.clear()
        notifyDataSetChanged()
        primaryDataList.addAll(list ?: emptyList())

        filterList.clear()
        filterList.addAll(primaryDataList)

        notifyDataSetChanged()
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        val bindingUtil = DataBindingUtil
            .inflate<B>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return ViewHolder(bindingUtil)
    }

    override fun getItemCount() = filterList.size

    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        if (populateListener != null) {
            populateListener!!.populateItemBindingHolder(holder, filterList[position], position)
        } else {
            if (brRes == -1)
                holder.binding.setVariable(BR._all, filterList[position])
            else
                holder.binding.setVariable(brRes, filterList[position])
        }
        holder.binding.executePendingBindings()
    }

    class ViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

}
