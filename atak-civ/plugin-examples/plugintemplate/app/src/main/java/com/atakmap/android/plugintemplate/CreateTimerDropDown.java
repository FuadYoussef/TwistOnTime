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

/**
 * The intent used when calling ChangeSoundsDropDown must contain a extra string with the name
 * "PAGE_TO_RETURN_TO" with the name of the page that this page should return to. Otherwise this
 * will default to the homescreen. Code in this file also need to be updated to deal with returning to
 * other screens
 *
 * If the intent has a extra string with the name "DEFAULT_SELECTED_SOUND" then that string will be the default
 * checked string otherwise the first element in the custom_sounds array in the strings.xml file
 * will be checked
 *
 * When the back button is clicked, the change sounds page returns to the home screen. This will
 * have to be changed to the screen that we actuall want to return to. When it does this, it creates a new intent to
 * go to the desired screen with an extra string called "SELECTED_SOUND" that stores the name of the
 * sound that was selected.
 *
 */
public class CreateTimerDropDown extends DropDownReceiver implements OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    // set up the string so that when called the dropdown reciever knows what to open
    public static final String SHOW_CREATE = "com.atakmap.android.plugintemplate.CreateTimerDropDown";
    private final View templateView;
    private final Context pluginContext;
    private Timer timer;

    public CreateTimerDropDown(final MapView mapView,
                                final Context context) {
        super(mapView);
        this.pluginContext = context;
        timer = new Timer();


        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context, R.layout.create_timer_layout, null);
//        Button changeRepeatButton = (Button)templateView.findViewById(R.id.changeRepeatButton);
//        changeRepeatButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent();
//                i.setAction();
//                AtakBroadcast.getInstance().sendBroadcast(i);
//            }
//        });
        final EditText name = templateView.findViewById(R.id.timerName);
        final EditText time  = templateView.findViewById(R.id.duration);
        final CheckBox preset = templateView.findViewById(R.id.checkBox);

        Button changeSoundButton = (Button)templateView.findViewById(R.id.changeSoundButton);
        changeSoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("PAGE_TO_RETURN_TO", "CreateTimerDropDown");
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
                i.setAction(CustomizeNotificationsDropDown.SHOW_CHANGE_NOTIFICATIONS);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });
        Button submit = (Button)templateView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = name.getText().toString();
                timer.setName(nameStr);
                String duration = time.getText().toString();
                timer.setDuration(duration);
                timer.setPreset(preset.isChecked());
                Intent i = new Intent();
                i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                i.putExtra("TIMER", timer);
                AtakBroadcast.getInstance().sendBroadcast(i);
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
                    HALF_HEIGHT, false);

            if (intent.getStringExtra("SELECTED_SOUND") != null) {
                Button b = (Button)templateView.findViewById(R.id.changeSoundButton);
                b.setText(intent.getStringExtra("SELECTED_SOUND"));
                timer.setSound(intent.getStringExtra("SELECTED_SOUND"));
            }
            if(intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS") !=null) {
                ArrayList<String> act = (intent.getStringArrayListExtra("SELECTED_NOTIFICATIONS"));
                timer.setNotification(act);
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