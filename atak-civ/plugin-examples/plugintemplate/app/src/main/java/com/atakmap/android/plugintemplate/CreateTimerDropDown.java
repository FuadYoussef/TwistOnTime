package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.dropdown.DropDown.OnStateListener;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateTimerDropDown extends DropDownReceiver implements OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_CREATE = "com.atakmap.android.plugintemplate.CreateTimerDropDown";
    private final View templateView;
    private final Context pluginContext;
    private Timer timer;
    private EditText name;
    private EditText durationHours;
    private EditText durationMinutes;
    private EditText durationSeconds;
    private CheckBox preset;
    private Button changeSoundButton;
    public CreateTimerDropDown(final MapView mapView,
                                final Context context) {
        super(mapView);
        this.pluginContext = context;
        timer = new Timer();

        templateView = PluginLayoutInflater.inflate(context, R.layout.create_timer_layout, null);

        this.name = templateView.findViewById(R.id.timerName);
        this.durationHours  = templateView.findViewById(R.id.durationHours);
        this.durationMinutes  = templateView.findViewById(R.id.durationMinutes);
        this.durationSeconds  = templateView.findViewById(R.id.durationSeconds);
        this.preset = templateView.findViewById(R.id.checkBox);
        this.changeSoundButton = (Button)templateView.findViewById(R.id.changeSoundButton);

        changeSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("PAGE_TO_RETURN_TO", "CreateTimerDropDown");
                i.putExtra("DEFAULT_SELECTED_SOUND", timer.getSound());
                i.setAction(ChangeSoundsDropDown.SHOW_CHANGE_SOUNDS);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });
        Button notificationButton = (Button)templateView.findViewById(R.id.addNotificationButton);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("PAGE_TO_RETURN_TO", "CreateTimerDropDown");
                i.putExtra("DEFAULT_SELECTED_NOTIFICATIONS", timer.getNotification());
                i.setAction(CustomizeNotificationsDropDown.SHOW_CHANGE_NOTIFICATIONS);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });
        Button submit = (Button)templateView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                String durationMinStr = durationMinutes.getText().toString();
                String durationHrStr = durationHours.getText().toString();
                String durationSecStr = durationSeconds.getText().toString();
                if (!durationMinStr.matches("") && !nameStr.matches("") &&
                        !durationHrStr.matches("") && !durationSecStr.matches("")) {
                    timer.setName(nameStr);
                    timer.setDuration(durationHours.getText().toString() + ":" +
                            durationMinutes.getText().toString() + ":" +
                            durationSeconds.getText().toString());
                    timer.setPreset(preset.isChecked());
                    timer.setMinutes(Integer.parseInt(durationMinStr));
                    timer.setHours(Integer.parseInt(durationHrStr));
                    timer.setSeconds(Integer.parseInt(durationSecStr));
                    Intent i = new Intent();
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                    i.putExtra("TIMER", timer);
                    AtakBroadcast.getInstance().sendBroadcast(i);
                }
            }
        });
        Button back = (Button)templateView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });
    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(Context context, final Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_CREATE)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, true);

            if (intent.getStringExtra("SELECTED_SOUND") != null) {
                this.changeSoundButton.setText(intent.getStringExtra("SELECTED_SOUND"));
                timer.setSound(intent.getStringExtra("SELECTED_SOUND"));
            } else if(intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS") !=null) {
                ArrayList<String> act = (intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS"));
                timer.setNotification(act);
            } else {
                this.changeSoundButton.setText("Change");
                this.name.setText("");
                this.durationHours.setText("");
                this.durationMinutes.setText("");
                this.durationSeconds.setText("");
                this.preset.setSelected(false);
                String defaultSound = pluginContext.getResources().getStringArray(R.array.custom_sounds)[0];
                this.timer.setSound(defaultSound);
                this.changeSoundButton.setText(defaultSound);
                String defaultNotification = pluginContext.getResources().getStringArray(R.array.custom_notification_settings)[0];
                this.timer.setNotification(new ArrayList(Arrays.asList(defaultNotification)));
            }
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