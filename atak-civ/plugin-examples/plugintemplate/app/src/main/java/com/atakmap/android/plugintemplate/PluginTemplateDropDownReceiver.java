package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import java.util.ArrayList;
import java.util.HashMap;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;

//TODO: Class desc

/**
 * This class handles the creation and display of the home screen of the Twist on Time Plugin.
 */



public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugintemplate.SHOW_PLUGIN";

    private final View templateView;
    private final Context pluginContext;
    private RecyclerView mainScreenTimerList;
    public static ArrayList<ActiveTimer> timers = new ArrayList<>();
    public static HashMap<Integer, ActiveTimer> timerNotifMap = new HashMap<>();
    private TimerListAdapter adapter;
    private final MapView mapView;
    private int notificationCount = 0;


    /**************************** CONSTRUCTOR *****************************/

    public PluginTemplateDropDownReceiver(final MapView mapView,
                                          final Context context) {
        super(mapView);
        this.pluginContext = context;
        this.mapView = mapView;
        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context, R.layout.main_layout, null);

        //sets up the list adapter and layout manager for multiple timers
        mainScreenTimerList = templateView.findViewById(R.id.timer_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        mainScreenTimerList.setLayoutManager(manager);

        adapter = new TimerListAdapter(timers);
        mainScreenTimerList.setAdapter(adapter);


        templateView.findViewById(R.id.add_button)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRetain(true);
                        // how to call customizeNotificaitonDropDown
                        Intent i = new Intent();
                        i.setAction(CreateTimerDropDown.SHOW_CREATE);
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
    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    /**
     * Receives context information from the main application when the plugin is launched
     * @param context context received from the main application
     * @param intent intent received from the main application
     */
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
                //we pass the adapter we use for our recycler view to each ActiveTimer so that as it counts down it forces the adapter to update
                //we pass the plugin context to the ActiveTimer so that it knows the context to make a sound
                timers.add(new ActiveTimer(timer,  notificationCount++, adapter, pluginContext, mapView));
                timerNotifMap.put(notificationCount-1, timers.get(timers.size()-1));
                adapter.notifyDataSetChanged();
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