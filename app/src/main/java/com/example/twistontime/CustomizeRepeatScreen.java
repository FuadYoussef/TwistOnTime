package com.example.twistontime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class CustomizeRepeatScreen extends AppCompatActivity {

    /**
     * onCreate expects an extra string in the intent that contains the repeat that should be pre-selected
     * but will default to the first repeat option (never) if nothing is passed in
     * back button returns the selected repeat setting as a string
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_repeat_screen);

        // get default repeat setting
        String defaultSelectedRepeat;
        if (getIntent().hasExtra("DEFAULT_SELECTED_REPEAT")) {
            defaultSelectedRepeat = getIntent().getStringExtra("DEFAULT_SELECTED_REPEAT");
        } else {
            defaultSelectedRepeat = null;
        }

        Button preset_button = (Button)findViewById(R.id.customizeRepeatBackButton);
        preset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find which radio button is checked and get repeat
                RadioGroup rgp = (RadioGroup) findViewById(R.id.custom_repeat_radiogroup);
                int selectedrepeatId = rgp.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedrepeatId);
                String selectedRbText = selectedRadioButton.getText().toString();
                // return repeat setting
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TIMER_REPEAT_SETTING", selectedRbText);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        // fill in radio buttons for the different repeat settings
        String[] repeat_settings_array = getResources().getStringArray(R.array.custom_repeat_settings);

        RadioGroup rgp = (RadioGroup) findViewById(R.id.custom_repeat_radiogroup);
        RadioGroup.LayoutParams rprms;

        for(int i = 0; i < repeat_settings_array.length; i++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(repeat_settings_array[i]);
            radioButton.setId(i);
            radioButton.setTextSize((int) getResources().getDimension(R.dimen.boxes_text_font_size));
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
            if (repeat_settings_array[i].equals(defaultSelectedRepeat)) {
                rgp.check(i);
            }
            if (defaultSelectedRepeat == null && i == 0) {
                rgp.check(0);
            }
        }

    }

}