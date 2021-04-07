package com.sample.genericrecycleradapter

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dilwar.GRecyclerFilterListener
import com.dilwar.bindingAdapters.GRecyclerBindingAdapter
import com.dilwar.bindingAdapters.GRecyclerBindingListener
import com.dilwar.normalAdapters.GRecyclerNormalAdapter
import com.dilwar.normalAdapters.GRecyclerNormalListener
import com.dilwar.setGenericBindingAdapter
import com.dilwar.setGenericNormalAdapter
import com.sample.genericrecycleradapter.dataProviders.DataModel
import com.sample.genericrecycleradapter.dataProviders.DataProvider
import com.sample.genericrecycleradapter.databinding.ActivityMainBinding
import com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding
import kotlinx.android.synthetic.main.activity_main.*

class KotlinActivity : AppCompatActivity(), GRecyclerNormalListener<DataModel>,
    GRecyclerBindingListener<DataModel, ItemRecyclerBinding>,
    GRecyclerFilterListener<DataModel> {


    lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
// Initialization of you dataBinding Layout
        // Full DataBinding Way
        activityBinding.list = DataProvider.getDummyList("Full-DB")
        // Provide a List of your Custom model here


        //  activityBinding.populate = this

        // With kotlin-Extentions and DataBinding
        val rvUsingKotlinExtentionsWithDataBindingAdapter =
            rvUsingKotlinExtentionsWithDataBinding.setGenericBindingAdapter<DataModel, ItemRecyclerBinding>(
                R.layout.item_recycler
            ) { vh, data, postion ->
                vh.binding.all = data
                vh.binding.executePendingBindings()
            }
        rvUsingKotlinExtentionsWithDataBindingAdapter.submitList(DataProvider.getDummyList("KE-DB"))
        // Normal using with DataBinding
        val gNormalRecyclerAdapter = GRecyclerBindingAdapter(
            R.layout.item_recycler,
            this
        )
        rvNormalWithDataBinding.adapter = gNormalRecyclerAdapter
        gNormalRecyclerAdapter.submitList(DataProvider.getDummyList("NormalWith-DB"))
        gNormalRecyclerAdapter.filter.filter("2")

        // Normal using Without DataBinding
        val gNormalWithoutDBAdapter = GRecyclerNormalAdapter(R.layout.item_recycler, this)
        rvNormalWithoutDataBinding.adapter = gNormalWithoutDBAdapter
        gNormalWithoutDBAdapter.submitList(DataProvider.getDummyList("NormalWithout-DB"))

        // Normal With kotlin-Extentions
        val gAdapter = rvNormalWithExtentions.setGenericNormalAdapter<DataModel>(
            R.layout.item_recycler/*Your Recycler Item Layout*/
        ) { viewHolder, data, position ->
            val textView = viewHolder.view.findViewById<TextView>(R.id.text)
            textView.text = data.name
        }
        gAdapter.submitList(DataProvider.getDummyList("NormalKE-DB"))

    }

    override fun populateItemBindingHolder(
        holder: GRecyclerBindingAdapter.ViewHolder<ItemRecyclerBinding>,
        data: DataModel,
        position: Int
    ) {
        holder.binding.all = data
        holder.binding.executePendingBindings()
    }

    override fun populateNormalItemHolder(
        viewHolder: GRecyclerNormalAdapter.ViewHolder,
        data: DataModel, position: Int
    ) {
        val textView = viewHolder.view.findViewById<TextView>(R.id.text)
        textView.text = data.name
    }

    override fun itemFilter(searchQuery: String, data: DataModel): Boolean {
        return data.name.contains(searchQuery)
    }
}
