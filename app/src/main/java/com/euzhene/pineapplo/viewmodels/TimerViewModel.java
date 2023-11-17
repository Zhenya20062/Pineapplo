package com.euzhene.pineapplo.viewmodels;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.euzhene.pineapplo.PineapploApp;
import com.euzhene.pineapplo.TimeSettings;

import java.io.File;

public class TimerViewModel extends AndroidViewModel {
    public TimerViewModel(@NonNull Application application) {
        super(application);
    }

    private CountDownTimer downTimer;

    com.euzhene.pineapplo.models.Timer timer = new com.euzhene.pineapplo.models.Timer();


    public boolean IsBreak() {
        return timer.isBreak();
    }

    public MutableLiveData<String> Minutes = new MutableLiveData<>(timer.getMinutes() < 10 ? "0" + timer.getMinutes() : String.valueOf(timer.getMinutes()));
    public MutableLiveData<String> Seconds = new MutableLiveData<>(timer.getSeconds() < 10 ? "0" + timer.getSeconds() : String.valueOf(timer.getSeconds()));

    public int GetTotalTimeInMillis() {
        return timer.getTotalTimeMillis();
    }

    public int GetTotalTimeInMinutes() {
        return timer.getTotalTimeMillis() / 1000 / 60;
    }

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
        getPlayer().start();
    }

    public void Stop() {
        downTimer.cancel();
        UpdateTimeSettings(false);
        getPlayer().pause();
    }

    private void UpdateStringTime() {
        Minutes.setValue(timer.getMinutes() < 10 ? "0" + timer.getMinutes() : String.valueOf(timer.getMinutes()));
        Seconds.setValue(timer.getSeconds() < 10 ? "0" + timer.getSeconds() : String.valueOf(timer.getSeconds()));
    }

    public void UpdateTimeSettings(boolean isBreak) {
        timer.updateCurrentTimeFromRepository(isBreak);
        UpdateStringTime();
    }

    public boolean IsGoing() {
        return timer.isGoing();
    }

    private PineapploApp getApp() {
        return getApplication();
    }

    private MediaPlayer getPlayer() {
        return getApp().musicPlayer;
    }


    public static class TimerViewModelFactory implements ViewModelProvider.Factory {
        Application application;

        public TimerViewModelFactory(@NonNull Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new TimerViewModel(application);
        }
    }
}
