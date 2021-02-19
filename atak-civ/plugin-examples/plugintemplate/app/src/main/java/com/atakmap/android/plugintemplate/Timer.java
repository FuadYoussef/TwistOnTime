package com.atakmap.android.plugintemplate;

import java.io.Serializable;
import java.util.ArrayList;

public class Timer implements Serializable {

    private String name;
    private String duration;
    private String sound;
    private ArrayList<String> notification;
    private boolean preset;

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

    public String getName() {
        return name;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public ArrayList<String> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<String> notification) {
        this.notification = notification;
    }
}

