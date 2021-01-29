
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_sounds_screen);

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
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
            if (i == 0) {
                rgp.check(0);
            }
        }

    }

}