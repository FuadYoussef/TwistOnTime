package com.atakmap.android.plugintemplate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
        ActiveTimer cur = PluginTemplateDropDownReceiver.timerNotifMap.get(notifyId);
        String action = intent.getAction();
        Log.d("TAG", action);
        if(action.equals("PAUSE")){
            Toast.makeText(context,cur.getName() + " Paused", Toast.LENGTH_SHORT).show();
            cur.pause();

        } else if(action.equals("RESUME")){
            Toast.makeText(context,cur.getName() + " Resumed", Toast.LENGTH_SHORT).show();
            cur.start();

        } else if(action.equals("CANCEL")) {
            Toast.makeText(context,cur.getName() + " Cancelled", Toast.LENGTH_SHORT).show();
            cur.dismiss();
        } else if(action.equals("RESTART")) {
            Toast.makeText(context,cur.getName() + " Restarted", Toast.LENGTH_SHORT).show();
            cur.reset();
        } else if (action.equals("DELETE")) {
            cur.updateNotification = false;
            Log.d("TAG", "here");
        }
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }


}