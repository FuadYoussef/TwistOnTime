package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;

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
    private int remainingDurationMillis;
    private CountDownTimer countDown;
    private ActiveTimerState state;

    /**
     * The constructor for an active timer, takes in a timer. An active timer starts in the
     * paused state which can be changed with various functions in the class
     * @param timer timer object to based ActiveTimer off of
     */
    public ActiveTimer(Timer timer) {
        this.timer = timer;
        this.remainingDurationMillis = timer.getDurationMillis();
        this.state = ActiveTimerState.PAUSED;
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
                ActiveTimer.this.tick();
            }

            @Override
            public void onFinish() {
                // set state to finished
                ActiveTimer.this.state = ActiveTimerState.FINISHED;
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
     * @param context the context is needed to make a sound
     */
    public void makeSound(Context context) {
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
     * This method updates the currentDurationMillis attribute of the ActiveTimer
     */
    private void tick() {
        if (state == ActiveTimerState.RUNNING) {
            remainingDurationMillis -= 1000;
        }

    }
}
