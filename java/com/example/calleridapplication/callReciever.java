package com.example.calleridapplication;

import android.animation.Keyframe;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.CallerIdApplication.R;
import com.google.android.material.navigation.NavigationView;

public class callReciever extends BroadcastReceiver {
    int count=0;
    public static final String CUSTOM_INTENT = "jason.wei.custom.intent.action.TEST";
   static String number="";
  public Context ctx;
  int opened =0;
  public static String numbertofetch;
  public static String numbertocreate="bonjour";
  public static Boolean openedOnNotFound = false;
  public static boolean openCreate=false;
  // Dialog dialog;
    DataBaseHelper dt = null;
    DataBaseHelper2 dt2 = null;
    @Override
    public void onReceive(Context context, Intent intent) {
         ctx = context;
         dt = new DataBaseHelper(context);
        dt2 = new DataBaseHelper2(context);
        if (intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            //if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            //Log.d(TAG, "Inside Extra state off hook");
            // String number = intent.getStringExtra(TelephonyManager.EXTRA_PHONE_NUMBER);
            // Log.e(TAG, "outgoing number : " + number);
            // }



//  running          if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                //Log.e(TAG, "Inside EXTRA_STATE_RINGING");
//                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                System.out.println("The Caller Number is: " + number);
//                showToast(context, "The Caller Number is: " + number);
//                //Log.e(TAG, "incoming number : " + number);
//            }


            // else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            // Log.d(TAG, "Inside EXTRA_STATE_IDLE");
            //}

            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //   String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                //   System.out.println("The Caller Number is offhook: " + number);
                //   showToast(context, "Call started... The Number is: " + number);
                //showToast(context, "Call started...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {


                if(!number.isEmpty() && number != null){
                    numbertofetch = number;
                }
Log.d("logs","we will be adding the logs to database later");
             //   addLogToDB2(context);
                Log.d("steps","call ended");
                if(Window.found || Window4Api.found){
                    Log.d("steps","ctct found");
                    openpopUpService( context,number);

                }else{
                    Log.d("steps","cn  tact not found");
                         numbertocreate = number;
                     openAppTocreate(context,number);

                }
                try{
                 //   startService2();
                    opened++;
                }catch(Exception e){
                    Log.e("start service error",e.toString());
                    showToast(context,e.toString());
                    showToast(context,"error opening second one");
                }

                        getLogs();
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

       if(!number.isEmpty() && number != null){
           numbertofetch = number;
           System.out.println("The Caller Number is Ringing:  " + number);
           showToast(context, "Incoming call... Number is: " + number);
              //  openBottomFragment(context,number);

         try{
           startService();
           opened++;
         }catch(Exception e){
             Log.e("start service error",e.toString());
             showToast(context,e.toString());
         }
         }
    }
                // showToast(context, "Incoming call...");
            }


        }

    private void addLogToDB2(Context ctx) {


        ContentResolver cr = ctx.getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {
                    Boolean directionBoolean = true;
                    String  phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    String  callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    Date dateFormat= new Date(Long.valueOf(callDate));
                    String callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));
                    String direction;
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                   String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));
                    String stringType;
                    try{
                        stringType = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                        Toast.makeText(ctx,stringType,Toast.LENGTH_LONG).show();
                        switch (stringType) {
                            case "2":
                                direction = "OUTGOING";
                                directionBoolean = true;
                                Log.d(String.valueOf(ctx),"durection boolean = true ->"+directionBoolean);
                                Log.d(String.valueOf(ctx),"durection value = outgoing ->"+direction);
                                System.out.println("durection boolean = true ->"+directionBoolean);
                                break;
                            case "1":
                                direction = "INCOMING";
                                System.out.println("durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(ctx),"durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(ctx),"durection value = INCOMING ->"+direction);
                                break;

                            case "3":
                                direction = "MISSED";
                                directionBoolean = false;
                                Log.d(String.valueOf(ctx),"durection value = INCOMING ->"+direction);
                                Log.d(String.valueOf(ctx),"durection boolean = false ->"+directionBoolean);
                                break;

                            default:
                                direction = "DEFAULT";
                                directionBoolean = false;
                                Log.d(String.valueOf(ctx),"durection value = INCOMING DEFAULT ->"+direction);
                                Log.d(String.valueOf(ctx),"durection boolean = false DEFAULT->"+directionBoolean);
                                break;
                        }
                    }catch(Exception e){
                        Log.d("direction",e.toString());

                    }
                    @SuppressLint("Range") int dircode = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));


                    String contactid="N/A";

                    contactid = dt.getContactIdByPhone(phNumber);
                        Log.d("dateString",dateString);
                    CallLogs cl =new CallLogs(callDuration,String.valueOf(directionBoolean),dateString,phNumber,"false",contactid);
                    if(dt2.addOne(cl)){
                        showToast(ctx,"success");
                    }else{
                        showToast(ctx,"unsuccessful");
                    }
                }
            }
            c.close();
        }
    }




    private void openAppTocreate(Context context,String number) {

        Intent i = new Intent(context,first.class);
        i.putExtra("number",number);
     openedOnNotFound=true;
        Log.d("steps","opening...");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }




       /* TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                super.onCallStateChanged(state, incomingNumber);
                String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                // if(state1.equals(TelephonyManager.EXTRA_STATE_RINGING)){
               if(!incomingNumber.isEmpty() && !incomingNumber.equals("null")){
                number = incomingNumber;
                // loop(context,number);
                //if(count == 1){
                   showDialog(context,number);

                //    }

               }

                //  }
            }
        },PhoneStateListener.LISTEN_CALL_STATE);

*/




//if (intent.getAction().equals(callReciever.CUSTOM_INTENT)) {
        /*
        System.out.println("GOT THE INTENT");
        Log.d("intent found","found");
        final String mobileNumber = intent.getExtras().getString("number");
        Thread thread = new Thread(){
            private int sleepTime = 40;

            @Override
            public void run() {
                super.run();
                try {
                    int wait_Time = 0;

                    while (wait_Time < sleepTime ) {
                        sleep(40);
                        wait_Time += 10 ;
                    }
                }catch (Exception e) {
                    Toast.makeText(context,
                            "Error Occured Because:" + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
                finally {

                }

                context.startActivity(new Intent(context, Callerinfoactivity.class).putExtra("number", mobileNumber)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        };
        thread.run();
  //  }
}*/
/* }


     //   String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
      //  if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
     //       Log.e("onmessagereceived","dsfa");
      //      number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
      //      showDialog(context);
     //   }


        /*
        if(intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
             //Log.e(TAG, "Inside EXTRA_STATE_RINGING");
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is: " + number);
                showToast(context, "The Caller Number is: " + number);
              //  Intent i = new Intent(context,Callerinfoactivity.class);
              //  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //  i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                // i.putExtra("number",number);
                Intent intent1 = context.getPackageManager().getLaunchIntentForPackage( "com.example.calleridapplication");
                if (intent1 != null) {
                    // We found the activity now start the activity
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }*/

              //  context.startActivity(i);
            /*  new Timer().schedule(new TimerTask() {
                  @Override
                  public void run() {
                      if(!check_if_number_isEmpty(number)){
                          Intent i = new Intent(context,Callerinfoactivity.class);
                          i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                          //    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                          if(context== null){  showToast(context,"context is null"); }
                         // i.putExtra("number",number);

                          context.startActivity(i);
                          return;
                      }

                  }
              },1);*/
              //Log.e(TAG, "incoming number : " + number);



//            }

     /*   telephony.listen(new PhoneStateListener(){
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                String state1 = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
                count++;
                showToast( context,"onRecieve called");
                    //if(count == 1){
                showToast( context,"lezim teftah anyminute now");
                  number = incomingNumber;

                  Intent i = new Intent(context,Callerinfoactivity.class);
                  i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              //    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                  if(context== null){  showToast(context,"context is null"); }
                  i.putExtra("number",number);

                  context.startActivity(i);

               //    }


          }
        },PhoneStateListener.LISTEN_CALL_STATE);

/*


        if(intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            //if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            //Log.d(TAG, "Inside Extra state off hook");
            // String number = intent.getStringExtra(TelephonyManager.EXTRA_PHONE_NUMBER);
            // Log.e(TAG, "outgoing number : " + number);
            // }



//  running          if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
//                //Log.e(TAG, "Inside EXTRA_STATE_RINGING");
//                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//                System.out.println("The Caller Number is: " + number);
//                showToast(context, "The Caller Number is: " + number);
//                //Log.e(TAG, "incoming number : " + number);
//            }


            // else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            // Log.d(TAG, "Inside EXTRA_STATE_IDLE");
            //}


            if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
             //   String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
             //   System.out.println("The Caller Number is offhook: " + number);
             //   showToast(context, "Call started... The Number is: " + number);
                //showToast(context, "Call started...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
              //  String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is idle: " + number);

                Intent intent1 = new Intent(context, Callerinfoactivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("number",number);
                context.startActivity(intent1);

                showToast(context, "Call ended... Number is: " + number);
                // showToast(context, "Call ended...");

            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                System.out.println("The Caller Number is Ringing:  " + number);
                showToast(context, "Incoming call... Number is: " + number);
                // showToast(context, "Incoming call...");
            }
        }*/


  /* public void showDialog(Context context,String nbre){

       Log.d("showDialog","outside if count is"+count );
       if(count!= 0){
       Log.d("showDialog","inside if block count is"+count );
       Intent i = new Intent(context,Callerinfoactivity.class);
       i.putExtra("number",nbre);
       i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
           i.setAction(CUSTOM_INTENT);
           context.sendBroadcast(i);
    //   context.startForegroundService(i);
       //    context.startActivity(i);
       }
       count++;
   }*/
public void loop(Context ctx,String number){
    while(number.isEmpty()){
        Log.d("looping","looping");
        loop(ctx,number);
        Log.d("looping","endded");
    }
    String savedNumber = number;
    //showDialog(ctx,savedNumber);

    return;
}


    private void openBottomFragment(Context context,String number) {


      /*  Intent i = new Intent(first.this,Callerinfoactivity.class);
        this.startActivity(i);*/

Intent i = new Intent(context,CallingPage.class);
i.putExtra("number",number);
i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(i);
    }

    private void openpopUpService(Context context,String number) {


      /*  Intent i = new Intent(first.this,Callerinfoactivity.class);
        this.startActivity(i);*/

       Intent i = new Intent(context,SaveLogsPage.class);
        i.putExtra("number",number);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

public boolean check_if_number_isEmpty(String nbre){
        if(nbre.equals("null")){return true;}
        return false;
}
    void showToast(Context context,String message){
        Toast toast=Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
    public void startService(){
    if(ctx == null){
        Log.e("error","ctx null");
        return;
    }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(ctx)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.startForegroundService(new Intent(ctx, ForegroundService.class));
                } else {

                 //   ctx.startService(new Intent(ctx, ForegroundService.class));
                    Intent i = new Intent(ctx,Window4Api.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(i);
                }
            }
        }else{
            Intent i = new Intent(ctx,Window4Api.class);
            ctx.startActivity(i);
         //  ctx.startService(new Intent(ctx, ForegroundService.class));
        }
    }


    public void getLogs(){


            ContentResolver cr = ctx.getContentResolver();
            Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

            int totalCall = 1;

            if (c != null) {
                totalCall = 1; // intenger call log limit

                if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                    for (int j = 0; j < totalCall; j++) {


                        String phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                        String callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                        String callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                        Date dateFormat= new Date(Long.valueOf(callDate));
                        String callDayTimes = String.valueOf(dateFormat);

                        String direction = null;
                        switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)))) {
                            case CallLog.Calls.OUTGOING_TYPE:
                                direction = "OUTGOING";
                                break;
                            case CallLog.Calls.INCOMING_TYPE:
                                direction = "INCOMING";
                                break;
                            case CallLog.Calls.MISSED_TYPE:
                                direction = "MISSED";
                                break;
                            default:
                                break;
                       }

                      //  c.moveToPrevious(); // if you used moveToFirst() for first logs, you should this line to moveToNext

                        Toast.makeText(ctx, phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_SHORT).show(); // you can use strings in this line

                    }
                }
                c.close();
            }
        }




    public void startService2(){
        if(ctx == null){
            Log.e("error","ctx null");
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(ctx)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.startForegroundService(new Intent(ctx, ForegroundService2.class));
                } else {
                    ctx.startService(new Intent(ctx, ForegroundService2.class));
                }
            }
        }else{
            ctx.startService(new Intent(ctx, ForegroundService2.class));
        }
    }

}


