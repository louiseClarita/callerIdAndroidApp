package com.example.calleridapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.CallerIdApplication.R;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CallLogsFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CallLogsFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
DataBaseHelper2 dt2;
DataBaseHelper dt;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<CallLogs> calllogsList;
   ListView logs_listview;
    public CallLogsFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *    //@param param1 Parameter 1.
     *     //@param param2 Parameter 2.
     *    //@return A new instance of fragment CallLogsFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static CallLogsFrag newInstance() {
        CallLogsFrag fragment = new CallLogsFrag();
        Bundle args = new Bundle();
      //  args.putString(ARG_PARAM1, param1);
       // args.putString(ARG_PARAM2, param2);
     //   fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_call_logs, container, false);
        fetchLogs();
        dt2 =  new DataBaseHelper2(getActivity().getApplicationContext());
        dt = new DataBaseHelper(getActivity().getApplicationContext());
      //  fetchLogs();
        logs_listview = v.findViewById(R.id.logs_listview);
        calllogsList = dt2.getEveryone();
        ArrayAdapter adapter = new CallLogsAdapter(getActivity(), R.layout.calllogsadapter_layout,calllogsList);
        logs_listview.setAdapter(adapter);


        return v;
    }



    public void fetchLogs(){


        ContentResolver cr = getActivity().getContentResolver();
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
                        Toast.makeText(getActivity(),stringType,Toast.LENGTH_LONG).show();
                        switch (stringType) {
                            case "2":
                                direction = "OUTGOING";
                                directionBoolean = true;
                                Log.d(String.valueOf(getActivity()),"durection boolean = true ->"+directionBoolean);
                                Log.d(String.valueOf(getActivity()),"durection value = outgoing ->"+direction);
                                System.out.println("durection boolean = true ->"+directionBoolean);
                                break;
                            case "1":
                                direction = "INCOMING";
                                System.out.println("durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(getActivity()),"durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(getActivity()),"durection value = INCOMING ->"+direction);
                                break;

                            case "3":
                                direction = "MISSED";
                                directionBoolean = false;
                                Log.d(String.valueOf(getActivity()),"durection value = INCOMING ->"+direction);
                                Log.d(String.valueOf(getActivity()),"durection boolean = false ->"+directionBoolean);
                                break;

                            default:
                                direction = "DEFAULT";
                                directionBoolean = false;
                                Log.d(String.valueOf(getActivity()),"durection value = INCOMING DEFAULT ->"+direction);
                                Log.d(String.valueOf(getActivity()),"durection boolean = false DEFAULT->"+directionBoolean);
                                break;
                        }
                    }catch(Exception e){
                        Log.d("direction",e.toString());

                    }
                 //   @SuppressLint("Range") int dircode = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));


                   /* switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            direction = "OUTGOING";
                            directionBoolean = true;
                            Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = true ->"+directionBoolean);
                            System.out.println("durection boolean = true ->"+directionBoolean);
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            direction = "INCOMING";
                            directionBoolean = false;
                            System.out.println("durection boolean = false ->"+directionBoolean);
                            Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            direction = "MISSED";
                            directionBoolean = false;

                            Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                            break;
                        default:
                            break;
                    }*/
String contactid="N/A";

    contactid = dt.getContactIdByPhone(phNumber);

                    CallLogs cl =new CallLogs(callDuration,String.valueOf(directionBoolean),dateString,phNumber,"false",contactid);
                         dt2.addOne(cl);
                         Toast.makeText(getActivity(),cl.toString(),Toast.LENGTH_LONG).show();
                }
            }
            c.close();
        }
    }
}