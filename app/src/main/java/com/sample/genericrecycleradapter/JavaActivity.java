package com.sample.genericrecycleradapter;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.sample.genericrecycleradapter.dataProviders.DataModel;
import com.sample.genericrecycleradapter.dataProviders.DataProvider;
import com.sample.genericrecycleradapter.databinding.ActivityMainBinding;
import com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding;
import in.dilwar.genericrecyclerviewadapter.GRecyclerAdapter;
import in.dilwar.genericrecyclerviewadapter.GRecyclerHolderListener;
import org.jetbrains.annotations.NotNull;

public class JavaActivity extends AppCompatActivity implements GRecyclerHolderListener<DataModel, ItemRecyclerBinding> {

    ActivityMainBinding activityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Full Data-Binding Way
        activityBinding.setList(DataProvider.getDummyList("Full-DB"));
        activityBinding.setPopulate(this);

        // Normal using with databinding
        GRecyclerAdapter<DataModel, ItemRecyclerBinding> gNormalRecyclerAdapter
                = new GRecyclerAdapter<>(R.layout.item_recycler, this);
        activityBinding.rvNormalWithDataBinding.setAdapter(gNormalRecyclerAdapter);
        gNormalRecyclerAdapter.submitList(DataProvider.getDummyList("Normal"));

    }

    @Override
    public void populateItemHolder(@NotNull ItemRecyclerBinding binding, DataModel data, int position) {
        binding.setData(data);
        binding.executePendingBindings();
    }
}
