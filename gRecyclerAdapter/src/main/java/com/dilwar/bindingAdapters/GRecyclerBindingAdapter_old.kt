package com.dilwar.bindingAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

private class GRecyclerBindingAdapter_old<M, B : ViewDataBinding>
private constructor(@LayoutRes val layoutRes: Int) :
    RecyclerView.Adapter<GRecyclerBindingAdapter_old.ViewHolder<B>>(), Filterable {

    private val primaryDataList = mutableListOf<M>()
    private val filterList = mutableListOf<M>()
    private var populateListener: GRecyclerBindingListener<M, B>? = null
    private var br: Int? = null

    constructor(
        @LayoutRes layoutRes: Int,
        populateListener: GRecyclerBindingListener<M, B>?
    ) : this(
        layoutRes
    ) {
        this.populateListener = populateListener
    }

    constructor(@LayoutRes layoutRes: Int, br: Int) : this(layoutRes) {
        this.br = br
    }

    fun submitList(list: List<M>?): GRecyclerBindingAdapter_old<M, B> {
        primaryDataList.clear()
        primaryDataList.addAll(list ?: emptyList())

        filterList.clear()
        filterList.addAll(primaryDataList)

        notifyDataSetChanged()
        return this
    }

    fun getAllItems(): List<M> {
        return primaryDataList
    }

    fun getFilteredList(): List<M> {
        return filterList
    }

    fun addItem(item: M) {
        addItem(item, filterList.size)
    }

    fun addItem(item: M, position: Int) {
        primaryDataList.add(position, item)
        filterList.add(position, item)
        notifyItemInserted(position)
    }

    fun removeItemAt(position: Int) {
        val data = filterList[position]
        removeItem(data)
    }

    fun removeItem(data: M) {
        primaryDataList.remove(data)
        filterList.remove(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        val bindingUtil = DataBindingUtil
            .inflate<B>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return ViewHolder(bindingUtil)
    }

    override fun getItemCount() = filterList.size


    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
        if (populateListener != null) {
            //  populateListener!!.populateItemBindingHolder(holder, filterList[position], position)
        } else {
            holder.binding.setVariable(br!!, filterList[position])
        }
        holder.binding.executePendingBindings()
    }


    class ViewHolder<B : ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val tempList = emptyList<M>().toMutableList()
                if (constraint.toString().isEmpty()) {
                    tempList.addAll(primaryDataList)
                } else {
                    primaryDataList.forEach {
                        if (populateListener != null) {
                            if (populateListener!!.itemFilter(constraint.toString(), it))
                                tempList.add(it)
                        }
                    }
                }
                val filterResult = FilterResults()
                filterResult.values = tempList
                filterResult.count = tempList.size

                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val list = results?.values as? List<M> ?: emptyList()
                filterList.clear()
                filterList.addAll(list)
                notifyDataSetChanged()
            }

        }
    }
}
