package `in`.dilwar.genericrecyclerviewadapter.normalAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class GRecyclerNormalAdapter<M>(@LayoutRes val layoutRes: Int, val listener: GRecyclerNormalListener<M>) :
    RecyclerView.Adapter<GRecyclerNormalAdapter.ViewHolder>() {

    private val dataList = mutableListOf<M>()


    fun submitList(list: List<M>?): GRecyclerNormalAdapter<M> {
        dataList.clear()
        dataList.addAll(list ?: emptyList())
        notifyDataSetChanged()
        return this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = dataList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        listener.populateNormalItemHolder(holder.view, dataList[position], position)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
