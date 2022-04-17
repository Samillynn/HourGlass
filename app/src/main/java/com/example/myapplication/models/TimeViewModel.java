package com.example.myapplication.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimeViewModel extends ViewModel {
    private final MutableLiveData<Integer> totalTimeSecs = new MutableLiveData<>();
    private final MutableLiveData<Integer> leftTimeSecs = new MutableLiveData<>();

    public MutableLiveData<Integer> getLeftTimeSecs() {
        return leftTimeSecs;
    }

    public MutableLiveData<Integer> getTotalTimeSecs() {
        return totalTimeSecs;
    }
}
