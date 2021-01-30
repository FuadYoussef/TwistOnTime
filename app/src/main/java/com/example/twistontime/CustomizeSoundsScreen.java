
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


public class CustomizeSoundsScreen extends AppCompatActivity {

    /**
     * onCreate expects an extra string in the intent that contains the sound that should be pre-selected
     * but will default to the first sound option if nothing is passed in
     * back button returns the selected sounds as a string
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_sounds_screen);

        // get default sounds
        String defaultSelectedSound;
        if (getIntent().hasExtra("DEFAULT_SELECTED_SOUND")) {
            defaultSelectedSound = getIntent().getStringExtra("DEFAULT_SELECTED_SOUND");
        } else {
            defaultSelectedSound = null;
        }

        Button preset_button = (Button)findViewById(R.id.customizeSoundsBackButton);
        preset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // find which radio button is checked and get sound
                RadioGroup rgp = (RadioGroup) findViewById(R.id.custom_sounds_radiogroup);
                int selectedSoundsId = rgp.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedSoundsId);
                String selectedRbText = selectedRadioButton.getText().toString();
                // return sound
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TIMER_SOUND_SETTING", selectedRbText);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });

        // fill in radio buttons for the different sounds
        String[] sounds_array = getResources().getStringArray(R.array.custom_sounds);

        RadioGroup rgp = (RadioGroup) findViewById(R.id.custom_sounds_radiogroup);
        RadioGroup.LayoutParams rprms;

        for(int i = 0; i < sounds_array.length; i++){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(sounds_array[i]);
            radioButton.setId(i);
            radioButton.setTextSize((int) getResources().getDimension(R.dimen.boxes_text_font_size));
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
            if (sounds_array[i].equals(defaultSelectedSound)) {
                rgp.check(i);
            }
            if (defaultSelectedSound == null && i == 0) {
                rgp.check(0);
            }
        }

    }

}