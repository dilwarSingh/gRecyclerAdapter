package com.sample.genericrecycleradapter

import `in`.dilwar.genericrecyclerviewadapter.GRecyclerBindingAdapter
import `in`.dilwar.genericrecyclerviewadapter.GRecyclerBindingListener
import `in`.dilwar.genericrecyclerviewadapter.normalAdapters.GRecyclerNormalAdapter
import `in`.dilwar.genericrecyclerviewadapter.normalAdapters.GRecyclerNormalListener
import `in`.dilwar.genericrecyclerviewadapter.setGenericBindingAdapter
import `in`.dilwar.genericrecyclerviewadapter.setGenericNormalAdapter
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.genericrecycleradapter.dataProviders.DataModel
import com.sample.genericrecycleradapter.dataProviders.DataProvider
import com.sample.genericrecycleradapter.databinding.ActivityMainBinding
import com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding
import kotlinx.android.synthetic.main.activity_main.*

class KotlinActivity : AppCompatActivity(), GRecyclerBindingListener<DataModel, ItemRecyclerBinding>,
    GRecyclerNormalListener<DataModel> {

    lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Full DataBinding Way
        activityBinding.list = DataProvider.getDummyList("Full-DB")
        activityBinding.populate = this

        // With kotlin-Extentions and DataBinding
        rvUsingKotlinExtentionsWithDataBinding.setGenericBindingAdapter<DataModel, ItemRecyclerBinding>(
            R.layout.item_recycler,
            DataProvider.getDummyList("KE-DB")
        ) { binding, data, postion ->
            binding.data = data
            binding.executePendingBindings()
        }

        // Normal using with DataBinding
        val gNormalRecyclerAdapter = GRecyclerBindingAdapter(R.layout.item_recycler, this)
        rvNormalWithDataBinding.adapter = gNormalRecyclerAdapter
        gNormalRecyclerAdapter.submitList(DataProvider.getDummyList("NormalWith-DB"))

        // Normal using Without DataBinding
        val gNormalWithoutDBAdapter = GRecyclerNormalAdapter(R.layout.item_recycler, this)
        rvNormalWithoutDataBinding.adapter = gNormalWithoutDBAdapter
        gNormalWithoutDBAdapter.submitList(DataProvider.getDummyList("NormalWithout-DB"))

        // Normal With kotlin-Extentions
        rvNormalWithExtentions.setGenericNormalAdapter(R.layout.item_recycler, DataProvider.getDummyList("NormalKE-DB"))
        { view, data, position ->
            val textView = view.findViewById<TextView>(R.id.text)
            textView.text = data.name
        }
    }

    override fun populateItemBindingHolder(binding: ItemRecyclerBinding, data: DataModel, position: Int) {
        binding.data = data
        binding.executePendingBindings()
    }

    override fun populateNormalItemHolder(view: View, data: DataModel, position: Int) {
        val textView = view.findViewById<TextView>(R.id.text)
        textView.text = data.name
    }
}
