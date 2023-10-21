package br.com.fmu.stopwatchapp.ui.timerwatch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TimerWatchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TimerWatchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}