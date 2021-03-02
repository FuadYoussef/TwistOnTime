package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
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

/**
 * The CustomizeNotificationsDropDown should be called when the user is trying to change the notifications
 * a timer should make. it allows the user to select from a list of possible notifications or create their own
 *
 * The intent used when calling CustomizeNotificationsDropDown must contain a extra string with the name
 * "PAGE_TO_RETURN_TO" with the name of the page that this page should return to. Otherwise this
 * will default to the homescreen. Code in this file also need to be updated to deal with returning to other
 * screens
 *
 * If the intent has a extra string arraylist with the name "DEFAULT_SELECTED_NOTIFICATIONS" then
 * the notifications corresponding to those strings will be checked otherwise nothing will be checked
 *
 * When the back button is clicked, the customize notifications page returns to the home screen. This will
 * have to be changed to the desired screen to return to. When it does this, it creates a new intent to
 * go to the desired with an extra string arraylist called "SELECTED_NOTIFICATIONS" that stores the names of the
 * notifications that were selected.
 *
 */
public class CustomizeNotificationsDropDown  extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PluginTemplateDropDownReceiver.class
            .getSimpleName();

    // set up the string so that when called the dropdown reciever knows what to open
    public static final String SHOW_CHANGE_NOTIFICATIONS = "com.atakmap.android.plugintemplate.SHOW_CHANGE_NOTIFICATIONS";
    private final View templateView;
    private final Context pluginContext;
    private int numBoxes = 0;
    private Intent intitialIntent;

    /**
     * Constructor for the dropdown
     * @param mapView mapview needed for constructor
     * @param context context needed for constructor
     */
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

    /**
     * This method sets up the dropdown when it is opened. It clears whatever setting were previously
     * on the dropdown and adds the list of possible notifications, adds and checks any notifications
     * that were passed in with the intent, and sets the back button onclick
     * @param context the context for the screen
     * @param intent the intent used to call this screen. The intent used when calling
     * CustomizeNotificationsDropDown must contain a extra string with the name "PAGE_TO_RETURN_TO"
     * with the name of the page that this page should return to. Otherwise this
     * will default to the homescreen. If the intent has a extra string arraylist with the name
     * "DEFAULT_SELECTED_NOTIFICATIONS" then the notifications corresponding to those strings will
     * be checked otherwise nothing will be checked
     */
    @Override
    public void onReceive(Context context, final Intent intent) {

        this.intitialIntent = intent;

        final String action = intent.getAction();
        if (action == null)
            return;

        if (action.equals(SHOW_CHANGE_NOTIFICATIONS)) {

            Log.d(TAG, "showing plugin drop down");

            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, true);

            // ---- set up radio buttons ----

            ArrayList<String> defaultSelectedNotifications = getDefaultNotifications();

            createAndCheckNotifications(defaultSelectedNotifications);

            // set up back button
            Button b = (Button)templateView.findViewById(R.id.back_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // return to desired screen. The return intent also stores an arraylist of the
                    // selected notifications
                    Intent i = getReturnIntent();
                    clearAll();
                    AtakBroadcast.getInstance().sendBroadcast(i);
                }
            });
        }
    }

    /**
     * This method generate the intent used to exit this dropdown.
     * @return an intent used to return to a different screen and exit the dropdown. The intent
     * contains an extra string arraylist called "SELECTED_NOTIFICATIONS" that stores the names
     * of the notifications that were selected.
     */
    private Intent getReturnIntent() {
        String toReturn = intitialIntent.getStringExtra("PAGE_TO_RETURN_TO");

        Intent i = new Intent();

        // if not specified return intent will be set to return to the home screen
        if (toReturn == null) {
            i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
        } else {
            switch (toReturn) {
                case "PluginTemplateDropDownReceiver":
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
                    break;
                case "CreateTimerDropDown":
                    i.setAction(CreateTimerDropDown.SHOW_CREATE);
                    break;
                default:
                    // in case bad input
                    i.setAction(PluginTemplateDropDownReceiver.SHOW_PLUGIN);
            }
        }
        i.putExtra("SELECTED_NOTIFICATIONS", getAllChecked());
        return i;
    }

    /**
     * This method gets a list of all the notifications there were check (except the custom button,
     * though that should never be checked)
     * @return a string arraylist of all checked notifications (except custom)
     */
    private ArrayList<String> getAllChecked() {
        ArrayList<String> currentSelectedNotifications = new ArrayList<>();
        for (int i = 0; i < numBoxes; i++) {
            CheckBox notificationCheckBox = (CheckBox) templateView.findViewById(i);
            if (notificationCheckBox.isChecked()) {
                if (!notificationCheckBox.getText().toString().contains("Custom")) {
                    currentSelectedNotifications.add(notificationCheckBox.getText().toString());
                }
            }
        }
        return currentSelectedNotifications;
    }

    /**
     * This method takes in a list of notifications that should be checked and adds them to the screen
     * and checks them. It also makes sure to not add duplicates
     * @param notifications String arraylist of notifications to check
     */
    private void createAndCheckNotifications(ArrayList<String> notifications) {
        String[] notifications_array = pluginContext.getResources().getStringArray(R.array.custom_notification_settings);
        // keep track of all the strings that have already been used to create notifications (to deal with duplicates)
        ArrayList<String> usedNotificationStrings = new ArrayList<>();
        // go through all of the notifications in the strings.xml file except the one whose text is "custom"
        for (int i = 0; i < notifications_array.length - 1; i++) {
            if (!usedNotificationStrings.contains(notifications_array[i])) {
                CheckBox checkBox = createCheckBox(numBoxes, notifications_array[i], false);
                usedNotificationStrings.add(notifications_array[i]);
                if (notifications.contains(notifications_array[i])) {
                    notifications.remove(notifications_array[i]);
                    checkBox.setChecked(true);
                }
            }
        }
        for (int i = 0; i < notifications.size(); i++) {
            if (!usedNotificationStrings.contains(notifications.get(i))) {
                createCheckBox(numBoxes, notifications.get(i), true);
                usedNotificationStrings.add(notifications.get(i));
            }
        }
        // create custom checkbox
        final CheckBox customCheckBox = createCheckBox(numBoxes, notifications_array[notifications_array.length - 1], false);
        // set custom onclick to create a popup that can be used to create a custom notification
        customCheckBox.setMaxLines(3);
        customCheckBox.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v){
                if (!customCheckBox.isChecked()) {
                    return;
                }
                // sends intent to popup screen and creates and sends intent to go to that screen
                goToCreateCustomNotificationScreen();
            }
        });
    }

    /**
     * This method is called when the custom button is click. It creates a new intent to open the
     * CreateCustomNotificationDropDown. This intent contains a string extra "PAGE_TO_RETURN_TO" that
     * is used so that when the CreateCustomNotificationDropDown returns this dropdown know what page
     * it should return to when closed. It also contains a extra string arraylist of the notifications
     * that are checked. This is also passed back with the extra notification added when the
     * CreateCustomNotificationDropDown returns.
     */
    private void goToCreateCustomNotificationScreen() {
        // create a new intent to go to the createCustomNotificationScreen, this intent will mirror the
        // intent sent to this dropdown so that this downdown returns to the correct location
        Intent i = new Intent();
        i.setAction(CreateCustomNotificationDropDown.SHOW_CREATE_CUSTOM_NOTIFICATION_SCREEN);
        i.putExtra("PAGE_TO_RETURN_TO", intitialIntent.getStringExtra("PAGE_TO_RETURN_TO"));
        i.putExtra("DEFAULT_SELECTED_NOTIFICATIONS", getAllChecked());
        // clear screen and broadcast intent
        clearAll();
        AtakBroadcast.getInstance().sendBroadcast(i);
    }

    /**
     * This creates a checkbox for a notification
     * @param id the id for the checkbox
     * @param text the text for the checkbox (this is the notification text)
     * @param checked if the checkbox should be checked
     * @return the created checkbox
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

    /**
     * This method is used to get the DEFAULT_SELECTED_NOTIFICATIONS arraylist from the intent
     * used to open this dropdown. If that string doesn't exist it returns an empty arraylist
     * @return a string arraylist of notifications that should be checked when creating this screen
     */
    private ArrayList<String> getDefaultNotifications() {
        ArrayList<String> defaultSelectedNotifications = intitialIntent.getStringArrayListExtra("DEFAULT_SELECTED_NOTIFICATIONS");
        if (defaultSelectedNotifications == null) {
            defaultSelectedNotifications = new ArrayList<>();
        }
        return defaultSelectedNotifications;
    }

    /**
     * This method removes all the checkboxes from the linearlayout and resets the numBoxes counter
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