package com.dilwar.normalAdapters

import androidx.recyclerview.widget.RecyclerView

interface ListData<M> : BaseFilterable<M> {
    val adapter: RecyclerView.Adapter<*>
    val filterList: MutableList<M>
    var filterText: String

    fun getAllItems(): List<M> {
        return primaryDataList
    }

    fun getFilteredList(): List<M> {
        return filterList
    }

    fun addItem(item: M) {
        addItem(item, primaryDataList.size)
    }

    fun addItem(item: M, position: Int) {
        primaryDataList.add(position, item)
        //filterList.add(position, item)
        filter.filter(filterText)
        //    adapter.notifyItemInserted(position)
    }

    fun removeItemAt(position: Int) {
        val data = filterList[position]
        removeItem(data)
    }

    fun removeItem(data: M) {
        primaryDataList.remove(data)
        filterList.remove(data)
        adapter.notifyDataSetChanged()
    }

    override fun publishResult(list: List<M>, filterText: String) {
        this.filterText = filterText
        filterList.clear()
        filterList.addAll(list)
        adapter.notifyDataSetChanged()
    }
}