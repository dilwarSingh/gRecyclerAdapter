package com.sample.genericrecycleradapter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dilwar.bindingAdapters.GRecyclerBindingAdapter;
import com.dilwar.bindingAdapters.GRecyclerBindingListener;
import com.dilwar.normalAdapters.GRecyclerNormalAdapter;
import com.dilwar.normalAdapters.GRecyclerNormalListener;
import com.sample.genericrecycleradapter.dataProviders.DataModel;
import com.sample.genericrecycleradapter.dataProviders.DataProvider;
import com.sample.genericrecycleradapter.databinding.ActivityMainBinding;
import com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding;

import org.jetbrains.annotations.NotNull;

public class JavaActivity extends AppCompatActivity
        implements //GRecyclerBindingListener<DataModel, ItemRecyclerBinding>,

        GRecyclerBindingListener
                <com.sample.genericrecycleradapter.dataProviders.DataModel,
                        com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding>,


        GRecyclerNormalListener<DataModel> {

    ActivityMainBinding activityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Full DataBinding Way
        activityBinding.setList(DataProvider.getDummyList("Full-DB"));
        activityBinding.setPopulate((GRecyclerBindingListener) this);

        // Normal using with DataBinding
        GRecyclerBindingAdapter<DataModel, ItemRecyclerBinding> gNormalRecyclerAdapter
                = new GRecyclerBindingAdapter<>(R.layout.item_recycler, this);
        activityBinding.rvNormalWithDataBinding.setAdapter(gNormalRecyclerAdapter);
        gNormalRecyclerAdapter.submitList(DataProvider.getDummyList("NormalWith-DB"));

        // Normal using Without DataBinding
        GRecyclerNormalAdapter<DataModel> gNormalWithoutDBAdapter = new GRecyclerNormalAdapter<>(R.layout.item_recycler, this);
        activityBinding.rvNormalWithoutDataBinding.setAdapter(gNormalWithoutDBAdapter);
        gNormalWithoutDBAdapter.submitList(DataProvider.getDummyList("NormalWithout-DB"));

    }

    @Override
    public boolean itemFilter(@NotNull String searchQuery, DataModel data) {
        return false;
    }

    @Override
    public void populateItemBindingHolder(@NotNull GRecyclerBindingAdapter.ViewHolder<ItemRecyclerBinding> holder, DataModel data, int position) {

        holder.getBinding().setAll(data);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public void populateNormalItemHolder(@NotNull GRecyclerNormalAdapter.ViewHolder viewHolder, DataModel data, int position) {
        TextView textView = viewHolder.getView().findViewById(R.id.text);
        textView.setText(data.getName());
    }
}
