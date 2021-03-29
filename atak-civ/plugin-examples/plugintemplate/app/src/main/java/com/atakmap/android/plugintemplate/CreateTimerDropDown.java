package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.PluginTemplateLifecycle;
import com.atakmap.android.plugintemplate.plugin.R;
import com.google.gson.Gson;


import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

// TODO: Add detailed description of class

/**
 * This class is used for the setup and creation of the Create Timer screen.
 */

public class CreateTimerDropDown extends DropDownReceiver implements OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_CREATE = "com.atakmap.android.plugintemplate.CreateTimerDropDown";
    private final View templateView;
    private Context pluginContext;
    private Timer timer;                    // Timer object used
    private EditText name;                  // Name of timer
    private EditText durationHours;         // Duration of timer (hour)
    private EditText durationMinutes;       // Duration of timer (minute)
    private EditText durationSeconds;       // Duration of timer (second)
    private CheckBox preset;                // Whether or not timer is marked as preset
    private Button changeSoundButton;       // Button on UI to change the sound
    private TextView changeSoundText;       // Text on UI to represent selected sound
    private boolean returnPreset;           // Help decide which screen to return to
    public CreateTimerDropDown(final MapView mapView,
                               final Context context) {
        super(mapView);
        this.pluginContext = context;
        timer = new Timer();

        templateView = PluginLayoutInflater.inflate(context, R.layout.create_timer_layout, null);

        this.name = (EditText) templateView.findViewById(R.id.timerName);
        this.durationHours  = (EditText) templateView.findViewById(R.id.durationHours);
        this.durationMinutes  =(EditText)  templateView.findViewById(R.id.durationMinutes);
        this.durationSeconds  = (EditText) templateView.findViewById(R.id.durationSeconds);
        this.preset =(CheckBox) templateView.findViewById(R.id.checkBox);
        this.changeSoundButton = (Button)templateView.findViewById(R.id.changeSoundButton);
        this.changeSoundText = (TextView)templateView.findViewById(R.id.changeSoundText);

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
                i.putExtra("DEFAULT_SELECTED_NOTIFICATIONS", timer.getNotifications());
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
                    timer.setPreset(preset.isChecked());
                    timer.setMinutes(Integer.parseInt(durationMinStr));
                    timer.setHours(Integer.parseInt(durationHrStr));
                    timer.setSeconds(Integer.parseInt(durationSecStr));
                    TextView title =(TextView) templateView.findViewById(R.id.title);
                    //Only re-add timer if you are not editing it
                    if (preset.isChecked() && !(returnPreset && title.getText().toString().contains("Edit"))) {
                        PluginTemplateDropDownReceiver.presets.add(timer);
                    }
                    writePresetsToJSON(PluginTemplateDropDownReceiver.presets);
                    if(returnPreset || preset.isChecked()) {
                        Intent i = new Intent();
                        i.setAction(PresetComponent.SHOW_PRESETS_PAGE);
                        i.putExtra("TIMER", timer);
                        AtakBroadcast.getInstance().sendBroadcast(i);

                    } else {
                        Intent i = new Intent();
                        i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                        i.putExtra("TIMER", timer);
                        AtakBroadcast.getInstance().sendBroadcast(i);
                    }
                }
            }
        });
        Button back = (Button)templateView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (returnPreset) {
                    Intent i = new Intent();
                    i.setAction(PresetComponent.SHOW_PRESETS_PAGE);
                    AtakBroadcast.getInstance().sendBroadcast(i);
                } else {
                    Intent i = new Intent();
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                    AtakBroadcast.getInstance().sendBroadcast(i);
                }
            }
        });
    }

    /**
     * This method reads the string values from the Timer object and populates the fields on the
     * Create Timer screen
     * @param timer The timer object that is to be read
     */
    private void setFields(Timer timer) {
        this.name.setText(timer.getName());
        this.preset.setChecked(timer.isPreset());
        this.changeSoundText.setText(timer.getSound());
        this.durationSeconds.setText(String.valueOf(timer.getSeconds()));
        this.durationMinutes.setText(String.valueOf(timer.getMinutes()));
        this.durationHours.setText(String.valueOf(timer.getHours()));
    }
    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    /**
     * Determines what screen the intent is coming from to set the data appropriately for editing
     * an existing timer or for creating a new timer.
     * @param context context of the previous screen
     * @param intent intent of the previous screen
     */
    @Override
    public void onReceive(Context context, final Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_CREATE)) {
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, true);
            if (intent.getStringExtra("SELECTED_SOUND") != null) {
                this.changeSoundText.setText(intent.getStringExtra("SELECTED_SOUND"));
                timer.setSound(intent.getStringExtra("SELECTED_SOUND"));
            } else if(intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS") !=null) {
                ArrayList<String> act = (intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS"));
                timer.setNotifications(act);
            } else if (intent.getSerializableExtra("TIMER") != null) {
                Timer timer = (Timer) intent.getSerializableExtra("TIMER");
                this.timer = timer;
                setFields(timer);
                TextView title =(TextView) templateView.findViewById(R.id.title);
                title.setText("Edit Timer");

            }else {
                TextView title =(TextView) templateView.findViewById(R.id.title);
                title.setText("Create Timer");
                this.timer = new Timer();
                this.name.setText("");
                this.durationHours.setText("");
                this.durationMinutes.setText("");
                this.durationSeconds.setText("");
                this.preset.setChecked(false);
                String defaultSound = pluginContext.getResources().getStringArray(R.array.custom_sounds)[0];
                this.timer.setSound(defaultSound);
                this.changeSoundText.setText(defaultSound);
                String defaultNotification = pluginContext.getResources().getStringArray(R.array.custom_notification_settings)[0];
                this.timer.setNotifications(new ArrayList(Arrays.asList(defaultNotification)));
            }
            if(intent.getStringExtra("PRESET") != null) {
                this.returnPreset = true;
                this.preset.setChecked(true);
            } else {
                this.returnPreset = false;
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

    /**
     * takes an arraylist of timers containing all existing preset timers and writes them to a text
     * file. This text file is called presets.txt and contains a JSON representation of the timers
     * @param timers the arraylist of preset timers to be saved to storage
     */
    private void writePresetsToJSON(ArrayList<Timer> timers) {

        String fileName2 = "presets.txt";
        Gson gson = new Gson();
        String s2 = gson.toJson(timers);
        try (FileOutputStream fos = PluginTemplateLifecycle.activity.getApplicationContext().openFileOutput(fileName2, Context.MODE_PRIVATE)) {
            fos.write(s2.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}