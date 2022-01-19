package com.euzhene.pineapplo;

import android.content.SharedPreferences;
import android.util.Log;


public  class TimeSettings {
    private static SharedPreferences sharedPreferences;

    public static int minuteWorkDefault = 25;
    public static int secondWorkDefault = 0;
    public static int minuteBreakDefault= 3;
    public static int secondBreakDefault = 0;

    public static final int countDownInterval = 1000;

    public static String[] timeWorkDurationArray;
    public static String[] timeBreakDurationArray;


    public static void UpdateTimeSettings() {
        if (sharedPreferences.contains("workTime") || sharedPreferences.contains("breakTime")) {
            minuteWorkDefault = sharedPreferences.getInt("workTime", 0);
            minuteBreakDefault = sharedPreferences.getInt("breakTime", 0);

            UpdateArrays();
        }
    }
    static {
        UpdateArrays();
    }
    public static SharedPreferences getSharedPreferences() {return sharedPreferences;}
    public static void setSharedPreferences(SharedPreferences sharedPreferences) {
        TimeSettings.sharedPreferences = TimeSettings.sharedPreferences == null ? sharedPreferences : TimeSettings.sharedPreferences;
        UpdateTimeSettings();
    }
    private static void UpdateArrays() {
        timeWorkDurationArray = new String[] {minuteWorkDefault + " minutes","0 minutes", "1 minute", "5 minutes","10 minutes","20 minutes", "25 minutes","30 minutes", "40 minutes"};
        timeBreakDurationArray = new String[] {minuteBreakDefault + " minutes", "3 minutes", "4 minutes","5 minutes", "7 minutes", "10 minutes"};
    }
}
