package `in`.dilwar.genericrecyclerviewadapter.normalAdapters

import android.view.View

@FunctionalInterface
interface GRecyclerNormalListener<M> {
    fun populateNormalItemHolder(view: View, data: M, position: Int)
    fun itemFilter(searchQuery: String, data: M): Boolean {
        return true
    }
}