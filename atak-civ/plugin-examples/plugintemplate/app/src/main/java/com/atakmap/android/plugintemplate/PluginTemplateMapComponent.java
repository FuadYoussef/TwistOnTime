package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDownMapComponent;

import com.atakmap.coremap.log.Log;
import com.atakmap.android.plugintemplate.plugin.R;

public class PluginTemplateMapComponent extends DropDownMapComponent {

    private static final String TAG = "PluginTemplateMapComponent";

    private Context pluginContext;

    private PluginTemplateDropDownReceiver ddr;
    private ChangeSoundsDropDown csr;
    private CustomizeNotificationsDropDown cnr;
    private CreateCustomNotificationDropDown ccnr;
    private CreateTimerDropDown ctr;


    public void onCreate(final Context context, Intent intent,
                         final MapView view) {

        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, view);
        pluginContext = context;

        ddr = new PluginTemplateDropDownReceiver(
                view, context);

        Log.d(TAG, "registering the plugin filter");
        DocumentedIntentFilter ddFilter = new DocumentedIntentFilter();
        ddFilter.addAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
        registerDropDownReceiver(ddr, ddFilter);
        // basically registering the ChangeSoundsDropDown -> need to do this or broadcasting intent won't work
        DocumentedIntentFilter csFilter = new DocumentedIntentFilter();
        csFilter.addAction(ChangeSoundsDropDown.SHOW_CHANGE_SOUNDS);
        csr = new ChangeSoundsDropDown(view, context);
        registerDropDownReceiver(csr, csFilter);

        // same for ChangeNotificationsDropDown
        DocumentedIntentFilter cnFilter = new DocumentedIntentFilter();
        cnFilter.addAction(CustomizeNotificationsDropDown.SHOW_CHANGE_NOTIFICATIONS);
        cnr = new CustomizeNotificationsDropDown(view, context);
        registerDropDownReceiver(cnr, cnFilter);

        // same for CreateCustomNotificationDropDown
        DocumentedIntentFilter ccnFilter = new DocumentedIntentFilter();
        ccnFilter.addAction(CreateCustomNotificationDropDown.SHOW_CREATE_CUSTOM_NOTIFICATION_SCREEN);
        ccnr = new CreateCustomNotificationDropDown(view, context);
        registerDropDownReceiver(ccnr, ccnFilter);

        //create timer dropwon
        DocumentedIntentFilter createTimerFilter = new DocumentedIntentFilter();
        createTimerFilter.addAction(CreateTimerDropDown.SHOW_CREATE);
        ctr = new CreateTimerDropDown(view, context);
        registerDropDownReceiver(ctr, createTimerFilter);
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        super.onDestroyImpl(context, view);
    }

}