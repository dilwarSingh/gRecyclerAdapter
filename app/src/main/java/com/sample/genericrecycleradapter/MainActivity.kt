package com.sample.genericrecycleradapter

import `in`.dilwar.genericrecyclerviewadapter.GRecyclerAdapter
import `in`.dilwar.genericrecyclerviewadapter.GRecyclerHolderListener
import `in`.dilwar.genericrecyclerviewadapter.databinding.ItemRecyclerSampleBinding
import `in`.dilwar.genericrecyclerviewadapter.setGenericAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GRecyclerHolderListener<String, ItemRecyclerSampleBinding> {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvVertical.setGenericAdapter(R.layout.item_recycler_sample, dummyList(), this)
        // rvVertical.adapter = genericAdapter
      //  rvVertical.adapter = GRecyclerAdapter(R.layout.item_recycler_sample, this).submitList(dummyList())
        //    rvHorizontal.setGenericAdapter(R.layout.item_recycler_sample, dummyList(), this)
    }


    override fun populateItemHolder(binding: ItemRecyclerSampleBinding, data: String, position: Int) {
        binding.data = data
        binding.executePendingBindings()
    }

    private fun dummyList(): List<String> {
        val list = mutableListOf<String>()

        for (i in 1 until 100) {
            list.add(i.toString())
        }
        return list
    }

}
