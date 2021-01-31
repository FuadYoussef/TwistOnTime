package com.example.twistontime;

        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;

        import android.util.DisplayMetrics;
        import android.view.Gravity;
        import android.view.LayoutInflater;
        import android.view.View;

        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.LinearLayout;
        import android.widget.NumberPicker;
        import android.widget.PopupWindow;
        import android.widget.RadioButton;
        import android.widget.Spinner;

        import java.util.ArrayList;


public class CustomizeNotificationScreen extends AppCompatActivity {
    Button closePopupBtn;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    int numBoxes = 0;

    /**
     * onCreate expects an extra arraylist of strings in the intent that contains the notification(s)
     * that should be pre-selected
     * but will default to the first notification option (never) if nothing is passed in
     * back button returns the selected notification setting as an arraylist of strings
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        numBoxes = getResources().getStringArray(R.array.custom_notification_settings).length;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_notifications_screen);

        // get default notification setting
        ArrayList<String> defaultSelectedNotifications;
        if (getIntent().hasExtra("DEFAULT_SELECTED_NOTIFICATIONS")) {
            defaultSelectedNotifications = getIntent().getStringArrayListExtra("DEFAULT_SELECTED_NOTIFICATIONS");
        } else {
            defaultSelectedNotifications = new ArrayList<>();
        }

        Button preset_button = (Button)findViewById(R.id.customizeNotificationBackButton);
        preset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find which notifications are checked and get those notification strings
                ArrayList<String> currentSelectedNotifications = new ArrayList<>();
                for (int i = 0; i < numBoxes; i++) {
                    CheckBox notificationCheckBox = (CheckBox) findViewById(i);
                    if (notificationCheckBox.isChecked()) {
                        currentSelectedNotifications.add(notificationCheckBox.getText().toString());
                    }
                    System.out.println(notificationCheckBox.getText());
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TIMER_NOTIFICATION_SETTINGS", currentSelectedNotifications);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        // make checkboxes for the different notifications
        String[] notification_settings_array = getResources().getStringArray(R.array.custom_notification_settings);

        for(int i = 0; i < notification_settings_array.length; i++){
            LinearLayout linearLayout = findViewById(R.id.notificationCheckboxContainer);

            // Create Checkbox Dynamically
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(notification_settings_array[i]);
            checkBox.setTextSize((int) getResources().getDimension(R.dimen.boxes_text_font_size));
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            if (defaultSelectedNotifications.contains(notification_settings_array[i])) {
                checkBox.setChecked(true);
                defaultSelectedNotifications.remove(notification_settings_array[i]);
            }

            if (notification_settings_array[i].contains("Custom")) {
                for (String s : defaultSelectedNotifications) {
                    // Create Checkbox Dynamically
                    CheckBox checkBox2 = new CheckBox(this);
                    checkBox2.setId(numBoxes);
                    checkBox2.setText(s);
                    checkBox2.setTextSize((int) getResources().getDimension(R.dimen.boxes_text_font_size));
                    checkBox2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    linearLayout.addView(checkBox2);
                    checkBox2.setChecked(true);
                    numBoxes++;
                }
            }

            linearLayout.addView(checkBox);

            if (checkBox.getText().toString().contains("Custom")) {

                checkBox.setMaxLines(3);

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkBox.isChecked()) {
                            return;
                        }
                        //instantiate the popup.xml layout file
                        LayoutInflater layoutInflater = (LayoutInflater) CustomizeNotificationScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = layoutInflater.inflate(R.layout.customize_notifications_popup,null);

                        closePopupBtn = customView.findViewById(R.id.closePopupBtn);

                        // set number picker
                        NumberPicker picker1 = customView.findViewById(R.id.notifications_num_picker);
                        picker1.setMinValue(1);
                        picker1.setMaxValue(99);

                        // set spinner to store repeat options
                        //get the spinner from the xml.
                        Spinner dropdown = customView.findViewById(R.id.time_options_spinner);
                        //get list of items for the spinner.
                        String[] items = getResources().getStringArray(R.array.custom_notifications_options);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomizeNotificationScreen.this, android.R.layout.simple_spinner_dropdown_item, items);
                        //set the spinners adapter to the previously created one.
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dropdown.setAdapter(adapter);


                        //instantiate popup window
                        popupWindow = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                        // Closes the popup window when touch outside.
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        // Removes default background.
                        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                        // give popup dimensions
                        DisplayMetrics dm = getResources().getDisplayMetrics();

                        int width = dm.widthPixels;
                        int height = dm.heightPixels;

                        popupWindow.setHeight((int) (height * 0.5));
                        popupWindow.setWidth((int) (width * 0.8));

                        //display the popup window
                        linearLayout1 = (LinearLayout) findViewById(R.id.notificationCheckboxContainer);
                        popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

                        //close the popup window on button click
                        closePopupBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String updatedText = picker1.getValue() + " " + dropdown.getSelectedItem().toString();
                                numBoxes++;
                                // Create Checkbox Dynamically
                                CheckBox checkBox = new CheckBox(CustomizeNotificationScreen.this);
                                checkBox.setId(numBoxes - 1);
                                checkBox.setText(updatedText);
                                checkBox.setTextSize((int) getResources().getDimension(R.dimen.boxes_text_font_size));
                                checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                checkBox.setChecked(true);
                                linearLayout.addView(checkBox, numBoxes - 2);
                                popupWindow.dismiss();
                                CheckBox cb = (CheckBox) linearLayout.getChildAt(numBoxes - 1);
                                cb.setChecked(false);
                            }
                        });

                    }
                });
            }
        }

    }

}