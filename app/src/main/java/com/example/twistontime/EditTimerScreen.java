package com.example.twistontime;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditTimerScreen extends AppCompatActivity {

    Timer timer = new Timer();
    class Timer {
        public String name;
        public Time duration;
        public String repeat;
        public String notification;
        public String sound;
        public boolean preset = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timer_screen);

        CheckBox preset = (CheckBox) findViewById(R.id.checkBox);
        preset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.preset = !timer.preset;
                System.out.println(timer.preset);
            }});
        EditText name = (EditText) findViewById(R.id.timerName);
        Button repeat_button = (Button)findViewById(R.id.changeRepeatButton);
        repeat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTimerScreen.this, CustomizeRepeatScreen.class));
            }
        });
        Button notification_button = (Button)findViewById(R.id.addNotificationButton);
        notification_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTimerScreen.this, CustomizeNotificationScreen.class));
            }
        });
        Button sound_button = (Button)findViewById(R.id.changeSoundButton);
        sound_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTimerScreen.this, CustomizeSoundsScreen.class));
            }
        });
        Button submit_button = (Button)findViewById(R.id.submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTimerScreen.this, MainActivity.class));
            }
        });
        Button back_button = (Button)findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditTimerScreen.this, MainActivity.class));
            }
        });
    }
}
