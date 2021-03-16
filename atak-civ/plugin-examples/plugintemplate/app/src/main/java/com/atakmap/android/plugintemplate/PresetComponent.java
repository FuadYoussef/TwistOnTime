package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;

import java.util.ArrayList;


/**
 * This class handles the Preset Timer Screen of the plugin. It should be called with the intent
 * action being the SHOW_PRESETS_PAGE string. This dropdown uses a recyclerview to display the preset
 * timers. Each preset timer can be edited by clicking on the timer name or duration, it can be set to run
 * by clicking on the set button and the preset can be deleted by clicking on the delete button. This
 * functionality is handled within the PresetTimerListAdapter class.
 */

public class PresetComponent extends DropDownReceiver implements OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class.getSimpleName();
    public static final String SHOW_PRESETS_PAGE = "com.atakmap.android.plugintemplate.SHOW_PRESETS_PAGE";
    private final View templateView;
    private RecyclerView mainScreenTimerList;
    private PresetTimerListAdapter adapter;


    public PresetComponent(final MapView mapView, final Context context) {
        super(mapView);

        templateView = PluginLayoutInflater.inflate(context, R.layout.preset_layout, null);

        Button back = (Button)templateView.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });

        //sets up the list adapter and layout manager for multiple timers
        mainScreenTimerList = templateView.findViewById(R.id.preset_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        mainScreenTimerList.setLayoutManager(manager);
    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    /**
     * Receives the intent from the Home Screen and transitions to the Preset Page
     * Also displays the existing preset timers
     * @param context context from the Home Screen
     * @param intent intent from the Home Screen
     */
    @Override
    public void onReceive(Context context, final Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PRESETS_PAGE)) {

            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, true);

            //TODO: replace this with call to read json to get list of timers
            ArrayList<Timer> preset_timers = new ArrayList<>();

            Timer timer1 = new Timer();
            timer1.setName("Timer 1");
            timer1.setPreset(true);
            timer1.setHours(1);
            timer1.setMinutes(20);
            timer1.setSeconds(30);
            timer1.setSound("Chime");
            timer1.setNotifications(new ArrayList<String>());

            Timer timer2 = new Timer();
            timer2.setName("Timer 2");
            timer2.setPreset(true);
            timer2.setHours(0);
            timer2.setMinutes(15);
            timer2.setSeconds(45);
            timer2.setSound("Alarm");
            timer2.setNotifications(new ArrayList<String>());

            Timer timer3 = new Timer();
            timer3.setName("Timer 3");
            timer3.setPreset(true);
            timer3.setHours(0);
            timer3.setMinutes(0);
            timer3.setSeconds(45);
            timer3.setSound("Radar");
            timer3.setNotifications(new ArrayList<String>());

            preset_timers.add(timer1);
            preset_timers.add(timer2);
            preset_timers.add(timer3);

            adapter = new PresetTimerListAdapter(preset_timers);
            mainScreenTimerList.setAdapter(adapter);

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