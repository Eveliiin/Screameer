package com.example.screameer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private static final int JOB_ID = 0;
    private JobScheduler mScheduler;
    private Button serviceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serviceButton = findViewById(R.id.serviceButton);

        if(isMyServiceRunning(ScreamService.class)){
                serviceButton.setText("STOP");
        }else{serviceButton.setText("START");}

        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        if(message!=null){
            if(message.equals("Notification closed")){
                serviceButton.setText("START");
            }
        }

        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(serviceButton.getText().toString().equals("START")){
                     //bindService(new Intent(MainActivity.this, ScreamService.class), sConn, Context.BIND_AUTO_CREATE);
                    Intent intent =new Intent(MainActivity.this,ScreamService.class);
                    intent.putExtra("Command","START");
                    startService(intent);

                    makeNotification();
                    serviceButton.setText("STOP");
                }else {
                    Intent intent =new Intent(MainActivity.this,ScreamService.class);
                    intent.putExtra("Command","STOP");
                    startService(intent);
                    //unbindService(sConn);
                    cancelNotification();
                    serviceButton.setText("START");
                }
            }
        });
    }
    public void makeNotification() {

        mScheduler= (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        ComponentName serviceName = new ComponentName(getPackageName(),NotificationJobService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,serviceName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder .setRequiresBatteryNotLow(true);
        }
        builder.setRequiresDeviceIdle(false);
        //Schedule the job and notify the user
        JobInfo myJobInfo =builder.build();
        mScheduler.schedule(myJobInfo);

    }
    public void cancelNotification(){
        NotificationManager mNotifyManager;
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyManager.cancel(1);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
