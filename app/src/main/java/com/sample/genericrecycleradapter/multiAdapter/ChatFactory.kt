package com.sample.genericrecycleradapter.multiAdapter

import com.dilwar.annotations.GRecyclerViewFactory
import com.dilwar.multiViewAdapter.GViewFactory
import com.sample.genericrecycleradapter.R
import com.sample.genericrecycleradapter.multiAdapter.asd.RecieveMessageType
import com.sample.genericrecycleradapter.multiAdapter.asd.SendMessageType

@GRecyclerViewFactory([SendMessageType::class, RecieveMessageType::class], enableDataBinding = true)
open class ChatFactory : GViewFactory<MessageModel> {

    override fun getViewTypeLayoutId(data: MessageModel): Int {
        return if (data.user) R.layout.item_send_message
        else R.layout.item_recieve_message
    }
}
