package com.mmp.ayatsurikun.repository;

import androidx.lifecycle.LiveData;

import com.mmp.ayatsurikun.model.Signal;

import java.util.List;

public interface SignalRepository {
    void create(Signal signal);

    void delete(Signal signal);

    LiveData<List<Signal>> findAll();
}
