package com.example.twistontime;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String CHANNEL_ID = "TIMER";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createNotificationChannel();
        Intent pauseIntent = new Intent(this, MainActivity.class);
        PendingIntent pausePendingIntent =
                PendingIntent.getBroadcast(this, 0, pauseIntent, 0);

        Intent resumeIntent = new Intent(this, MainActivity.class);
        PendingIntent resumePendingIntent =
                PendingIntent.getBroadcast(this, 0, resumeIntent, 0);
        Notification notification = new
                Notification.Builder(MainActivity.this)
                .setContentTitle("TimerNameHere")
                .setContentText("30 Seconds left - 0:00:30")
                .setSmallIcon(R.drawable.notification_icon)
                .setChannelId(CHANNEL_ID)
                .addAction(R.drawable.resume, "Resume", resumePendingIntent)
                .addAction(R.drawable.pause, "Pause",
                        pausePendingIntent)
                .addAction(R.drawable.pause, "Cancel",
                        pausePendingIntent)
                .build();
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(1, notification);


        Button preset_button = (Button)findViewById(R.id.presetButton);

        preset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PresetTimerScreen.class));
            }
        });
        Button create_button = (Button)findViewById(R.id.createNewButton);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateTimerScreen.class));
            }
        });
        TextView fakeTimer = (TextView)findViewById(R.id.fakeTimer);
        fakeTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditTimerScreen.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Timer Notification";
            String description = "who knows";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}