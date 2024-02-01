package com.mmp.ayatsurikun.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ActivityDeviceControlBinding;
import com.mmp.ayatsurikun.viewmodel.DeviceControlViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DeviceControlActivity extends AppCompatActivity {
    private ActivityDeviceControlBinding binding;
    private DeviceControlViewModel viewModel;
    @Inject
    SignalListFragment signalListFragment;
    @Inject
    ScheduleListFragment scheduleListFragment;
    private AddSignalDialogFragment addDialogFragment;

    public static void start(Context context) {
        final Intent intent = new Intent(context, DeviceControlActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_device_control);
        viewModel = new ViewModelProvider(this).get(DeviceControlViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.connect(((App)getApplicationContext()).getDevice());
        viewModel.getSignal().observe(this, bytes -> {
            if(bytes != null) startAddSignalDialog(bytes);
        });
        setupToolbar();
        setBottomNavigation();
    }

    @Override
    public void onPause() {
        if (addDialogFragment != null && addDialogFragment.isAdded()) {
            addDialogFragment.dismiss();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        viewModel.disconnect();
        super.onDestroy();
    }

    private void startAddSignalDialog(byte[] signal) {
        if(addDialogFragment != null && addDialogFragment.isAdded()) addDialogFragment.dismiss();
        addDialogFragment = new AddSignalDialogFragment(viewModel, signal);
        addDialogFragment.show(getSupportFragmentManager(), "AddButtonDialogFragment");
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setBottomNavigation() {
        binding.bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_menu_signal) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, signalListFragment)
                        .commitNow();
                return true;
            } else if (item.getItemId() == R.id.bottom_menu_schedule) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, scheduleListFragment)
                        .commitNow();
                return true;
            }
            return false;
        });
        binding.bottomNav.setSelectedItemId(R.id.bottom_menu_signal);
    }
}