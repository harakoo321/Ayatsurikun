package com.mmp.ayatsurikun.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.FragmentScheduleListBinding;
import com.mmp.ayatsurikun.model.Schedule;
import com.mmp.ayatsurikun.viewmodel.ScheduleListViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScheduleListFragment extends Fragment {
    private ScheduleListViewModel viewModel;
    private ScheduleAdapter scheduleAdapter;
    @Inject
    ScheduleDialogFragment dialog;
    private FragmentScheduleListBinding binding;

    @Inject
    public ScheduleListFragment() {
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
        viewModel = new ViewModelProvider(this).get(ScheduleListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        setupMenuBar();
        setupRecyclerView();
        viewModel.getAllSchedulesAndSignals().observe(getViewLifecycleOwner(), scheduleAdapter::submitList);
        viewModel.getLongClickedSchedule().observe(getViewLifecycleOwner(), schedule -> {
            if (schedule != null) startDeleteScheduleDialog(schedule);
        });
    }

    private void setupMenuBar() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(R.string.schedule_title);
        ((MenuHost)requireActivity()).addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                inflater.inflate(R.menu.add_schedule_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == android.R.id.home) requireActivity().finish();
                else if (item.getItemId() == R.id.add_schedule_button) {
                    if (dialog != null && dialog.isAdded()) dialog.dismiss();
                    Objects.requireNonNull(dialog).show(getParentFragmentManager(), "schedule_dialog");
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.recyclerSchedule;
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        scheduleAdapter = new ScheduleAdapter(viewModel);
        recyclerView.setAdapter(scheduleAdapter);
    }

    private void startDeleteScheduleDialog(Schedule schedule) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.confirm);
        builder.setMessage(schedule.getDateTimeString() + getString(R.string.delete_confirm));
        builder.setPositiveButton(R.string.delete, (dialog, which) ->  viewModel.deleteSchedule(schedule));
        builder.setNegativeButton(R.string.dialog_btn_ng, (dialog, which) -> viewModel.clearLongClickedSchedule());
        builder.create();
        builder.show();
    }
}