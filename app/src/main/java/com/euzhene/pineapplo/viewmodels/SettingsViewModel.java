package com.euzhene.pineapplo.viewmodels;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.euzhene.pineapplo.PineapploApp;
import com.euzhene.pineapplo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SettingsViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> isLoadingMusic = new MutableLiveData<>(false);

    public MutableLiveData<Integer> fileProgress = new MutableLiveData<>(0);

    public MutableLiveData<String> error = new MutableLiveData<>(null);

    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }


    public void uploadMusic(Uri uri) {
        new Thread(() -> {
            try {
                isLoadingMusic.postValue(true);
                InputStream is = getApplication().getContentResolver().openInputStream(uri);
                if (is == null) {
                    error.postValue(getApplication().getString(R.string.unknown_error));
                    return;
                }
                int fileSize = is.available();
                File file = new File(getApplication().getCacheDir() + "/music");
                if (file.exists()) {
                    Log.d(PineapploApp.TAG, "file already exists. its length is " + file.length());
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
                Log.d(PineapploApp.TAG, "uploadMusic: " + file.getPath());
                int size = is.available();
                fileProgress.postValue(size / fileSize * 100);
                while (size > 0) {
                    byte[] buffer = new byte[size];
                    size = is.read(buffer);
                    fileOutputStream.write(buffer);
                    fileProgress.postValue(size * 100 / fileSize);
                }
                is.close();
                fileOutputStream.close();
            } catch (Exception e) {
                Log.e(PineapploApp.TAG, e.toString());
                error.postValue(e.getLocalizedMessage());
            } finally {
                isLoadingMusic.postValue(false);
            }
        }).start();
    }

    public static class SettingsViewModelFactory implements ViewModelProvider.Factory {
        Application application;

        public SettingsViewModelFactory(@NonNull Application application) {
            this.application = application;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new SettingsViewModel(application);
        }
    }
}
