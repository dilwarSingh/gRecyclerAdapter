package com.sample.genericrecycleradapter.multiAdapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.genericrecycleradapter.R
import com.sample.genericrecycleradapter.databinding.ActivityMultiViewBinding

class MultiViewActivity : AppCompatActivity() {
    lateinit var activityMultiViewBinding: ActivityMultiViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMultiViewBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_multi_view)

        val chatFactoryAdapter = MyAdapter()
        activityMultiViewBinding.multiRecycler.adapter = chatFactoryAdapter

        activityMultiViewBinding.sendBt.setOnClickListener {
            com.sample.genericrecycleradapter.multiAdapter.MessageModel(
                activityMultiViewBinding.switch1.isChecked,
                activityMultiViewBinding.messageET.text.toString()
            ).apply {
                chatFactoryAdapter.addData(this)
            }
            activityMultiViewBinding.multiRecycler.smoothScrollToPosition(chatFactoryAdapter.list.size - 1)
        }
    }
}
