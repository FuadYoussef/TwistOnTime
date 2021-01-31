package com.example.twistontime;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


public class CustomizeRepeatScreen extends AppCompatActivity {
    Button closePopupBtn;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;

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
                String rbText = selectedRbText.replace('\n', ' ');
                // return repeat setting
                Intent resultIntent = new Intent();
                resultIntent.putExtra("TIMER_REPEAT_SETTING", rbText);
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
            //radioButton.setTextSize((int) getResources().getDimension(R.dimen.boxes_text_font_size));
            rprms= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rgp.addView(radioButton, rprms);
            if (repeat_settings_array[i].equals(defaultSelectedRepeat)) {
                rgp.check(i);
            }
            if (defaultSelectedRepeat != null && defaultSelectedRepeat.contains("Custom") && repeat_settings_array[i].toString().contains("Custom")) {
                String[] words = defaultSelectedRepeat.split(" ");
                String newText = words[0] + "\n" + words[1] + " " + words[2] + "\n" + words[3];
                radioButton.setText(newText);
                radioButton.setChecked(true);
            }
            if (defaultSelectedRepeat == null && i == 0) {
                rgp.check(0);
            }
            if (radioButton.getText().toString().contains("Custom")) {

                radioButton.setMaxLines(3);

                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //instantiate the popup.xml layout file
                        LayoutInflater layoutInflater = (LayoutInflater) CustomizeRepeatScreen.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View customView = layoutInflater.inflate(R.layout.customize_repeat_popup,null);

                        closePopupBtn = customView.findViewById(R.id.closePopupBtn);

                        // set number picker
                        NumberPicker picker1 = customView.findViewById(R.id.repeat_num_picker);
                        picker1.setMinValue(1);
                        picker1.setMaxValue(99);

                        // set spinner to store repeat options
                        //get the spinner from the xml.
                        Spinner dropdown = customView.findViewById(R.id.time_options_spinner);
                        //get list of items for the spinner.
                        String[] items = getResources().getStringArray(R.array.custom_repeat_options);

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CustomizeRepeatScreen.this, android.R.layout.simple_spinner_dropdown_item, items);
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
                        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout1);
                        popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

                        //close the popup window on button click
                        closePopupBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int numButtons = rgp.getChildCount();
                                for (int i = 0; i < numButtons; i++) {
                                    View o = rgp.getChildAt(i);
                                    if (o instanceof RadioButton) {
                                        RadioButton rb = (RadioButton)o;
                                        if (rb.getText().toString().contains("Custom")) {
                                            String updatedText = "Every\n" + picker1.getValue() + " " + dropdown.getSelectedItem().toString() + "\n(Custom)";
                                            rb.setText(updatedText);
                                        }
                                    }
                                }
                                popupWindow.dismiss();
                            }
                        });

                    }
                });
            }
        }

    }

}