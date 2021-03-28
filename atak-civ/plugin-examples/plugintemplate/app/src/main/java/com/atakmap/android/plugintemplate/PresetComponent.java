package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;

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

    /**************************** CONSTRUCTOR *****************************/
    /**
     * Constructor for the dropdown
     * @param mapView mapview needed for constructor
     * @param context context needed for constructor
     */
    public PresetComponent(final MapView mapView, final Context context) {
        super(mapView);

        templateView = PluginLayoutInflater.inflate(context, R.layout.preset_layout, null);

        Button back = templateView.findViewById(R.id.back);
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

        ImageButton add_new_preset = (ImageButton)templateView.findViewById(R.id.preset_add_button);
        add_new_preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: the intent created in this function will likely need at least one addition extra so
                // that the create/edit timer screen know to return to the preset screen and not the home screen
                // as well as not creating a running timer but just creating a new preset. It also might be good
                // to have the preset checkbox default to checked when the create new timer screen is called from
                // the preset screen
                Intent i = new Intent();
                i.setAction(CreateTimerDropDown.SHOW_CREATE);
                AtakBroadcast.getInstance().sendBroadcast(i);
            }
        });
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

            ArrayList<Timer> preset_timers = PluginTemplateDropDownReceiver.presets;
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