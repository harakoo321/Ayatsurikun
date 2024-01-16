package com.mmp.ayatsurikun.view;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.contract.DeviceListViewContract;
import com.mmp.ayatsurikun.databinding.DeviceItemBinding;
import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.viewmodel.DeviceItemViewModel;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private final DeviceListViewContract view;
    private final Context context;
    private List<Device> items;

    public DeviceAdapter(Context context, DeviceListViewContract view) {
        this.view = view;
        this.context = context;
    }

    public void setItemsAndRefresh(List<Device> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public Device getItemAt(int position) {
        return items.get(position);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.device_item, parent, false);
        binding.setViewModel(new DeviceItemViewModel(view));
        binding.setLifecycleOwner((LifecycleOwner) view);
        return new DeviceViewHolder(binding.getRoot(), binding.getViewModel());
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {
        final Device item = getItemAt(position);
        holder.loadItem(item);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final DeviceItemViewModel viewModel;

        public DeviceViewHolder(View itemView, DeviceItemViewModel viewModel) {
            super(itemView);
            this.viewModel = viewModel;
        }

        public void loadItem(Device item) {
            viewModel.loadItem(item);
        }
    }
}