package com.atakmap.android.plugintemplate;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private String CHANNEL_ID = "TIMER";
    private final MapView mapView;
    private final int notifyID;
    public boolean updateNotification = false;
    private String notificationStr = getDurationRemainingString();
    /**
     * The constructor for an active timer takes in a timer, the adapter that will display the active
     * timer, and a context. An active timer starts in the paused state which can be changed with
     * various functions in the class
     * @param timer timer object to based ActiveTimer off of
     * @param notificationID the ID the notify should use
     * @param containingAdapter the adapter that will display the timer. This is used so that the
     * activeTimer can notify the containingAdapter to update as the
     * activeTimer ticks
     * @param context the relevant context. This is used by the active timer when creating a sound
     * @param mapView used to get the actual notification context
     */
    public ActiveTimer(Timer timer, int notificationID, TimerListAdapter containingAdapter, Context context, MapView mapView) {
        this.timer = timer;
        this.remainingDurationMillis = timer.getDurationMillis();
        this.state = ActiveTimerState.PAUSED;
        this.containingAdapter = containingAdapter;
        this.context = context;
        this.mapView = mapView;
        this.notifyID = notificationID;
        createNotificationChannel();
        this.CHANNEL_ID = "TIMER";

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
        final Set<Integer> notificationSet = new HashSet<>();
        for(String notification: timer.getNotifications()) {
            int cur = notificationToMillis(notification)/1000;
            notificationSet.add(cur);
        }
        countDown = new CountDownTimer(remainingDurationMillis, 1000) {
            @Override
            public void onTick(long l) {
                remainingDurationMillis = l;
                 int cur = (int)l/1000;
                notificationStr = getDurationRemainingString();
                containingAdapter.notifyDataSetChanged();
                if (notificationSet.contains(cur) && cur != 0) {
                    makeSound();
                    updateNotification = true;
                    createNotification();
                }
                if(updateNotification) createNotification();

            }

            @Override
            public void onFinish() {
                ActiveTimer.this.state = ActiveTimerState.FINISHED;
                containingAdapter.notifyDataSetChanged();
                ActiveTimer.this.makeSound();
                createNotification();

            }
        };
        countDown.start();
    }
    /**
     * Creates a notification channel for each timer
     */
    private void createNotificationChannel() {
        Context actualContext = mapView.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && NotificationManagerCompat.from(actualContext) != null) {
            CharSequence name = "Timer Notification";
            String description = "Notification for a timer";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
            NotificationManager notificationManager = actualContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            channel.setSound(null, null);
        }
    }
    /**
     * Creates (and updates) a notification
     * Timers are low priority to avoid other sounds from being played
     * Timers cannot be dismissed to avoid having a receiver class which can complicate
     * things
     */
    private void createNotification() {
        Context actualContext = mapView.getContext();

        NotificationCompat.Builder builder;
        if(state == ActiveTimerState.PAUSED) {
            notificationStr = getDurationRemainingString();
            builder = new NotificationCompat.Builder(actualContext, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.alert_light_frame)
                    .setContentTitle(timer.getName() + " Paused")
                    .setContentText(notificationStr)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setColor(Color.blue(1))
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setSound(null);
        } else if (state == ActiveTimerState.RUNNING){
            notificationStr = getDurationRemainingString();
            builder = new NotificationCompat.Builder(actualContext, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.alert_light_frame)
                    .setContentTitle(timer.getName())
                    .setContentText(notificationStr)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setColor(Color.blue(1))
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setSound(null);
        } else {
            notificationStr = "Finished";
            builder = new NotificationCompat.Builder(actualContext, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.alert_light_frame)
                    .setContentTitle(timer.getName())
                    .setContentText(notificationStr)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setColor(Color.blue(1))
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setSound(null);
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(actualContext);
        notificationManager.notify(notifyID, builder.build());
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
        if(updateNotification) createNotification();

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
        String hours_string = new DecimalFormat("00").format(hours);
        String minutes_string = new DecimalFormat("00").format(minutes);
        String seconds_string = new DecimalFormat("00").format(seconds);
        return (hours_string +":"+ minutes_string +":"+ seconds_string);
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
        Context actualContext = mapView.getContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(actualContext);
        notificationManager.cancel(notifyID);
        containingAdapter.notifyDataSetChanged();

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
        Context actualContext = mapView.getContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(actualContext);
        notificationManager.cancel(notifyID);

        PluginTemplateDropDownReceiver.timers.remove(this);
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

