package com.euzhene.pineapplo.activities;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.ViewModelProvider;

import com.euzhene.pineapplo.Custom.CustomAdapter;
import com.euzhene.pineapplo.R;
import com.euzhene.pineapplo.TimeSettings;
import com.euzhene.pineapplo.databinding.SettingsActivityBinding;
import com.euzhene.pineapplo.viewmodels.SettingsViewModel;


public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SettingsActivityBinding binding;
    private SettingsViewModel viewModel;

    private ActivityResultLauncher<String> launcher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ChangeBackgroundAlpha(130);

        CreateSpinners();

        binding.btnUploadMusic.setOnClickListener((v) -> launcher.launch("audio/*"));

        viewModel = new ViewModelProvider(this, new SettingsViewModel.SettingsViewModelFactory(getApplication())).get(SettingsViewModel.class);

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result == null) return;

            viewModel.uploadMusic(result);
        });

        setObservables();
    }


    private void CreateSpinners() {
        CustomAdapter workAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getDurations("work"));
        CustomAdapter breakAdapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, getDurations("break"));

        workAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breakAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.workDurationSpinner.setAdapter(workAdapter);
        workAdapter.setItemToHide(0);

        binding.breakDurationSpinner.setAdapter(breakAdapter);
        breakAdapter.setItemToHide(0);

        binding.workDurationSpinner.setOnItemSelectedListener(this);
        binding.breakDurationSpinner.setOnItemSelectedListener(this);
    }

    private String[] getDurations(String durationID) {
        String[] durationsDefault = null;
        String[] durationsExtented = null;
        int minuteDefault = 0;
        if (durationID.equals("work")) {
            durationsDefault = getResources().getStringArray(R.array.timerWorkDurationArray);
            durationsExtented = new String[durationsDefault.length + 1];
            minuteDefault = TimeSettings.minuteWorkDefault;
        } else if (durationID.equals("break")) {
            durationsDefault = getResources().getStringArray(R.array.timerBreakDurationArray);
            durationsExtented = new String[durationsDefault.length + 1];
            minuteDefault = TimeSettings.minuteBreakDefault;
        }
        for (int i = 1; i < durationsExtented.length; i++) {
            if (String.valueOf(minuteDefault).equals(durationsDefault[i - 1].split(" ")[0])) {
                durationsExtented[0] = durationsDefault[i - 1];
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
        } else if (parent.getId() == R.id.breakDurationSpinner) {
            editor.putInt("breakTime", iInput);
        }

        editor.apply();
        TimeSettings.UpdateTimeSettings();
    }

    private void setObservables() {
        viewModel.error.observe(this, s -> {
            if (s == null) return;

            Toast.makeText(this, s, Toast.LENGTH_LONG).show();
        });

        viewModel.isLoadingMusic.observe(this, b -> {
            binding.btnUploadMusic.setEnabled(!b);
            binding.btnUploadMusic.setBackgroundColor(getColor(b ? R.color.bg_spinner_inactive : R.color.bg_spinner));
            if (b) binding.progressIndicator.show(); else binding.progressIndicator.hide();
        });

        viewModel.fileProgress.observe(this, b->{
            binding.progressIndicator.setProgress(b);
        });
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

    private static final int PICK_MUSIC_REQUEST_CODE = 343;
}
