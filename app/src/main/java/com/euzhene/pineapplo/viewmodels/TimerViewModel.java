package com.euzhene.pineapplo.viewmodels;

import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.euzhene.pineapplo.TimeSettings;

public class TimerViewModel extends ViewModel {

    private CountDownTimer downTimer;

    com.euzhene.pineapplo.models.Timer timer = new com.euzhene.pineapplo.models.Timer();

    public boolean IsBreak() { return timer.isBreak();}

    public MutableLiveData<String> Minutes = new MutableLiveData<>(timer.getMinutes() < 10 ? "0" + timer.getMinutes():String.valueOf(timer.getMinutes()));
    public MutableLiveData<String> Seconds = new MutableLiveData<>(timer.getSeconds() < 10 ? "0" + timer.getSeconds():String.valueOf(timer.getSeconds()));

    public int GetTotalTimeInMillis() { return timer.getTotalTimeMillis();}

    public void Start() {

        timer.setGoing(true);
        downTimer = new CountDownTimer(GetTotalTimeInMillis(), TimeSettings.countDownInterval) {
            @Override
            public void onTick(long l) {
                timer.tick(l);

                UpdateStringTime();
            }

            @Override
            public void onFinish() {
                UpdateTimeSettings(!timer.isBreak());
            }
        }.start();
    }
    public void Stop() {
        downTimer.cancel();
        UpdateTimeSettings(false);
    }

    private void UpdateStringTime() {
        Minutes.setValue(timer.getMinutes() < 10 ? "0" + timer.getMinutes():String.valueOf(timer.getMinutes()));
        Seconds.setValue(timer.getSeconds() < 10 ? "0" + timer.getSeconds():String.valueOf(timer.getSeconds()));
    }
    public void UpdateTimeSettings(boolean isBreak) {
        timer.updateCurrentTimeFromRepository(isBreak);
        UpdateStringTime();
    }

    public boolean IsGoing() {
        return timer.isGoing();
    }
}
