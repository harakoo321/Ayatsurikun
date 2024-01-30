package com.mmp.ayatsurikun.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.databinding.FragmentSignalListBinding;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.SignalListViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignalListFragment extends Fragment {
    private SignalListViewModel viewModel;
    private DeleteSignalDialogFragment deleteDialogFragment;

    public static SignalListFragment newInstance() {
        return new SignalListFragment();
    }
    FragmentSignalListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSignalListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SignalListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        RecyclerView recyclerView = binding.recyclerSchedule;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SignalAdapter signalAdapter = new SignalAdapter(viewModel);
        recyclerView.setAdapter(signalAdapter);
        viewModel.getAllSignals().observe(getViewLifecycleOwner(), signalAdapter::submitList);
        viewModel.getLongClickedSignal().observe(getViewLifecycleOwner(), signal -> {
            if(signal != null) startDeleteSignalDialog(signal);
        });
        binding.signalToolbar.setNavigationOnClickListener(v -> requireActivity().finish());
    }

    @Override
    public void onPause() {
        if (deleteDialogFragment != null && deleteDialogFragment.isAdded()) {
            deleteDialogFragment.dismiss();
        }
        super.onPause();
    }

    private void startDeleteSignalDialog(Signal signal) {
        if(deleteDialogFragment != null && deleteDialogFragment.isAdded()) deleteDialogFragment.dismiss();
        deleteDialogFragment = new DeleteSignalDialogFragment(viewModel, signal);
        deleteDialogFragment.show(getParentFragmentManager(), "DeleteSignalDialogFragment");
    }
}