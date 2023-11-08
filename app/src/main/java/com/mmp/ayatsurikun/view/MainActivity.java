package com.mmp.ayatsurikun.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ActivityMainBinding;
import com.mmp.ayatsurikun.viewmodel.DeviceListViewModel;

public class MainActivity extends AppCompatActivity {

    private DeviceAdapter deviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(new DeviceListViewModel());
        setupViews();
    }

    private void setupViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_dev);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceAdapter = new DeviceAdapter((Context) this, this); //
        recyclerView.setAdapter(deviceAdapter);
    }
}