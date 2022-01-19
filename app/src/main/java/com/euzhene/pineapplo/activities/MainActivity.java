package com.euzhene.pineapplo.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.euzhene.pineapplo.MyService;
import com.euzhene.pineapplo.R;
import com.euzhene.pineapplo.TimeSettings;
import com.euzhene.pineapplo.databinding.ActivityMainBinding;
import com.euzhene.pineapplo.viewmodels.TimerViewModel;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Intent intent;
    public static TimerViewModel timerViewModel;

    boolean isFirstResume = true;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        TimeSettings.setSharedPreferences(getSharedPreferences("MyPrefs", Context.MODE_PRIVATE));

        timerViewModel = new TimerViewModel();

        intent = new Intent(this, MyService.class);

        setButtonListeners();

        SetObserveRelationship();
    }

    @Override
    protected void onResume() {
        if (isFirstResume) {isFirstResume=false; super.onResume(); return;}

        if ((!timerViewModel.IsGoing() && (TimeSettings.minuteWorkDefault != Integer.parseInt(timerViewModel.Minutes.getValue()) && !timerViewModel.IsBreak() || TimeSettings.minuteBreakDefault != Integer.parseInt(timerViewModel.Minutes.getValue()) && timerViewModel.IsBreak()))) {
            timerViewModel.UpdateTimeSettings(timerViewModel.IsBreak());
        }

        if ((Integer.parseInt(timerViewModel.Minutes.getValue())  == TimeSettings.minuteWorkDefault && Integer.parseInt(timerViewModel.Seconds.getValue()) == TimeSettings.secondWorkDefault) ||
                (Integer.parseInt(timerViewModel.Minutes.getValue())  == TimeSettings.minuteBreakDefault && Integer.parseInt(timerViewModel.Seconds.getValue()) == TimeSettings.secondBreakDefault)) {

            ChangeBtnViews(false);
            stopService(intent);
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBtn:
                ChangeBtnViews(true);

                BeginForegroundService();
                break;
            case R.id.stopBtn:
                timerViewModel.Stop();

                ChangeBtnViews(false);

                stopService(intent);
                break;
            case R.id.settingsBtn:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(intent);
        super.onDestroy();
    }

    private void ChangeBtnViews(boolean isStartBtnClicked) {
        if (isStartBtnClicked) {
           binding.stopBtn.setVisibility(View.VISIBLE);
           binding.startBtn.setVisibility(View.GONE);
        }
        else {
           binding.stopBtn.setVisibility(View.GONE);
           binding.startBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setButtonListeners() {
        binding.startBtn.setOnClickListener(this);
        binding.stopBtn.setOnClickListener(this);
        binding.settingsBtn.setOnClickListener(this);

    }
    private void SetObserveRelationship() {
        final Observer<String> secondObserver = s -> {
            binding.txtSeconds.setText(s);
            //Log.i("minutesSeconds", timer.Minutes.getValue() + ";" + timer.Seconds.getValue());
            if (Integer.parseInt(timerViewModel.Minutes.getValue()) == 0 && Integer.parseInt(timerViewModel.Seconds.getValue()) == 0) {
                ChangeBtnViews(false);
                stopService(intent);
            }
        };
        timerViewModel.Seconds.observe(this, secondObserver);

        final Observer<String> minuteObserver = s -> binding.txtMinutes.setText(s);
        timerViewModel.Minutes.observe(this, minuteObserver);
    }
    private void BeginForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent);
        else startService(intent);
    }
}