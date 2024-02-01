package com.mmp.ayatsurikun.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.mmp.ayatsurikun.BR;
import com.mmp.ayatsurikun.databinding.ScheduleItemBinding;
import com.mmp.ayatsurikun.model.ScheduleAndSignal;
import com.mmp.ayatsurikun.viewmodel.ScheduleListViewModel;

public class ScheduleAdapter extends ListAdapter<ScheduleAndSignal, ScheduleAdapter.ScheduleViewHolder> {
    private final ScheduleListViewModel viewModel;

    public ScheduleAdapter(ScheduleListViewModel viewModel) {
        super(new DiffUtil.ItemCallback<ScheduleAndSignal>() {
            @Override
            public boolean areItemsTheSame(@NonNull ScheduleAndSignal oldItem, @NonNull ScheduleAndSignal newItem) {
                return oldItem.schedule.getId() == newItem.schedule.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ScheduleAndSignal oldItem, @NonNull ScheduleAndSignal newItem) {
                return oldItem.schedule.equals(newItem.schedule);
            }
        });
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ScheduleViewHolder(ScheduleItemBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBindViewHolder(final ScheduleViewHolder holder, final int position) {
        holder.bind(getItem(position), viewModel);
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        private final ScheduleItemBinding binding;

        public ScheduleViewHolder(ScheduleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(ScheduleAndSignal scheduleAndSignal, ScheduleListViewModel viewModel) {
            binding.setVariable(BR.scheduleAndSignal, scheduleAndSignal);
            binding.setViewModel(viewModel);
            binding.executePendingBindings();
        }
    }
}
