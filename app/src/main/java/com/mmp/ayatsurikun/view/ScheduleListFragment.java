package com.mmp.ayatsurikun.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.databinding.FragmentScheduleListBinding;
import com.mmp.ayatsurikun.viewmodel.ScheduleListViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScheduleListFragment extends Fragment {
    @Inject
    ScheduleDialogFragment dialog;
    private FragmentScheduleListBinding binding;

    @Inject
    public ScheduleListFragment() {
    }

    public static ScheduleListFragment newInstance() {
        return new ScheduleListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ScheduleListViewModel viewModel = new ViewModelProvider(this).get(ScheduleListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.scheduleToolbar.setNavigationOnClickListener(v -> requireActivity().finish());
        RecyclerView recyclerView = binding.recyclerSchedule;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(viewModel);
        recyclerView.setAdapter(scheduleAdapter);
        viewModel.getAllSchedulesAndSignals().observe(getViewLifecycleOwner(), scheduleAdapter::submitList);
        viewModel.getLongClickedSchedule().observe(getViewLifecycleOwner(), schedule -> {
            if (schedule != null) dialog.show(getParentFragmentManager(), "schedule_dialog");
        });
    }
}