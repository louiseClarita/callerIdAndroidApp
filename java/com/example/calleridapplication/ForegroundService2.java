package com.example.calleridapplication;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.CallerIdApplication.R;

public class ForegroundService2 extends Service {
    public ForegroundService2(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        // create an instance of Window class
        // and display the content on screen
        Window2 window2=new Window2(this);
        window2.open();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    // for android version >=O we need to create
    // custom notification stating
    // foreground service is running
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground() {

        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName ="Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID,channelName, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        assert manager != null;
        manager.createNotificationChannel(chan);


        NotificationCompat.Builder notificaBuilder = new NotificationCompat.Builder(ForegroundService2.this,NOTIFICATION_CHANNEL_ID);
        Notification notification = notificaBuilder.setOngoing(true)
                .setContentTitle("Caller id")
                .setContentText("saving logs")
                // this is important, otherwise the notification will show the way
                // you want i.e. it will show some default notification
                .setSmallIcon(R.mipmap.ic_javista_logo_foreground)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(3,notification);

    }
}
