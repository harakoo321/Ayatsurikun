package com.mmp.ayatsurikun.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mmp.ayatsurikun.BR;
import com.mmp.ayatsurikun.databinding.SignalItemBinding;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.DeviceControlViewModel;

public class SignalAdapter extends ListAdapter<Signal, SignalAdapter.SignalViewHolder> {
    private final DeviceControlViewModel viewModel;
    public SignalAdapter(DeviceControlViewModel viewModel) {
        super(new DiffUtil.ItemCallback<Signal>() {
            @Override
            public boolean areItemsTheSame(@NonNull Signal oldItem, @NonNull Signal newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Signal oldItem, @NonNull Signal newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public SignalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SignalViewHolder(SignalItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(final SignalViewHolder holder, final int position) {
        holder.bind(getItem(position), viewModel);
    }

    public static class SignalViewHolder extends RecyclerView.ViewHolder {
        private final SignalItemBinding binding;
        public SignalViewHolder(SignalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(Signal signal, DeviceControlViewModel viewModel) {
            binding.setVariable(BR.signal, signal);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }
}
