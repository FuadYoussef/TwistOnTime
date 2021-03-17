package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;

import com.atakmap.android.plugintemplate.plugin.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * The ActiveTimer class is used to represent a timer that is running or will be run soon
 * The Active timer stores a timer that it corresponds to, an int remainingDurationMillis that
 * represents how many millis are remaining in the timer and a state. The state can have four values:
 * paused (when the timer is paused, newly created, or reset), running (when the timer is running)
 * finished (when the timer is finished), dismissed (when the timer is dismissed)
 */
public class ActiveTimer implements Serializable {
    public enum ActiveTimerState { RUNNING, PAUSED, FINISHED, DISMISSED}

    private Timer timer;
    private long remainingDurationMillis;
    private CountDownTimer countDown;
    private ActiveTimerState state;
    public TimerListAdapter containingAdapter;
    public Context context;

    /**
     * The constructor for an active timer takes in a timer, the adapter that will display the active
     * timer, and a context. An active timer starts in the paused state which can be changed with
     * various functions in the class
     * @param timer timer object to based ActiveTimer off of
     * @param containingAdapter the adapter that will display the timer. This is used so that the
     * activeTimer can notify the containingAdapter to update as the
     * activeTimer ticks
     * @param context the relevant context. This is used by the active timer when creating a sound
     */
    public ActiveTimer(Timer timer, TimerListAdapter containingAdapter, Context context) {
        this.timer = timer;
        this.remainingDurationMillis = timer.getDurationMillis();
        this.state = ActiveTimerState.PAUSED;
        this.containingAdapter = containingAdapter;
        this.context = context;
    }

    /**
     * Getter method for the state
     * @return state of the timer
     */
    public ActiveTimerState getState() {
        return state;
    }

    /**
     * This method starts an active timer. It creates a CountDownTimer that will count down
     * currentDurationMillis seconds, calling the ActiveTimer tick() method with each tick of the
     * CountDownTimer. When the CountDownTimer is finished. It changes the ActiveTimer state to
     * finished.
     */
    public void start() {
        state = ActiveTimerState.RUNNING;
        countDown = new CountDownTimer(remainingDurationMillis, 1000) {
            @Override
            public void onTick(long l) {
                remainingDurationMillis = l;
                containingAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFinish() {
                // set state to finished
                ActiveTimer.this.state = ActiveTimerState.FINISHED;
                containingAdapter.notifyDataSetChanged();
                ActiveTimer.this.makeSound();
            }
        };
        countDown.start();
    }

    /**
     * This method pauses an active timer. It does this by cancelling the running CountDownTimer
     * if there is one.
     */
    public void pause() {
        if (countDown != null) {
            countDown.cancel();
        }
        state = ActiveTimerState.PAUSED;
    }

    /**
     * This methods returns a nicely formatted string showing the remaining duration of the timer.
     * @return a nicely formatted string showing the remaining duration of the timer. The string
     * format is:
     * h:mm:ss if the currentDurationMillis is more than an hour
     * m:ss if the currentDurationMillis is less than 1 hour and at least 1 minute
     * s if the currentDurationMillis is less than 60 seconds
     */
    public String getDurationRemainingString() {
        int hours = (int) remainingDurationMillis /(60*60*1000);
        int minutes = (int)(remainingDurationMillis -(hours*60*60*1000))/(60*1000);
        int seconds = (int)(remainingDurationMillis -(hours*60*60*1000)-(minutes*60*1000))/1000;
        if (hours != 0) {
            String minutes_string = new DecimalFormat("00").format(minutes);
            String seconds_string = new DecimalFormat("00").format(seconds);
            return (hours +":"+ minutes_string +":"+ seconds_string);
        }
        if (minutes != 0) {
            String seconds_string = new DecimalFormat("00").format(seconds);
            return (minutes +":"+ seconds_string);
        } else {
            return ("" + seconds);
        }
    }

    /**
     * This method resets a timer. It does this by dismissing the current CountDownTimer if there
     * is one and setting the currentDurationMillis to the timer's duration millis. It also changes
     * the state of the timer to paused.
     */
    public void reset() {
        remainingDurationMillis = timer.getDurationMillis();
        if (countDown != null) {
            countDown.cancel();
        }
        state = ActiveTimerState.PAUSED;
    }

    /**
     * This method dismisses a timer. It cancels the current countdown timer if there is one
     * and sets the ActiveTimer's state to dismissed
     */
    public void dismiss() {
        if (countDown != null) {
            countDown.cancel();
        }
        state = ActiveTimerState.DISMISSED;
    }

    /**
     * This method makes the sound the active timer should make
     */
    public void makeSound() {
        final MediaPlayer mp;
        switch (timer.getSound().toLowerCase()) {
            case "chime":
                mp = MediaPlayer.create(context, R.raw.chime);
                break;
            case "alarm":
                mp = MediaPlayer.create(context, R.raw.alarm);
                break;
            case "radar":
                mp = MediaPlayer.create(context, R.raw.radar);
                break;
            case "signal":
                mp = MediaPlayer.create(context, R.raw.signal);
                break;
            default:
                mp = MediaPlayer.create(context, R.raw.chime);
        }
        mp.start();
    }

    /**
     * This method makes the notification sound the active timer should make
     * I am not sure if this is necessary. If possible it would be better to just call makeSound
     * but I'm not sure if that will work when the plugin is not open
     */
    public void makeNotificationSound() {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    /**
     * This method checks if the active timer should make a notification
     * If the timer is running, this method checks each of the timer's notification to see if it
     * matches with the currentDurationMillis
     * @return boolean indicating if a notification corresponding to this active timer should be displayed
     */
    public boolean shouldMakeNotification() {
        if (state == ActiveTimerState.RUNNING) {
            ArrayList<String> notifications = timer.getNotifications();
            for (String notification : notifications) {
                if (notificationToMillis(notification) == remainingDurationMillis) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method converts a string notification to the corresponding millis value
     * (At time of event = 0 millis, 1 second before = 1000 millis, 1 minute before = 60000 millis, etc)
     * @param notification string representing the notification to convert to millis
     * @return an int representing the millis value corresponding to the notification
     */
    private int notificationToMillis(String notification) {
        int millis = 0;
        if (notification.toLowerCase().contains("second")) {
            String val = notification.split(" ")[0];
            millis = Integer.parseInt(val) * 1000;
        }
        else if (notification.toLowerCase().contains("minute")) {
            String val = notification.split(" ")[0];
            millis = Integer.parseInt(val) * 1000 * 60;
        }
        else if (notification.toLowerCase().contains("hour")) {
            String val = notification.split(" ")[0];
            millis = Integer.parseInt(val) * 1000 * 60 * 60;
        }
        return millis;
    }

    /**
     * This method returns the name of the timer
     */
    public String getName() {
        return timer.getName();
    }

    /**
     * This method returns the timer linked to the active timer
     */
    public Timer getTimer() {
        return timer;
    }

    public long getRemainingDurationMillis() { return remainingDurationMillis; }
}

