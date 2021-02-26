package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;

import com.atakmap.coremap.log.Log;
import android.app.AlertDialog;
import android.app.AlertDialog;
import android.widget.Toast;

public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugintemplate.SHOW_PLUGIN";

    private final View templateView;
    private final Context pluginContext;
    private Timer globalTimer;


    /**************************** CONSTRUCTOR *****************************/

    public PluginTemplateDropDownReceiver(final MapView mapView,
                                          final Context context) {
        super(mapView);
        this.pluginContext = context;
        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context, R.layout.main_layout, null);

        // setting the home screen's add button so that when clicked it causes the ChangeSoundsDropDown
        // to open this is done with the intent shown below and other changes to the PluginTemplateMapComponent
        // and ChangeSoundsDropDown files

        templateView.findViewById(R.id.add_button)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRetain(true);
                        /* example intent for how to call change sounds
                        Intent i = new Intent();
                        i.setAction(ChangeSoundsDropDown.SHOW_CHANGE_SOUNDS);
                        i.putExtra("DEFAULT_SELECTED_SOUND", "Radar");
                        i.putExtra("PAGE_TO_RETURN_TO", "PluginTemplateDropDownReceiver");
                         */
                        // how to call customizeNotificaitonDropDown
                        Intent i = new Intent();

                        i.setAction(CreateTimerDropDown.SHOW_CREATE);
//                        i.setAction(CustomizeNotificationsDropDown.SHOW_CHANGE_NOTIFICATIONS);
//                        ArrayList<String> notifications_to_select = new ArrayList<>();
//                        notifications_to_select.add("2 Hours Before");
//                        notifications_to_select.add("3 Hours Before");
//                        i.putExtra("DEFAULT_SELECTED_NOTIFICATIONS", notifications_to_select);
//                        //i.putExtra("DEFAULT_SELECTED_SOUND", "Radar");
//                        i.putExtra("PAGE_TO_RETURN_TO", "PluginTemplateDropDownReceiver");
                        AtakBroadcast.getInstance().sendBroadcast(i);
                    }
                });
        templateView.findViewById(R.id.presets_button)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRetain(true);
                        Intent i = new Intent();
                        i.setAction(PresetComponent.SHOW_PRESETS_PAGE);
                        AtakBroadcast.getInstance().sendBroadcast(i);
                    }
                });
        templateView.findViewById(R.id.start_timer_button)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final TextView firstTimerTimeTextView = (TextView)templateView.findViewById(R.id.first_timer_time);
                        new CountDownTimer(globalTimer.getDurationMillis(), 1000) {
                            public void onTick(long millisUntilFinished) {
                                int hours = (int)millisUntilFinished/(60*60*1000);
                                int minutes = (int)(millisUntilFinished-(hours*60*60*1000))/(60*1000);
                                int seconds = (int)(millisUntilFinished-(hours*60*60*1000)-(minutes*60*1000))/1000;
                                String duration = hours +":"+ minutes +":"+ seconds;
                                firstTimerTimeTextView.setText(duration);
                            }

                            public void onFinish() {
                                firstTimerTimeTextView.setText("done!");
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(context, notification);
                                r.play();
                            }
                        }.start();
                    }
                });
    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PLUGIN)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,

                    HALF_HEIGHT, true);
            // how to process return intent value from calling ChangeSoundsScreen
            if (intent.getSerializableExtra("TIMER") != null) {
                Timer timer = (Timer) intent.getSerializableExtra("TIMER");
                globalTimer = timer;

                String toastMessage = "New timer '" + timer.getName() + "' set.";
                Toast.makeText(context,toastMessage,Toast.LENGTH_SHORT).show();

//                Log.d(TAG, "Timer " + timer.getName());
                TextView firstTimerNameTextView = (TextView)templateView.findViewById(R.id.first_timer_name);
                firstTimerNameTextView.setText(timer.getName());

//                Log.d(TAG, "Timer " + timer.getDuration());
                TextView firstTimerTimeTextView = (TextView)templateView.findViewById(R.id.first_timer_time);
                firstTimerTimeTextView.setText(timer.getDuration());

//                Log.d(TAG, "Timer " + timer.getSound());

//                for(String s : timer.getNotification()) {
//                    Log.d(TAG, "Timer " + s);
//                }

            }

            /*
            // how to process return value from calling CustomizeNotificationsDropDown
            if (intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS") != null) {
                Button b = (Button)templateView.findViewById(R.id.add_button);
                int i = intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS").size();
                b.setText("" + i);
            }
             */
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }

}