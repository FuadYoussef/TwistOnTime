package com.atakmap.android.plugintemplate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * This class interfaces with notifications to process actions like pause or resume
 */
public class TimerNotificationActionReceiver extends BroadcastReceiver {
    /**
     * Override the onreceive function of broadcast receiver
     * @param context application context
     * @param intent passed in when creating the notification
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        int notifyId = intent.getIntExtra("activeTimer", -1);
        //break if notifyId is messed up
        if(notifyId == -1) {
            return;
        }
        ActiveTimer cur = PluginTemplateDropDownReceiver.timers.get(notifyId);
        String action = intent.getAction();
        if(action.equals("PAUSE")){
            Toast.makeText(context,"Paused", Toast.LENGTH_SHORT).show();
            cur.pause();

        } else if(action.equals("RESUME")){
            Toast.makeText(context,"Resumed", Toast.LENGTH_SHORT).show();
            cur.start();

        } else if(action.equals("CANCEL")) {
            Toast.makeText(context,"Cancelled", Toast.LENGTH_SHORT).show();
            cur.dismiss();
        }
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }


}