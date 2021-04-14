package com.sample.genericrecycleradapter.multiAdapter.asd


import androidx.recyclerview.widget.RecyclerView
import com.dilwar.annotations.GRecyclerViewType
import com.dilwar.multiViewAdapter.GViewType
import com.sample.genericrecycleradapter.R
import com.sample.genericrecycleradapter.databinding.ItemSendMessageBinding
import com.sample.genericrecycleradapter.multiAdapter.MessageModel
import g.recycler.com.sample.genericrecycleradapter.multiAdapter.ChatFactoryViewHolders

@GRecyclerViewType
open class SendMessageType : GViewType<MessageModel> {

    override fun layoutId() = R.layout.item_send_message;

    override fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder, model: MessageModel) {
        val holder =
            recyclerViewHolder as ChatFactoryViewHolders.SendMessageTypeRow
        val binding = holder.binding as ItemSendMessageBinding
        binding.text.text = model.msg
    }
}
