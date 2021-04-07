package com.sample.genericrecycleradapter.multiAdapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sample.genericrecycleradapter.R
import kotlinx.android.synthetic.main.activity_multi_view.*

class MultiViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_view)

        val chatFactoryAdapter = MyAdapter()
        multiRecycler.adapter = chatFactoryAdapter

        sendBt.setOnClickListener {
            com.sample.genericrecycleradapter.multiAdapter.MessageModel(
                switch1.isChecked,
                messageET.text.toString()
            ).apply {
                chatFactoryAdapter.addData(this)
            }
            multiRecycler.smoothScrollToPosition(chatFactoryAdapter.list.size - 1)
        }
    }
}
