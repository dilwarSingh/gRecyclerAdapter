package `in`.dilwar.normalAdapters

import android.view.View

@FunctionalInterface
interface GRecyclerNormalListener<M> {
    fun populateNormalItemHolder(view: View, data: M, position: Int)
    fun itemFilter(searchQuery: String, data: M): Boolean {
        return true
    }
}