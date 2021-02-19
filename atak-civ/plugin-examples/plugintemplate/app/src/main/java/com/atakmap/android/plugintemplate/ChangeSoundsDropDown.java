package com.atakmap.android.plugintemplate;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.dropdown.DropDownReceiver;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugintemplate.plugin.R;
import com.atakmap.coremap.log.Log;
import com.atakmap.android.dropdown.DropDown.OnStateListener;

import java.util.Arrays;

/**
 * The intent used when calling ChangeSoundsDropDown must contain a extra string with the name
 * "PAGE_TO_RETURN_TO" with the name of the page that this page should return to. Otherwise this
 * will default to the homescreen. Code in this file also need to be updated to deal with returning to
 * other screens
 *
 * If the intent has a extra string with the name "DEFAULT_SELECTED_SOUND" then that string will be the default
 * checked string otherwise the first element in the custom_sounds array in the strings.xml file
 * will be checked
 *
 * When the back button is clicked, the change sounds page returns to the home screen. This will
 * have to be changed to the screen that we actually want to return to. When it does this, it creates a new intent to
 * go to the desired screen with an extra string called "SELECTED_SOUND" that stores the name of the
 * sound that was selected.
 *
 */
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


        createRadioButtons();
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

        if (action.equals(SHOW_CHANGE_SOUNDS)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(templateView, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT, false);

            // ---- set up radio buttons ----
            String defaultSelectedSound = getDefaultSound(intent);
            checkButton(defaultSelectedSound);

            // set up back button
            Button b = (Button)templateView.findViewById(R.id.back_button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // find which radio button is checked and get sound
                    Intent i = getReturnIntent(intent);
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
        i.putExtra("SELECTED_SOUND", getChecked());
        return i;
    }

    private String getChecked() {
        RadioGroup rgp = (RadioGroup) templateView.findViewById(R.id.sounds_radio_group);
        int selectedSoundsId = rgp.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = templateView.findViewById(selectedSoundsId);
        String selectedRbText = selectedRadioButton.getText().toString();
        return selectedRbText;
    }

    private void checkButton(String toCheck) {
        String[] sounds_array = pluginContext.getResources().getStringArray(R.array.custom_sounds);
        int index = Arrays.asList(sounds_array).indexOf(toCheck);
        RadioGroup rgp = (RadioGroup) templateView.findViewById(R.id.sounds_radio_group);
        rgp.check(index);
    }

    private String getDefaultSound(Intent intent) {
        String[] sounds_array = pluginContext.getResources().getStringArray(R.array.custom_sounds);
        String defaultSelectedSound;
        if (intent.hasExtra("DEFAULT_SELECTED_SOUND")) {
            defaultSelectedSound = intent.getStringExtra("DEFAULT_SELECTED_SOUND");
            if (!Arrays.asList(sounds_array).contains(defaultSelectedSound)) {
                defaultSelectedSound = sounds_array[0];
            }
        } else {
            defaultSelectedSound = sounds_array[0];
        }
        return defaultSelectedSound;
    }

    private void createRadioButtons() {
        String[] sounds_array = pluginContext.getResources().getStringArray(R.array.custom_sounds);
        RadioGroup rgp = (RadioGroup) templateView.findViewById(R.id.sounds_radio_group);
        RadioGroup.LayoutParams rprms;

        for(int i = 0; i < sounds_array.length; i++){
            RadioButton radioButton = new RadioButton(pluginContext);
            radioButton.setText(sounds_array[i]);
            radioButton.setId(i);
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
            if (i == 0) {
                rgp.check(i);
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