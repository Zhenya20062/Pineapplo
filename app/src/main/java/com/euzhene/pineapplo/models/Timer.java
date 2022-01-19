package com.euzhene.pineapplo.models;

import com.euzhene.pineapplo.TimeSettings;

public class Timer {
    private boolean isBreak;
    private boolean isGoing;

    private int minutes = TimeSettings.minuteWorkDefault;
    private int seconds = TimeSettings.secondWorkDefault;

    private int totalTimeMillis = TimeSettings.minuteWorkDefault * 60 * 1000 + TimeSettings.secondWorkDefault * 1000;

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getTotalTimeMillis() {
        return totalTimeMillis;
    }

    public boolean isBreak() {
        return isBreak;
    }


    public void tick(long left) {
        totalTimeMillis = (int)left;
        minutes =  (totalTimeMillis / TimeSettings.countDownInterval / 60);
        seconds = ((totalTimeMillis / TimeSettings.countDownInterval)- minutes * 60);
    }

    public void updateCurrentTimeFromRepository(boolean isBreak) {
        this.isBreak = isBreak;
        if (this.isBreak) {
            totalTimeMillis = TimeSettings.minuteBreakDefault * 60 * 1000 + TimeSettings.secondBreakDefault * 1000;

            minutes = TimeSettings.minuteBreakDefault;
            seconds = TimeSettings.secondBreakDefault;
        } else {
            totalTimeMillis = TimeSettings.minuteWorkDefault * 60 * 1000 + TimeSettings.secondWorkDefault * 1000;

            minutes = TimeSettings.minuteWorkDefault;
            seconds = TimeSettings.secondWorkDefault;
        }
        isGoing = false;
    }

    public void setGoing(boolean going) {
        isGoing = going;
    }

    public boolean isGoing() {
        return isGoing;
    }
}
