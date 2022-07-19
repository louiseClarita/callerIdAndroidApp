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

public class ForegroundService extends Service {
public ForegroundService(){

}
public static  Notification notification;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
       throw new UnsupportedOperationException("Not Yet Implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            startMyOwnForeground();
            System.out.println("entered foreground1");
        }
        else{
            startForeground(1, new Notification());
            System.out.println("entered foreground2");
        }
        // create an instance of Window class 
        // and display the content on screen
        Window window=new Window(this);
        System.out.println("entered foreground3");
        window.open();


        
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


        NotificationCompat.Builder notificaBuilder = new NotificationCompat.Builder(ForegroundService.this,NOTIFICATION_CHANNEL_ID);
         notification = notificaBuilder.setOngoing(true)
                .setContentTitle("Caller id")
                .setContentText("fetching Contacts")
        // this is important, otherwise the notification will show the way
        // you want i.e. it will show some default notification
                 .setSmallIcon(R.mipmap.ic_javista_logo)
                 .setPriority(NotificationManager.IMPORTANCE_LOW)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();


     startForeground(2,notification);

    }
}
