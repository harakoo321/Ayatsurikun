package com.mmp.ayatsurikun.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
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
import java.util.Objects;

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
        viewModel = new ViewModelProvider(this).get(ScheduleListViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        builder.setView(binding.getRoot());
        binding.textDate.setText(dateFormat.format(calendar.getTime()));
        binding.textTime.setText(timeFormat.format(calendar.getTime()));
        calendar.set(Calendar.SECOND, 0);
        binding.dateButton.setOnClickListener(v -> {
            DialogFragment dialog = new DatePickerFragment((view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                binding.textDate.setText(dateFormat.format(calendar.getTime()));
            }, calendar);
            dialog.show(getParentFragmentManager(), "DatePicker");
        });

        binding.timeButton.setOnClickListener(v -> {
            DialogFragment dialog = new TimePickerFragment((view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                binding.textTime.setText(timeFormat.format(calendar.getTime()));
            }, calendar);
            dialog.show(getParentFragmentManager(), "TimePicker");
        });

        binding.spinner.setAdapter(new ArrayAdapter<Signal>(
                context,
                android.R.layout.simple_spinner_item,
                viewModel.getAllSignalsSync()) {
            {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView)super.getView(position, convertView, parent);
                textView.setText(Objects.requireNonNull(getItem(position)).getName());
                return textView;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                TextView textView = (TextView)super.getDropDownView(position, convertView, parent);
                textView.setText(Objects.requireNonNull(getItem(position)).getName());
                return textView;
            }
        });

        builder.setPositiveButton(R.string.dialog_btn_ok, (dialog, which) -> {
            Calendar now = Calendar.getInstance();
            Signal signal = (Signal) binding.spinner.getSelectedItem();
            if (calendar.after(now) && signal != null) {
                viewModel.addSchedule(
                        ((App)context).getDevice().getId(),
                        signal,
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

    public static class DatePickerFragment extends DialogFragment {
        private final DatePickerDialog.OnDateSetListener listener;
        private final Calendar calendar;

        public DatePickerFragment(DatePickerDialog.OnDateSetListener listener, Calendar calendar) {
            this.listener = listener;
            this.calendar = calendar;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new DatePickerDialog(
                    requireActivity(),
                    listener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
    }

    public static class TimePickerFragment extends DialogFragment {
        private final TimePickerDialog.OnTimeSetListener listener;
        private final Calendar calendar;

        public TimePickerFragment(TimePickerDialog.OnTimeSetListener listener, Calendar calendar) {
            this.listener = listener;
            this.calendar = calendar;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new TimePickerDialog(
                    requireActivity(),
                    listener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true);
        }
    }
}
