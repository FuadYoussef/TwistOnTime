package com.atakmap.android.plugintemplate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class TimerNotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notifyId = intent.getIntExtra("activeTimer", -1);
        Log.d("MYTAG", "" + notifyId);
        ActiveTimer cur = PluginTemplateDropDownReceiver.timers.get(notifyId);
        String action = intent.getAction();
        if(action.equals("PAUSE")){
            Toast.makeText(context,"Paused", Toast.LENGTH_SHORT).show();
            cur.pause();

        }
        else if(action.equals("RESUME")){
            Toast.makeText(context,"Resumed", Toast.LENGTH_SHORT).show();
            cur.start();

        }
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }


}