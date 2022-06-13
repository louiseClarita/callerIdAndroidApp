package com.example.calleridapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.CallLog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.Contact;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;

import static android.content.Context.WINDOW_SERVICE;

import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Window2 {

    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;

    EditText ETdescription;
    String desc;
    String phNumber;
    String callDate;
    String callDuration;
    Date dateFormat;
    String callDayTimes;
    String number = Window.numberToFetch;
    String owner_id;
    String duration,time,date;
    DataBaseHelper dataBaseHelper;
    CardView logsCardView;
    public Window2(Context context){
        this.context = context;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            mParams = new WindowManager.LayoutParams(
                    // Shrink the window to wrap the content rather
                    // than filling the screen
                    WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                    // Display it on top of other application windows
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    // Don't let it grab the input focus
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    // Make the underlying application window visible
                    // through any transparent parts
                    PixelFormat.TRANSLUCENT);
        }
        //getting layout inflater
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.savelog_layout,null);
        /*
        dataBaseHelper = new DataBaseHelper(context);
        // set onClickListener on the remove button, which removes
        // the view from the window
        mView.findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        logsCardView = mView.findViewById(R.id.logscardView);
        ETdescription = mView.findViewById(R.id.description);

        mView.findViewById(R.id.savephonecall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                desc = ETdescription.getText().toString();
                fetchLogs();
                saveLogs(desc,duration,phNumber,date);
            }
        });

*/

        mParams.gravity = Gravity.CENTER;
        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);




    }

    public void open(){


        try{
            //checking if the view is already inflated or present in the window
           // if(mView.getWindowToken() == null){
            //    if(mView.getParent() == null){
                    mWindowManager.addView(mView,mParams);
             //   }
         //   }

        }catch(Exception e){
            Log.d("Error openning ",e.toString());
        }


    }

    private void saveLogs(String desc, String duration, String phNumber, String date) {
        logsCardView.setEnabled(false);
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"desc\": \" heythere \"\r\n}");

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://calleridcrmapi.azure-api.net/phonecalls")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("okhttp1",e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {

                            Log.e("okhttp2",response.toString());


                            close();

                            Toast.makeText(context,"successfullt saved",Toast.LENGTH_LONG).show();
                            throw new IOException("Unexpected code " + response);
                        }else{
                            //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());
                            close();
                            Toast.makeText(context,"successfullt saved",Toast.LENGTH_LONG).show();
                            Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }















    public void close(){
        try{
            // remove the view from the window
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            mView.invalidate();
            // remove all views
            ((ViewGroup)mView.getParent()).removeAllViews();

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions

        }catch (Exception e) {
            Log.d("Error2",e.toString());
        }
    }
    public void fetchLogs(){


        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {


                     phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);

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

                    Toast.makeText(context, phNumber + callDuration + callDayTimes + direction, Toast.LENGTH_SHORT).show(); // you can use strings in this line

                }
            }
            c.close();
        }
    }



}
