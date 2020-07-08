package com.example.screameer;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ScreamService extends IntentService implements SensorEventListener {

    String LOG_TAG="ScreamService";

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private MediaPlayer mp;

    public ScreamService() {

        super("Screamer");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor =event.sensor;
        String sensorName= event.sensor.getName();
        Log.d("RESULT",sensorName+": X: "+event.values[0]+"; Y: " + event.values[1] + "; Z: " + event.values[2] + ";");
        double accelValuesX ;
        double accelValuesY ;
        double accelValuesZ ;
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER ) {
            accelValuesX = event.values[0];
            accelValuesY = event.values[1];
            accelValuesZ = event.values[2];
            double rootSquare = Math.sqrt(Math.pow(accelValuesX, 2) + Math.pow(accelValuesY, 2) + Math.pow(accelValuesZ, 2));
            if (rootSquare < 2.0) {
                Toast.makeText(this, "Fall detected", Toast.LENGTH_SHORT).show();
                mp.start();
            }
        }

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager mNotifyManager;
        mNotifyManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyManager.cancel(1);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager != null) {
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.unregisterListener(this,mAccelerometer);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String command =intent.getStringExtra("Command");
        if(command.equals("START")){

        mp = MediaPlayer.create(this, R.raw.ouch_sound);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (mSensorManager != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        }else{
            if(command.equals("STOP")){
                NotificationManager mNotifyManager;
                mNotifyManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyManager.cancel(1);

                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                if (mSensorManager != null) {
                    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    mSensorManager.unregisterListener(this, mAccelerometer);
                }
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
                String command = intent.getStringExtra("Command");
            if ("STOP".equals(command) ) {
                Toast.makeText(ScreamService.this,"STOPPED",Toast.LENGTH_SHORT).show();
                NotificationManager mNotifyManager;
                mNotifyManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                mNotifyManager.cancel(1);

                mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                if (mSensorManager != null) {
                    mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    mSensorManager.unregisterListener(this, mAccelerometer);
                }
                        /*
                    case "START":
                        //if(!isMyServiceRunning(ScreamService.class)){
                        mp = MediaPlayer.create(this, R.raw.ouch_sound);
                        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                        if (mSensorManager != null) {
                            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        }
                        //}
                        break;*/
            } else {
                Log.d(LOG_TAG, "command unknown: " + command);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
