package com.mmp.ayatsurikun.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mmp.ayatsurikun.model.Signal;
import com.mmp.ayatsurikun.usecase.ConnectionUseCase;
import com.mmp.ayatsurikun.usecase.SignalUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignalListViewModel extends ViewModel {
    private final ConnectionUseCase connectionUseCase;
    private final SignalUseCase signalUseCase;
    private final MutableLiveData<Signal> longClickedSignal = new MutableLiveData<>();

    @Inject
    public SignalListViewModel(
            ConnectionUseCase connectionUseCase,
            SignalUseCase signalUseCase) {
        this.connectionUseCase = connectionUseCase;
        this.signalUseCase = signalUseCase;
    }

    public LiveData<Signal> getLongClickedSignal() {
        return longClickedSignal;
    }

    public void clearLongClickedSignal() {
        longClickedSignal.setValue(null);
    }

    public LiveData<List<Signal>> getAllSignals() {
        return signalUseCase.getAllSignals();
    }

    public void onItemClick(Signal signal) {
        connectionUseCase.send(signal.getSignal());
    }

    public boolean onItemLongClick(Signal signal) {
        longClickedSignal.setValue(signal);
        return true;
    }

    public void deleteSignal(Signal signal) {
        signalUseCase.deleteSignal(signal);
        longClickedSignal.setValue(null);
    }
}