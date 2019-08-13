package `in`.dilwar

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * GRecyclerBindingAdapter
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 *
 * @property layoutRes Layout Resource from Displaying RecyclerView
 */
class GRecyclerBindingAdapter<M, B : ViewDataBinding>
    (
    @LayoutRes val layoutRes: Int, val listener: GRecyclerBindingListener<M, B>
) : RecyclerView.Adapter<GRecyclerBindingAdapter.ViewHolder<B>>(), Filterable {


    //   var listener: GRecyclerBindingListener<M, B>? = null
    private val primaryDataList = mutableListOf<M>()
    private val filterList = mutableListOf<M>()

    /*  constructor(@LayoutRes layoutRes: Int, listener: GRecyclerBindingListener<M, B>?) : this(layoutRes) {
          this.listener = listener
      }*/


    /**
     * PopulateRecyclerView
     *
     * @param list List of Data-Model
     * @return
     */
    fun submitList(list: List<M>?): GRecyclerBindingAdapter<M, B> {
        primaryDataList.clear()
        primaryDataList.addAll(list ?: emptyList())

        filterList.clear()
        filterList.addAll(primaryDataList)

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

    override fun getItemCount() = filterList.size

    /**
     * onBindViewHolder
     *
     * @param holder viewholder
     * @param position postion of view
     */
    override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {

        listener.populateItemBindingHolder(holder.b, filterList[position], position)
        holder.b.executePendingBindings()
    }

    /**
     * ViewHolder For RecyclerView
     *
     * @param B Data Binding Class
     * @property b is the type DataBinding Class
     */
    class ViewHolder<B : ViewDataBinding>(val b: B) : RecyclerView.ViewHolder(b.root)


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val tempList = emptyList<M>().toMutableList()

                primaryDataList.forEach {
                    if (listener.itemFilter(constraint.toString(), it)) {
                        tempList.add(it)
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
