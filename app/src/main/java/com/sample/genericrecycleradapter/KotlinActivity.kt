package com.sample.genericrecycleradapter

import `in`.dilwar.genericrecyclerviewadapter.GRecyclerAdapter
import `in`.dilwar.genericrecyclerviewadapter.GRecyclerHolderListener
import `in`.dilwar.genericrecyclerviewadapter.setGenericAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.sample.genericrecycleradapter.dataProviders.DataModel
import com.sample.genericrecycleradapter.dataProviders.DataProvider
import com.sample.genericrecycleradapter.databinding.ActivityMainBinding
import com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding
import kotlinx.android.synthetic.main.activity_main.*

class KotlinActivity : AppCompatActivity(), GRecyclerHolderListener<DataModel, ItemRecyclerBinding> {

    lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Full Data-Binding Way
        activityBinding.list = DataProvider.getDummyList("Full-DB")
        activityBinding.populate = this

        // With kotlin-Extentions and Databinding
        rvUsingKotlinExtentionsWithDataBinding.setGenericAdapter(
            R.layout.item_recycler,
            DataProvider.getDummyList("KX-DB"),
            this
        )

        // Normal using with databinding
        val gNormalRecyclerAdapter = GRecyclerAdapter(R.layout.item_recycler, this)
        rvNormalWithDataBinding.adapter = gNormalRecyclerAdapter
        gNormalRecyclerAdapter.submitList(DataProvider.getDummyList("Normal"))

    }

    override fun populateItemHolder(binding: ItemRecyclerBinding, data: DataModel, position: Int) {
        binding.data = data
        binding.executePendingBindings()
    }

}
