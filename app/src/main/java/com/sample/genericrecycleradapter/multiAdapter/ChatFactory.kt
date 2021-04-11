package com.sample.genericrecycleradapter.multiAdapter

import com.dilwar.annotations.GRecyclerViewFactory
import com.dilwar.multiViewAdapter.GViewFactory
import com.sample.genericrecycleradapter.R
import com.sample.genericrecycleradapter.multiAdapter.asd.ReceiveMessageType
import com.sample.genericrecycleradapter.multiAdapter.asd.SendMessageType
import g.recycler.resources.GResources

@GRecyclerViewFactory([SendMessageType::class, ReceiveMessageType::class], enableDataBinding = true)
open class ChatFactory : GViewFactory<MessageModel> {

    override fun getViewType(data: MessageModel): Int {
        return if (data.user) GResources.sendMessageType
        else GResources.receiveMessageType
    }
}