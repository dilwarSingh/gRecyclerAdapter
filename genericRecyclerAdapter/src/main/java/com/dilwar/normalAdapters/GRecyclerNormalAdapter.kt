package com.dilwar.normalAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class GRecyclerNormalAdapter<M> private constructor(
    override val listener: GRecyclerNormalListener<M>,
    @LayoutRes val layoutRes: Int,
    override val primaryDataList: MutableList<M> = emptyList<M>().toMutableList(),
    override val filterList: MutableList<M> = emptyList<M>().toMutableList(),
    override var filterText: String = ""
) : RecyclerView.Adapter<GRecyclerNormalAdapter.ViewHolder>(), ListData<M> {

    constructor(
        @LayoutRes layoutRes: Int, listener: GRecyclerNormalListener<M>
    ) : this(listener, layoutRes)

    fun submitList(list: List<M>?): GRecyclerNormalAdapter<M> {
        primaryDataList.clear()
        primaryDataList.addAll(list ?: emptyList())

        filterList.clear()
        filterList.addAll(primaryDataList)

        notifyDataSetChanged()
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listener.populateNormalItemHolder(holder, filterList[position], position)
    }

    override val adapter: RecyclerView.Adapter<*>
        get() = this

    override fun publishResult(list: List<M>, filterText: String) {
        this.filterText = filterText
        filterList.clear()
        filterList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
