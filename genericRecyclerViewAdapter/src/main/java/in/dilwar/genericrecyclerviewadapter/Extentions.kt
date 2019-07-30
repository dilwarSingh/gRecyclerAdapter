package `in`.dilwar.genericrecyclerviewadapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 *
 * @param M Data-Model to be used in RecyclerView Adapter
 * @param B DataBindingClass Name For the Layout
 * @param list List of Data-Model
 * @param layoutRes Layout Resource from Displaying RecyclerView
 * @return
 */
fun <M, B : ViewDataBinding> RecyclerView.setMyAdapter(@LayoutRes layoutRes: Int, list: List<M>? = emptyList()): GRecyclerAdapter<M, B> {
    val adapter = GRecyclerAdapter<M, B>(layoutRes)
    return adapter.submitList(list)
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
fun <M, B : ViewDataBinding> RecyclerView.setMyAdapter(
    @LayoutRes layoutRes: Int, list: List<M>? = emptyList(),
    listner: (B, M, Int) -> Unit
): GRecyclerAdapter<M, B> {
    val adapter = GRecyclerAdapter(
        layoutRes,
        object : GRecyclerHolderListener<M, B> {
            override fun populateItemHolder(binding: B, data: M, position: Int) {
                listner(binding, data, position)
            }
        })

    return adapter.submitList(list)
}
