package com.mmp.ayatsurikun.view;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.DeviceItemBinding;
import com.mmp.ayatsurikun.model.DeviceService;
import com.mmp.ayatsurikun.viewmodel.DeviceItemViewModel;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private final MainActivity view;
    private final Context context;
    private List<DeviceService.DeviceItem> items;

    public DeviceAdapter(Context context, MainActivity view) {
        this.view = view;
        this.context = context;
    }

    public void setItemsAndRefresh(List<DeviceService.DeviceItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.device_item, parent, false);
        binding.setViewModel(new DeviceItemViewModel(view));
        return new DeviceViewHolder(binding.getRoot(), binding.getViewModel());
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, int position) {
        final DeviceService.DeviceItem item = items.get(position);
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

        public void loadItem(DeviceService.DeviceItem item) {
            viewModel.loadItem(item);
        }
    }
}