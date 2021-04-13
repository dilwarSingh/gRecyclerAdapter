package com.sample.genericrecycleradapter.multiAdapter.asd


import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dilwar.annotations.GRecyclerViewType
import com.dilwar.multiViewAdapter.GViewType
import com.sample.genericrecycleradapter.R
import com.sample.genericrecycleradapter.multiAdapter.MessageModel

@GRecyclerViewType
open class ReceiveMessageType : GViewType<MessageModel> {

    override fun layoutId() = R.layout.item_recieve_message;

    override fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder, model: MessageModel) {

        recyclerViewHolder.itemView.findViewById<TextView>(R.id.text).text = model.msg

        /* val holder =
             recyclerViewHolder as ChatFactoryViewHolders.RecieveMessageTypeRow
         val binding = holder.binding as ItemRecieveMessageBinding
         binding.text.text = data.msg*/
    }
}
