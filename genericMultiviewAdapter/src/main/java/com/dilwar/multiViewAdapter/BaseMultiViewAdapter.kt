package com.dilwar.multiViewAdapter

import android.util.Log
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class BaseMultiViewAdapter<M> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val list: MutableList<GViewType<M>> = emptyList<GViewType<M>>().toMutableList()

    fun setInitialList(receivedMessages: List<M>) {
        list.clear()
        receivedMessages.forEach { receivedMessage ->
            list.add(addMessageToList(receivedMessage))
        }
        notifyDataSetChanged()
    }

    fun clearList(receivedMessages: List<M>) {
        list.clear()
        notifyDataSetChanged()
    }

    fun addData(receivedMessage: M) {
        addData(list.size, receivedMessage)
    }

    fun addData(position: Int, receivedMessage: M) {
        list.add(position, addMessageToList(receivedMessage))
        notifyItemInserted(position)
    }


    override fun getItemViewType(position: Int): Int {
        return (list[position] as GViewLayout).getViewType()
    }

    @CallSuper
    abstract fun addMessageToList(receivedMessage: M): GViewType<M>

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (list[position] as GViewLayout).onBindViewHolder(holder)
    }
}