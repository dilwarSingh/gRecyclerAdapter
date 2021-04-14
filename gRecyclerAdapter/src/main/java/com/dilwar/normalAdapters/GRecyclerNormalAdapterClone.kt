package com.dilwar.normalAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

private class GRecyclerNormalAdapterClone<M>(
    @LayoutRes val layoutRes: Int,
    val listener: GRecyclerNormalListener<M>
) :
    RecyclerView.Adapter<GRecyclerNormalAdapterClone.ViewHolder>(), Filterable {

    private val primaryDataList = mutableListOf<M>()
    private val filterList = mutableListOf<M>()

    fun submitList(list: List<M>?): GRecyclerNormalAdapterClone<M> {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = filterList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //   listener.populateNormalItemHolder(holder, filterList[position], position)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val tempList = emptyList<M>().toMutableList()

                if (constraint.toString().isEmpty()) {
                    tempList.addAll(primaryDataList)
                } else {
                    primaryDataList.forEach {
                        if (listener.itemFilter(constraint.toString(), it)) {
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
