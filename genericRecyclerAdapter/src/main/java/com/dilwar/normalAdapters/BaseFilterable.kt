package com.dilwar.normalAdapters

import android.widget.Filter
import android.widget.Filterable
import com.dilwar.GRecyclerFilterListener

interface BaseFilterable<M> : Filterable {
    val listener: GRecyclerFilterListener<M>?
    val primaryDataList: MutableList<M>

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val tempList = emptyList<M>().toMutableList()

                if (constraint.toString().isEmpty()) {
                    tempList.addAll(primaryDataList)
                } else {
                    if (listener != null) {
                        primaryDataList.forEach {
                            if (listener!!.itemFilter(constraint.toString(), it)) {
                                tempList.add(it)
                            }
                        }
                    } else {
                        tempList.addAll(primaryDataList)
                    }
                }

                val filterResult = FilterResults()
                filterResult.values = tempList
                filterResult.count = tempList.size

                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                val list = results?.values as? List<M> ?: emptyList()
                publishResult(list, constraint.toString())
            }
        }
    }

    fun publishResult(list: List<M>, filterText: String)

}