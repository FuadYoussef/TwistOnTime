package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.dropdown.DropDown.OnStateListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomizeNotificationsDropDown  extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    // set up the string so that when called the dropdown reciever knows what to open
    public static final String SHOW_CHANGE_NOTIFICATIONS = "com.atakmap.android.plugintemplate.SHOW_CHANGE_NOTIFICATIONS";
    private final View templateView;
    private final Context pluginContext;
    private int numBoxes = 0;

    public CustomizeNotificationsDropDown(final MapView mapView,
                                final Context context) {
        super(mapView);
        this.pluginContext = context;

        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context, R.layout.customize_notifications_layout, null);
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

        if (action.equals(SHOW_CHANGE_NOTIFICATIONS)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false);

            // ---- set up radio buttons ----
            ArrayList<String> defaultSelectedNotifications = getDefaultNotifications(intent);
            createAndCheckNotifications(defaultSelectedNotifications);

            // set up back button
            Button b = (Button)templateView.findViewById(R.id.back_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // return to desired screen. The return intent also stores an arraylist of the
                    // selected notifications
                    Intent i = getReturnIntent(intent);
                    clearAll();
                    AtakBroadcast.getInstance().sendBroadcast(i);
                }
            });
        }
    }

    private Intent getReturnIntent(Intent intent) {
        String toReturn = intent.getStringExtra("PAGE_TO_RETURN_TO");

        Intent i = new Intent();

        // if not specified return intent will be set to return to the home screen
        if (toReturn == null) {
            i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
        } else {
            switch (toReturn) {
                case "PluginTemplateDropDownReceiver":
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                    break;
                default:
                    // in case bad input
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
            }
        }
        i.putExtra("SELECTED_NOTIFICATIONS", getAllChecked());
        return i;
    }

    /*
    Gets all checked notifications
     */
    private ArrayList<String> getAllChecked() {
        ArrayList<String> currentSelectedNotifications = new ArrayList<>();
        for (int i = 0; i < numBoxes; i++) {
            CheckBox notificationCheckBox = (CheckBox) templateView.findViewById(i);
            if (notificationCheckBox.isChecked()) {
                currentSelectedNotifications.add(notificationCheckBox.getText().toString());
            }
        }
        return currentSelectedNotifications;
    }

    /*
    given a string of notifications that should be checked this creates the entire list of notifications
    and checks whichever ones should be checked
     */
    private void createAndCheckNotifications(ArrayList<String> notifications) {
        String[] notifications_array = pluginContext.getResources().getStringArray(R.array.custom_notification_settings);
        // go through all of the notifications in the strings.xml file except the one whose text is "custom"
        for (int i = 0; i < notifications_array.length - 1; i++) {
            CheckBox checkBox = createCheckBox(numBoxes, notifications_array[i], false);
            if (notifications.contains(notifications_array[i])) {
                notifications.remove(notifications_array[1]);
                checkBox.setChecked(true);
            }
        }
        for (int i = 0; i < notifications.size(); i++) {
            createCheckBox(numBoxes, notifications.get(i), true);
        }
        // create custom checkbox
        CheckBox customCheckBox = createCheckBox(numBoxes, notifications_array[notifications_array.length - 1], false);
    }

    /*
    creates a checkbox with the given id, text, and checked values and adds the checkbox to the view
    also increments numBoxes
     */
    private CheckBox createCheckBox(int id, String text, boolean checked) {
        CheckBox checkBox = new CheckBox(pluginContext);
        checkBox.setId(id);
        checkBox.setText(text);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setChecked(checked);
        numBoxes++;
        LinearLayout linearLayout = templateView.findViewById(R.id.notification_options_zone);
        linearLayout.addView(checkBox);
        return checkBox;
    }

    /*
    Gets the list of default notificaitons from the intent
     */
    private ArrayList<String> getDefaultNotifications(Intent intent) {
        ArrayList<String> defaultSelectedNotifications = intent.getStringArrayListExtra("DEFAULT_SELECTED_NOTIFICATIONS");
        if (defaultSelectedNotifications == null) {
            defaultSelectedNotifications = new ArrayList<>();
        }
        return defaultSelectedNotifications;
    }

    /*
    removes all the checkboxes from the linearlayout and resets the numBoxes counter
     */
    private void clearAll() {
        LinearLayout linearLayout = templateView.findViewById(R.id.notification_options_zone);
        linearLayout.removeAllViews();
        numBoxes = 0;
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