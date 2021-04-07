package com.sample.genericrecycleradapter.multiAdapter.asd


import androidx.recyclerview.widget.RecyclerView
import com.dilwar.annotations.GRecyclerViewType
import com.dilwar.multiViewAdapter.GViewType
import com.sample.genericrecycleradapter.R
import com.sample.genericrecycleradapter.databinding.ItemSendMessageBinding
import com.sample.genericrecycleradapter.multiAdapter.MessageModel
import dilwar.GRecycler.com.sample.genericrecycleradapter.multiAdapter.ChatFactoryViewHolders

@GRecyclerViewType(layout = R.layout.item_send_message)
open class SendMessageType : GViewType<MessageModel> {

    override fun onBindViewHolder(recyclerViewHolder: RecyclerView.ViewHolder, data: MessageModel) {
        val holder =
            recyclerViewHolder as ChatFactoryViewHolders.SendMessageTypeRow
        val binding = holder.binding as ItemSendMessageBinding
        binding.text.text = data.msg
    }
}
