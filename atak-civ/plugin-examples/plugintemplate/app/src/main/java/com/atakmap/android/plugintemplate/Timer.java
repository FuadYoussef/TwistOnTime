package com.atakmap.android.plugintemplate;

import java.io.Serializable;
import java.util.ArrayList;

public class Timer implements Serializable {

    private String name;                    // the name of the timer
    private String duration;                // the overall duration of the timer
    private String sound;                   // the sound the timer will make once time is up
    private ArrayList<String> notifications; // list of notifications associated with the timer
    private boolean preset;                 // flag that determines if timer is a preset or not
    private int hours;                      // the amount of hours remaining in the timer
    private int minutes;                    // the amount of minutes remaining in the timer
    private int seconds;  

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public int getDurationMillis() {
        return 1000*seconds + 1000*60*minutes + 1000*60*60*hours;
    }

    public String getName() {
        return name;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<String> notifications) {
        this.notifications = notifications;
    }

}

