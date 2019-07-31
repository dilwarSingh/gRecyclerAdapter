package `in`.dilwar.genericrecyclerviewadapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * GRecyclerAdapter
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 *
 * @property layoutRes Layout Resource from Displaying RecyclerView
 */
class GRecyclerAdapter<M, B : ViewDataBinding>
    (
    @LayoutRes val layoutRes: Int
) : RecyclerView.Adapter<GRecyclerAdapter.ViewHolder<B>>() {

    var listener: GRecyclerHolderListener<M, B>? = null
    private val dataList = mutableListOf<M>()

    constructor(@LayoutRes layoutRes: Int, listener: GRecyclerHolderListener<M, B>?) : this(layoutRes) {
        this.listener = listener
    }


    /**
     * PopulateRecyclerView
     *
     * @param list List of Data-Model
     * @return
     */
    fun submitList(list: List<M>?): GRecyclerAdapter<M, B> {
        dataList.clear()
        dataList.addAll(list ?: emptyList())
        notifyDataSetChanged()
        return this
    }

    /**
     * onCreateViewHolder
     *
     * @param parent ViewGroup
     * @param viewType Integer
     * @return
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
        val bindingUtil = DataBindingUtil
            .inflate<B>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return ViewHolder(bindingUtil)
    }

    override fun getItemCount() = dataList.size

    /**
     * onBindViewHolder
     *
     * @param holder viewholder
     * @param position postion of view
     */
    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
      /*  if (listener == null) {
            holder.b.setVariable(`in`.dilwar.genericrecyclerviewadapter.BR.data, dataList[position])
        } else {*/
            listener!!.populateItemHolder(holder.b, dataList[position], position)
     //   }
        holder.b.executePendingBindings()
    }

    /**
     * ViewHolder For RecyclerView
     *
     * @param B Data Binding Class
     * @property b is the type DataBinding Class
     */
    class ViewHolder<B : ViewDataBinding>(val b: B) : RecyclerView.ViewHolder(b.root)
}
