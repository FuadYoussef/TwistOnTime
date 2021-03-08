package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;


//TODO: In-Depth Class desc

/**
 * This class handles the Preset Timer Screen of the plugin
 */

public class PresetComponent extends DropDownReceiver implements OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();
    public static final String SHOW_PRESETS_PAGE = "com.atakmap.android.plugintemplate.SHOW_PRESETS_PAGE";
    private final View templateView;
    public PresetComponent(final MapView mapView,
                               final Context context) {
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
    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    /**
     * Receives the intent from the Home Screen and transitions to the Preset Page
     * @param context context from the Home Screen
     * @param intent intent from the Home Screen
     */
    @Override
    public void onReceive(Context context, final Intent intent) {
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_PRESETS_PAGE)) {

            Log.d(TAG, "showing preset page");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, true);

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