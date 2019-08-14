package `in`.dilwar.genericrecyclerviewadapter

import `in`.dilwar.genericrecyclerviewadapter.normalAdapters.GRecyclerNormalAdapter
import `in`.dilwar.genericrecyclerviewadapter.normalAdapters.GRecyclerNormalListener
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


/**
 * TODO
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 * @param list List of Data-Model
 * @param layoutRes Layout Resource from Displaying RecyclerView
 * @param listner List to tell RecyclerView how to populate Its Child Views
 * @return
 */
fun <M, B : ViewDataBinding> RecyclerView.setGenericBindingAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listner: (B, M, Int) -> Unit
): GRecyclerBindingAdapter<M, B> {
    val gAdapter = GRecyclerBindingAdapter(
        layoutRes,
        object : GRecyclerBindingListener<M, B> {
            override fun populateItemBindingHolder(binding: B, data: M, position: Int) {
                listner(binding, data, position)
            }
        })
    this.adapter = gAdapter.submitList(list)
    return gAdapter
}

/**
 * TODO
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 * @param list List of Data-Model
 * @param layoutRes Layout Resource from Displaying RecyclerView
 * @param listner List to tell RecyclerView how to populate Its Child Views
 * @return
 */
fun <M, B : ViewDataBinding> RecyclerView.setGenericBindingAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listner: GRecyclerBindingListener<M, B>
): GRecyclerBindingAdapter<M, B> {
    val gAdapter = GRecyclerBindingAdapter(layoutRes, listner).submitList(list)
    this.adapter = gAdapter
    // val adapter = GRecyclerBindingAdapter(layoutRes, listner)
    return gAdapter
}


fun <M> RecyclerView.setGenericNormalAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listner: GRecyclerNormalListener<M>
): GRecyclerNormalAdapter<M> {
    val gAdapter = GRecyclerNormalAdapter(layoutRes, listner).submitList(list)
    this.adapter = gAdapter
    return gAdapter
}

fun <M> RecyclerView.setGenericNormalAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listner: (View, M, Int) -> Unit
): GRecyclerNormalAdapter<M> {
    val gAdapter = GRecyclerNormalAdapter(layoutRes, object : GRecyclerNormalListener<M> {
        override fun populateNormalItemHolder(view: View, data: M, position: Int) {
            listner(view, data, position)
        }

    })
    this.adapter = gAdapter.submitList(list)
    return gAdapter
}



