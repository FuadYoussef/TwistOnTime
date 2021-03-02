package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.CustomizeNotificationsDropDown;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;

import com.atakmap.coremap.log.Log;

import java.util.ArrayList;

/**
 * This screen is used to create a custom notification. It is expected to only communicate with the
 * CustomizeNotificationsDropDown class. From the CustomizeNotificationsDropDown, it expects an intent with
 * the name of the screen that the CustomizeNotificationsDropDown should return too (that should be in the intent
 * the CustomizeNotificationsDropDown received when it was called). It also expects a list of the notifications
 * that were checked in the CustomizeNotificationsDropDown so that it can send that list back along with the
 * new notification
 */
public class CreateCustomNotificationDropDown extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = CreateCustomNotificationDropDown.class
            .getSimpleName();

    public static final String SHOW_CREATE_CUSTOM_NOTIFICATION_SCREEN = "com.atakmap.android.plugintemplate.SHOW_CREATE_CUSTOM_NOTIFICATION_SCREEN";
    private final View templateView;
    private final Context pluginContext;
    private Intent initialIntent;


    /**************************** CONSTRUCTOR *****************************/
    /**
     * Constructor for the dropdown
     * @param mapView mapview needed for constructor
     * @param context context needed for constructor
     */
    public CreateCustomNotificationDropDown(final MapView mapView,
                                          final Context context) {
        super(mapView);
        this.pluginContext = context;

        // Remember to use the PluginLayoutInflator if you are actually inflating a custom view
        // In this case, using it is not necessary - but I am putting it here to remind
        // developers to look at this Inflator
        templateView = PluginLayoutInflater.inflate(context, R.layout.create_custom_notification_layout, null);

    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    /**
     * This method sets up the createCustomNotificationsDropDown based on the passed in intent
     * @param context the context for the screen
     * @param intent The intent used to call this screen. This intent contains a string extra "PAGE_TO_RETURN_TO" that
     * is used so that when the CreateCustomNotificationDropDown returns to the CustomizeNotificationsDropDown,
     * the CustomizeNotificationsDropDown know what page it should return to when it is closed.
     * It also contains a extra string arraylist of the notifications that are checked. This page adds one
     * notification to the list and then the list is also passed back to the CustomizeNotificationsDropDown
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        this.initialIntent = intent;
        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_CREATE_CUSTOM_NOTIFICATION_SCREEN)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, true);

            instantiateScreen();
        }
    }

    /**
     * This method sets up the create custom notification screen. It sets up the two number pickers
     * corresponding to the number and units (seconds, minutes, hours) of the notification
     */
    private void instantiateScreen() {
        // set number picker
        final NumberPicker numValuePicker = templateView.findViewById(R.id.notifications_num_picker);
        numValuePicker.setMinValue(1);
        numValuePicker.setMaxValue(99);
        numValuePicker.setValue(1);
        // set onValueChange for numValuePicker so that timeStringPicker shows the correct strings
        numValuePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // Save the value in the number picker
                picker.setValue(newVal);
                if (newVal == 1) {
                    setupTimeStringPicker(false);
                } else {
                    setupTimeStringPicker(true);
                }
            }
        });

        // set second num picker to store repeat options
        setupTimeStringPicker(false);

        // set on click for set notification button
        Button setNotificationBtn = templateView.findViewById(R.id.setNotificationButton);
        setNotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = numValuePicker.getValue();
                NumberPicker timeStringPicker = templateView.findViewById(R.id.notifications_string_picker);
                int unit_index = timeStringPicker.getValue();
                String units;
                if (count == 1) {
                    units = pluginContext.getResources().getStringArray(R.array.custom_notifications_options_singluar)[unit_index];
                }
                else {
                    units = pluginContext.getResources().getStringArray(R.array.custom_notifications_options_plural)[unit_index];
                }
                String newNotification = count + " " + units;
                // send intent back with this info
                Intent i = new Intent();
                i.setAction(CustomizeNotificationsDropDown.SHOW_CHANGE_NOTIFICATIONS);
                i.putExtra("PAGE_TO_RETURN_TO", CreateCustomNotificationDropDown.this.initialIntent.getStringExtra("PAGE_TO_RETURN_TO"));
                ArrayList<String> selectedNotifications = CreateCustomNotificationDropDown.this.initialIntent.getStringArrayListExtra("DEFAULT_SELECTED_NOTIFICATIONS");
                selectedNotifications.add(newNotification);
                i.putExtra("DEFAULT_SELECTED_NOTIFICATIONS", selectedNotifications);
                //broadcast intent
                AtakBroadcast.getInstance().sendBroadcast(i);

            }
        });

    }

    /**
     * This method sets up the num picker that corresponds to the units of the notification
     * (seconds, minutes, etc). It takes in boolean plural to determine if values in num picker
     * should be plural or not
     * @param plural boolean used to determine if the units should be plural or singular
     */
    private void setupTimeStringPicker(boolean plural) {
        String[] items;
        if (plural) {
            items = pluginContext.getResources().getStringArray(R.array.custom_notifications_options_plural);
        } else {
            items = pluginContext.getResources().getStringArray(R.array.custom_notifications_options_singluar);
        }
        NumberPicker timeStringPicker = templateView.findViewById(R.id.notifications_string_picker);
        timeStringPicker.setMinValue(0);
        timeStringPicker.setMaxValue(items.length - 1);
        timeStringPicker.setDisplayedValues(items);
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