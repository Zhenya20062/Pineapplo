package com.euzhene.pineapplo.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.euzhene.pineapplo.Custom.CustomAdapter;
import com.euzhene.pineapplo.R;
import com.euzhene.pineapplo.TimeSettings;


public class SettingsActivity extends Activity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ChangeBackgroundAlpha(130);

        CreateSpinners();
    }

    private void CreateSpinners() {
        CustomAdapter workAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getDurations("work"));
        CustomAdapter breakAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getDurations("break"));

        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breakAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner workSpn = findViewById(R.id.workDurationSpinner);
        workSpn.setAdapter(workAdapter);
        workAdapter.setItemToHide(0);

        Spinner breakSpn = findViewById(R.id.breakDurationSpinner);
        breakSpn.setAdapter(breakAdapter);
        breakAdapter.setItemToHide(0);

        workSpn.setOnItemSelectedListener(this);
        breakSpn.setOnItemSelectedListener(this);
    }
    private String[] getDurations(String durationID) {
        String[] durationsDefault = null;
        String[] durationsExtented = null;
        int minuteDefault = 0;
        if (durationID.equals("work")) {
            durationsDefault =getResources().getStringArray(R.array.timerWorkDurationArray);
            durationsExtented = new String[durationsDefault.length + 1];
            minuteDefault = TimeSettings.minuteWorkDefault;
        }
        else if (durationID.equals("break")) {
            durationsDefault = getResources().getStringArray(R.array.timerBreakDurationArray);
            durationsExtented = new String[durationsDefault.length + 1];
            minuteDefault = TimeSettings.minuteBreakDefault;
        }
        for (int i = 1; i < durationsExtented.length; i++) {
            if (String.valueOf(minuteDefault).equals(durationsDefault[i-1].split(" ")[0])) {
                durationsExtented[0] = durationsDefault[i-1];
            }

            durationsExtented[i] = durationsDefault[i - 1];
        }
        return durationsExtented;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
         String input = parent.getSelectedItem().toString();

         int iInput = Integer.parseInt(input.split(" ")[0]);
        SharedPreferences.Editor editor = TimeSettings.getSharedPreferences().edit();

        if (parent.getId() == R.id.workDurationSpinner) {
            editor.putInt("workTime", iInput);
        }
        else if (parent.getId() == R.id.breakDurationSpinner){
            editor.putInt("breakTime", iInput);
        }

        editor.apply();
        TimeSettings.UpdateTimeSettings();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChangeBackgroundAlpha(255);
    }
    private void ChangeBackgroundAlpha(int alpha) {
        Drawable background = AppCompatResources.getDrawable(getApplicationContext(), R.drawable.bg);
        background.setAlpha(alpha);
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
