package com.mmp.ayatsurikun.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.FragmentSignalListBinding;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.SignalListViewModel;

import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignalListFragment extends Fragment {
    private SignalListViewModel viewModel;
    private DeleteSignalDialogFragment deleteDialogFragment;
    private FragmentSignalListBinding binding;

    @Inject
    public SignalListFragment() {
    }

    public static SignalListFragment newInstance() {
        return new SignalListFragment();
    }

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
        setupMenuBar();
        RecyclerView recyclerView = binding.recyclerSchedule;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SignalAdapter signalAdapter = new SignalAdapter(viewModel);
        recyclerView.setAdapter(signalAdapter);
        viewModel.getAllSignals().observe(getViewLifecycleOwner(), signalAdapter::submitList);
        viewModel.getLongClickedSignal().observe(getViewLifecycleOwner(), signal -> {
            if(signal != null) startDeleteSignalDialog(signal);
        });
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

    private void setupMenuBar() {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                .setTitle(R.string.signal_title);
        ((MenuHost)requireActivity()).addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == android.R.id.home) requireActivity().finish();
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }
}