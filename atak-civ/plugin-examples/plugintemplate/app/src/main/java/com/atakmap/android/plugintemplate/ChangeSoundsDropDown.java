package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.dropdown.DropDown.OnStateListener;


public class ChangeSoundsDropDown extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    // set up the string so that when called the dropdown reciever knows what to open
    public static final String SHOW_CHANGE_SOUNDS = "com.atakmap.android.plugintemplate.SHOW_CHANGE_SOUNDS";
    private final View templateView;
    private final Context pluginContext;

    public ChangeSoundsDropDown(final MapView mapView,
                           final Context context) {
        super(mapView);
        this.pluginContext = context;

        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context, R.layout.change_sounds_layout, null);
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

        if (action.equals(SHOW_CHANGE_SOUNDS)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false);
            Button b = (Button)templateView.findViewById(R.id.back_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                    AtakBroadcast.getInstance().sendBroadcast(i);
                    //TabViewDropDown tabView = new TabViewDropDown(getMapView(), pluginContext);
                    //tabView.showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT, false);

                }
            });
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

