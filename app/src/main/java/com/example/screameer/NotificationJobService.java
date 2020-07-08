package com.example.screameer;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class NotificationJobService extends JobService {
    NotificationManager mNotifyManager;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "Notification";
    private static final int NOTIFICATION_ID =1;

    @Override
    public boolean onStartJob(JobParameters params) {
        createNotificationChannel();
        new MyAsyncTask(this).execute();
        return false;
    }

    /*
    static class SmartInteger {

        private final int i;

        public SmartInteger(int i) {
            this.i = i;
        }

        public SmartInteger increment() {
            int x= this.i;
            x++;
            SmartInteger n = new SmartInteger(x);
            return n;
        }

        @Override
        public String toString() {
            return "SmartInteger{" +
                    "i=" + i +
                    '}';
        }
    }

    public void doStuff(int x, SmartInteger i, String s) {
        x = 5;
        i.increment();
        s += " suffix";
    }
    class asd {
        asd(String i){

        }
    }*/

    public void createNotificationChannel() {
/*
        int ox = 1;
        SmartInteger si = new SmartInteger(2);
        String ss = "Evelin";

        doStuff(ox, si, ss);
*/
        System.out.println();

        // Define notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,"Job Service notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifications from Job Service");

            mNotifyManager.createNotificationChannel(notificationChannel);

        }
    }

    class MyAsyncTask extends AsyncTask<Void,Integer,Void> {//params(doinbckgrnd), progress(update), result(postexec)

        NotificationJobService notificationJobService;
        MyAsyncTask(NotificationJobService notificationJobService){
            this.notificationJobService= notificationJobService;
        }


        @Override
        protected Void doInBackground(Void... voids) {//nem lehet frissiteni a UI-t

            PendingIntent contentPendingIntent =PendingIntent.getActivity(
                    notificationJobService,NOTIFICATION_ID,new Intent(notificationJobService,MainActivity.class)
                    ,PendingIntent.FLAG_UPDATE_CURRENT);
            //construct and deliver the notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(notificationJobService,PRIMARY_CHANNEL_ID)
                    .setContentTitle("Screamer")
                    .setContentText("Running...")
                    .setContentIntent(contentPendingIntent)
                    .setSmallIcon(R.drawable.ic_dots)
                    .setDeleteIntent(getDeleteIntent())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);
            mNotifyManager.notify(NOTIFICATION_ID,builder.build());
            return null;

        }

    }
    protected PendingIntent getDeleteIntent () {
        Intent intent = new Intent(NotificationJobService.this, NotificationBroadcastReceiver.class ) ;
        intent.setAction( "notification_cancelled" ) ;
        return PendingIntent. getBroadcast (NotificationJobService.this, 0 , intent , PendingIntent. FLAG_CANCEL_CURRENT ) ;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("TAG","onStopJob");
        return false;
    }

}

