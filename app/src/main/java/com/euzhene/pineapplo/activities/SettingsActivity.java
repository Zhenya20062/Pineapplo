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
        //Log.i("durationarray", TimeSettings.timeWorkDurationArray.toString());
        CustomAdapter workAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, TimeSettings.timeWorkDurationArray);
        CustomAdapter breakAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, TimeSettings.timeBreakDurationArray);

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
