package com.mmp.ayatsurikun.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.mmp.ayatsurikun.App;
import com.mmp.ayatsurikun.R;
import com.mmp.ayatsurikun.databinding.ScheduleDialogBinding;
import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.viewmodel.ScheduleListViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.qualifiers.ApplicationContext;

@AndroidEntryPoint
public class ScheduleDialogFragment extends DialogFragment {
    private final Context context;
    private ScheduleListViewModel viewModel;
    private ScheduleDialogBinding binding;
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Inject
    public ScheduleDialogFragment(@ApplicationContext Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        binding = ScheduleDialogBinding.inflate(requireActivity().getLayoutInflater());
        viewModel = new ViewModelProvider(requireParentFragment()).get(ScheduleListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        builder.setView(binding.getRoot());
        binding.textDate.setText(dateFormat.format(calendar.getTime()));
        binding.textTime.setText(timeFormat.format(calendar.getTime()));
        calendar.set(Calendar.SECOND, 0);
        binding.dateButton.setOnClickListener(v -> {
            DialogFragment dialog = new DialogFragment(){
                @NonNull
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    return new DatePickerDialog(
                            requireActivity(),
                            (view, year, month, dayOfMonth) -> {
                                calendar.set(year, month, dayOfMonth);
                                binding.textDate.setText(dateFormat.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH));
                }
            };
            dialog.show(requireActivity().getSupportFragmentManager(), "DatePicker");
        });

        binding.timeButton.setOnClickListener(v -> {
            DialogFragment dialog = new DialogFragment(){
                @NonNull
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    return new TimePickerDialog(
                            requireActivity(),
                            (view, hourOfDay, minute) -> {
                                calendar.set(hourOfDay, minute);
                                binding.textTime.setText(timeFormat.format(calendar.getTime()));
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true);
                }
            };
            dialog.show(requireActivity().getSupportFragmentManager(), "TimePicker");
        });

        builder.setPositiveButton(R.string.dialog_btn_ok, (dialog, which) -> {
            Calendar now = Calendar.getInstance();
            if (calendar.after(now)) {
                viewModel.addSchedule(
                        ((App)context).getDevice().getId(),
                        (Signal) binding.spinner.getSelectedItem(),
                        calendar.getTimeInMillis()
                );
            } else {
                Toast.makeText(context, R.string.invalid_input, Toast.LENGTH_SHORT).show();
            }
            viewModel.clearLongClickedSchedule();
        });
        builder.setNegativeButton(R.string.dialog_btn_ng, (dialog, which) -> viewModel.clearLongClickedSchedule());
        return builder.create();
    }
}
