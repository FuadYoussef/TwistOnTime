
package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;

import com.atakmap.coremap.log.Log;

public class PluginTemplateDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugintemplate.SHOW_PLUGIN";
    private final View templateView;
    private final Context pluginContext;
    private final ChangeSoundsDropDown changeSoundsView;


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
        changeSoundsView = new ChangeSoundsDropDown(getMapView(), pluginContext);
        templateView.findViewById(R.id.add_button)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setRetain(true);
                        changeSoundsView.showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                                HALF_HEIGHT, false);
                        //Intent i2 = new Intent(ChangeSoundsDropDown.SHOW_CHANGE_SOUNDS);
                        //AtakBroadcast.getInstance().sendBroadcast(i2);
                        Intent i = new Intent();
                        i.setAction(ChangeSoundsDropDown.SHOW_CHANGE_SOUNDS);
                        AtakBroadcast.getInstance().sendBroadcast(i);
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
                    HALF_HEIGHT, false);
            /*
            Button b = (Button)templateView.findViewById(R.id.add_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button b = (Button)templateView.findViewById(R.id.add_button);
                    b.setText("5");
                    //TabViewDropDown tabView = new TabViewDropDown(getMapView(), pluginContext);
                    //tabView.showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH, HALF_HEIGHT, false);

                    Intent i2 = new Intent(
                            ChangeSoundsDropDown.SHOW_CHANGE_SOUNDS);
                    AtakBroadcast.getInstance().sendBroadcast(i2);

                }
            });
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
