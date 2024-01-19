package com.mmp.ayatsurikun.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.databinding.DeviceItemBinding;
import com.mmp.ayatsurikun.model.Device;
import com.mmp.ayatsurikun.viewmodel.DeviceListViewModel;

public class DeviceAdapter extends ListAdapter<Device, DeviceAdapter.DeviceViewHolder> {
    private final DeviceListViewModel viewModel;

    public DeviceAdapter(DeviceListViewModel viewModel) {
        super(new DiffUtil.ItemCallback<Device>() {
            @Override
            public boolean areItemsTheSame(@NonNull Device oldItem, @NonNull Device newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Device oldItem, @NonNull Device newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DeviceViewHolder(DeviceItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, final int position) {
        holder.bind(getItem(position), viewModel);
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final DeviceItemBinding binding;
        public DeviceViewHolder(DeviceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Device device, DeviceListViewModel viewModel) {
            binding.setVariable(com.mmp.ayatsurikun.BR.device, device);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }
}