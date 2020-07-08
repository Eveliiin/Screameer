package com.example.screameer;

import android.app.IntentService;
import android.content.BroadcastReceiver ;
import android.content.Context ;
import android.content.Intent ;
import android.widget.Toast ;



public class NotificationBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context , Intent intent) {
        String action = intent.getAction() ;
        if (action.equals( "notification_cancelled" )) {

            Intent closeIntent = new Intent(context,ScreamService.class);
            closeIntent.putExtra("Command","STOP");
            context.startService(closeIntent);
            //Toast. makeText (context , "Notification Removed" , Toast. LENGTH_SHORT ).show() ;
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("message","Notification closed");
            context.startActivity(i);
        }
    }
}