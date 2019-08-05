package com.sample.genericrecycleradapter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.sample.genericrecycleradapter.dataProviders.DataModel;
import com.sample.genericrecycleradapter.dataProviders.DataProvider;
import com.sample.genericrecycleradapter.databinding.ActivityMainBinding;
import com.sample.genericrecycleradapter.databinding.ItemRecyclerBinding;
import in.dilwar.genericrecyclerviewadapter.GRecyclerBindingAdapter;
import in.dilwar.genericrecyclerviewadapter.GRecyclerBindingListener;
import in.dilwar.genericrecyclerviewadapter.normalAdapters.GRecyclerNormalAdapter;
import in.dilwar.genericrecyclerviewadapter.normalAdapters.GRecyclerNormalListener;
import org.jetbrains.annotations.NotNull;

public class JavaActivity extends AppCompatActivity
        implements GRecyclerBindingListener<DataModel, ItemRecyclerBinding>,
        GRecyclerNormalListener<DataModel> {

    ActivityMainBinding activityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        // Full DataBinding Way
        activityBinding.setList(DataProvider.getDummyList("Full-DB"));
        activityBinding.setPopulate(this);

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
    public void populateItemBindingHolder(@NotNull ItemRecyclerBinding binding, DataModel data, int position) {
        binding.setData(data);
        binding.executePendingBindings();
    }

    @Override
    public void populateNormalItemHolder(@NotNull View view, DataModel data, int position) {
        TextView textView = view.findViewById(R.id.text);
        textView.setText(data.getName());
    }


    @Override
    public boolean itemFilter(@NotNull String searchQuery, DataModel data) {
        return false;
    }
}
