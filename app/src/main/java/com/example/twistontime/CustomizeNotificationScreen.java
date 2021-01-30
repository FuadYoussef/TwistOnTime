package com.example.twistontime;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;

        import androidx.appcompat.app.AppCompatActivity;

        import android.view.View;

        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.LinearLayout;

        import java.util.ArrayList;
        import java.util.Arrays;


public class CustomizeNotificationScreen extends AppCompatActivity {

    /**
     * onCreate expects an extra arraylist of strings in the intent that contains the notification(s)
     * that should be pre-selected
     * but will default to the first notification option (never) if nothing is passed in
     * back button returns the selected notification setting as an arraylist of strings
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                String[] notification_settings_array = getResources().getStringArray(R.array.custom_notification_settings);
                for (int i = 0; i < notification_settings_array.length; i++) {
                    CheckBox notificationCheckBox = (CheckBox) findViewById(i);
                    if (notificationCheckBox.isChecked()) {
                        currentSelectedNotifications.add(notificationCheckBox.getText().toString());
                    }
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
            }

            linearLayout.addView(checkBox);

        }

    }

}