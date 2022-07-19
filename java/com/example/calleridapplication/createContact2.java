package com.example.calleridapplication;

import static com.example.CallerIdApplication.R.*;
import static com.example.CallerIdApplication.R.color.*;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.CallLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link createContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class createContact2 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String contactid = null;
    String URLline;
    CallLogs cl2=null;
    String ids;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    Button cancel;
    static Boolean openFromCreate = false;
    public static ContactModel contactfound = null;
    public String APIURL ="https://calleridcrmapi.azure-api.net/test/contacts/";
    EditText ETfirstname,ETlastname,ETcompany,ETjob,ETemail,ETphonenumber;
    String URL_GETALLCONTACTS = "https://getallcontacts20220530100450.azurewebsites.net/api/Function1?code=vQ39xN3XM4syuBk6ACNCDkUwpwAsS-EiiQi3Trc4028RAzFuOQbYKQ==";

    TextView createStatus;
    String firstname,lastname,company,job,email,mobilephone;
    Button btnCreateContact,gotoSaveLogs;
    ProgressBar progressbar;
    DataBaseHelper dataBaseHelper;
    DataBaseHelper2 dataBaseHelper2;
    DataBaseHelper3 dataBaseHelper3;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public createContact2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *  //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * //@return A new instance of fragment createContact.
     */
    // TODO: Rename and change types and number of parameters
    public static createContact2 newInstance(String ids) {
        createContact2 fragment = new createContact2();
          Bundle args = new Bundle();
           args.putString(ARG_PARAM1,ids);
           Log.d("idincreate2",ids);
        Log.d("idincreate2",ARG_PARAM1);
        //    args.putString(ARG_PARAM2, param2);
           fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if (getArguments() != null) {
           mParam1 = getArguments().getString(ARG_PARAM1);
           Log.d("mParams",mParam1);
        //   mParam2 = getArguments().getString(ARG_PARAM2);
           }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(layout.fragment_create_contact, container, false);
        //EditText ETfirstname,ETlastname,ETcompany,ETjob,ETemail,ETphonenumber;
        ETfirstname = view.findViewById(id.cr_firstname2);
        ETlastname = view.findViewById(id.cr_lastname2);
        ETcompany = view.findViewById(id.cr_company2);
        ETjob = view.findViewById(id.cr_job2);
        ETemail = view.findViewById(id.cr_email2);
        ETphonenumber= view.findViewById(id.cr_phonenbre2);
       // progressbar=view.findViewById(id.progressBar12);
        btnCreateContact = view.findViewById(id.createContact2);
        createStatus = view.findViewById(id.createStatus2);
        gotoSaveLogs = view.findViewById(id.gotoSaveLogs2);
        cancel = view.findViewById(id.button3);
        createStatus.setText("");
        ETfirstname.setText("");
        ETlastname.setText("");
        ETcompany.setText("");
        ETjob.setText("");
        ETemail.setText("");
        ETphonenumber.setText("");
        ETphonenumber.setEnabled(true);
        dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        dataBaseHelper2 = new DataBaseHelper2(getActivity().getApplicationContext());
        dataBaseHelper3 = new DataBaseHelper3(getActivity().getApplicationContext());
        if(!(dataBaseHelper3.getCount() == 0)){

            //showProgressBar();
            TextView userName = first.mHeaderView.findViewById(R.id.userName);
            TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);
            userName.setText(dataBaseHelper3.getUser().getName());
            userEmail.setText(dataBaseHelper3.getUser().getEmail());
            // hideProgressBar();
        }
        gotoSaveLogs.setVisibility(View.GONE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                                Intent i = new Intent(getActivity(),first.class);
                                getActivity().startActivity(i);

                    }
                });

            }
        });
        Log.d("ids",mParam1);

        cl2=dataBaseHelper2.getLoginfo(mParam1);
        Log.d("cl2:",cl2.toString());
        ETphonenumber.setText(cl2.getPhoneNbre());
        ETphonenumber.setEnabled(false);
        ETphonenumber.setClickable(false);
        btnCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSingleAccountApp==null){
                    PublicClientApplication.createSingleAccountPublicClientApplication(getActivity(), R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
                        @Override
                        public void onCreated(ISingleAccountPublicClientApplication application){

                            if(getActivity() == null) Log.e("EMT","EMT");

                            mSingleAccountApp = application;

                            if(mSingleAccountApp!=null){
                                Log.d("Tag","entereed againnn");
                                loadAccount();


                            }

                        }
                        @Override
                        public void onError(MsalException exception){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressBar();
                                    Log.d("TAG",exception.toString());


                                    Toast.makeText(getActivity(),"error!please try again later!",Toast.LENGTH_LONG).show();
                                    return;
                                }
                            });

                            //  displayError(exception);

                        }
                    });
                }

         if(dataBaseHelper3.getCount()!=1){
             Toast.makeText(getActivity(),"you need to sign in to perform this action.",Toast.LENGTH_LONG).show();
               return;
          }
                firstname=ETfirstname.getText().toString().trim();
                lastname=ETlastname.getText().toString().trim();
                company=ETcompany.getText().toString().trim();
                job=ETjob.getText().toString().trim();
                email=ETemail.getText().toString().trim();
                mobilephone=ETphonenumber.getText().toString().trim();
                if(!email.isEmpty()) {
                //    String email_regex = "[A-Z]+[a-zA-Z_]+@\b([a-zA-Z]+.){2}\b?.[a-zA-Z]+";
                    String email_regex ="^[A-Za-z0-9+_.-]+@(.+)$";
                    if(!email.matches(email_regex)) {
                        createStatus.setVisibility(View.VISIBLE);
                        createStatus.setTextColor(createContact2.this.getResources().getColor(R.color.red));
                        createStatus.setText("bad email format!");
                        return;
                    }
                }
                if(firstname.isEmpty() || lastname.isEmpty() || mobilephone.isEmpty()){

                    Toast.makeText(getActivity(),"make sure that all required fields are there!",Toast.LENGTH_LONG).show();
                    return;
                }
                showProgressBar();
                createStatus.setText("");
                //this one is using volley
                //   createContactinCRM1(firstname,lastname,company,job,email,mobilephone);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    /*    for(int i=0;i<1000;i++){
                             createContactinCRM3("firstname","lastname","company","job","email","mobilephone");
                        }*/
                        createContactinCRM2(firstname,lastname,company,job,email,mobilephone);
                    }
                });




            }
        });

        gotoSaveLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
                gotosaveLogsPage();
            }
        });




        return view;
    }

    private void gotosaveLogsPage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openFromCreate=true;
                Intent i = new Intent(getActivity(),SaveLogsPage.class);
                i.putExtra("id",mParam1);
                contactid = contactfound.getContact_id();
                Log.d("ids",contactfound.getContact_id());
                getActivity().startActivity(i);
                getActivity().finish();
            }
        });

    }

    private void createContactinCRM3(String firstname, String lastname, String company, String job, String email, String mobilephone) {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\r\n    \"firstname\": \""+firstname+"\"\r\n," +
                        "\r\n    \"lastname\": \""+lastname+"\"\r\n," +
                        "\r\n    \"cr051_companyname\": \""+company+"\"\r\n," +
                        "\r\n    \"emailaddress1\": \""+email.trim()+"\"\r\n," +
                        "\r\n    \"jobtitle\": \""+job+"\"\r\n," +
                        "\r\n    \"mobilephone\": \""+mobilephone.trim()+"    \"\r\n}");

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://calleridcrmapi.azure-api.net/contacts")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        hideProgressBar();
                        Log.e("okhttp1",e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {

                            Log.e("okhttp2",response.toString());


                            throw new IOException("Unexpected code " + response);
                        }else{
                            //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                }
                            });
                            Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }
        });



    }









    private void createContactinCRM2(String firstname, String lastname, String company, String job, String email, String mobilephone) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{" +
                        "\r\n\"firstname\": \""+firstname.toUpperCase().trim().charAt(0)+""+firstname.substring(1).toLowerCase().trim()+"\"\r\n," +
                        "\r\n \"lastname\": \""+lastname.toUpperCase().trim()+"\"\r\n," +
                        "\r\n    \"cr051_companyname\": \""+company.trim()+"\"\r\n," +
                        "\r\n    \"emailaddress1\": \""+email.trim()+"\"\r\n," +
                        "\r\n    \"jobtitle\": \""+job.trim()+"\"\r\n," +
                        "\r\n    \"mobilephone\": \""+mobilephone.trim()+"\"\r\n}");

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://calleridcrmapi.azure-api.net/contacts")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                      getActivity().runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              hideProgressBar();
                              Toast.makeText(getActivity(),"error creating this contact, please try again later!",Toast.LENGTH_LONG).show();
                              Log.e("okhttp1",e.toString());
                              e.printStackTrace();
                          }
                      });

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {

                            Log.e("okhttp2",response.toString());

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressBar();
                                    Toast.makeText(getActivity(),"error creating this contact, please try again later!",Toast.LENGTH_LONG).show();
                                  //  progressbar.setVisibility(View.INVISIBLE);
                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    createStatus.setTextColor(Integer.parseInt("#eb4b4b"));
                                    createStatus.setText("unsuccessfull!");

                                }
                            });

                            throw new IOException("Unexpected code " + response);
                        }else{
                            //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cancel.setVisibility(View.GONE);
                                 //   progressbar.setVisibility(View.INVISIBLE);
                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    createStatus.setTextColor(createContact2.this.getResources().getColor(R.color.green));
                                    createStatus.setText("added success!");
                                    btnCreateContact.setText("saved!");
                                    hideProgressBar();
                                    Log.d("mobilephone1",mobilephone.trim());
                                    addthisContactToDB(mobilephone);

                                }
                            });

                            Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }
        });



    }

    private void addthisContactToDB(String mobilephone11) {
        mobilephone11 = mobilephone11.replace("+","%2B").trim();


        Toast.makeText(getActivity(), "adding contact to database....", Toast.LENGTH_LONG).show();
        //  URLline = "https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email="+mobilephone+"";
        //    URLline ="https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email=70753661";
        URLline= "https://calleridcrmapi.azure-api.net/contacts?$filter=(mobilephone eq '"+mobilephone11+"')";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLline,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("contactincrm",">>"+response);
                        getActivity().runOnUiThread( new Runnable() {
                            @Override
                            public void run() {

                                parsejsonContact(response);
                            }
                        });


                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        hideProgressBar();
                        Log.d("contactincrm",">>"+error.toString());
                     //   Toast.makeText(getActivity(),"error creating contact ,"+error.toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);

           }

    private void parsejsonContact(String response) {

        //jsonResponse.setText(response);
        Log.d("json",response);
        try{    JSONObject jsonObject = new JSONObject(response);
            //   if(jsonObject.getString("status").equals("true")){
            JSONArray callerid = jsonObject.getJSONArray("value");
            //for (int i = 0; i < callerid.length(); i++) {
            //    String name,JobTitle,Company,etag,contactid;
            JSONObject dataobj = callerid.getJSONObject(0);
            String name =dataobj.getString("firstname");
            String lname = dataobj.getString("lastname");
            String JobTitle=dataobj.getString("jobtitle");
            String Company = dataobj.getString("cr051_companyname");
            String contactid = dataobj.getString("contactid");
            //    etag = dataobj.getString("@odata.etag");
            email = dataobj.getString("emailaddress1");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    contactfound = new ContactModel(contactid,name,lname,Company,JobTitle,email,mobilephone);
                    if(dataBaseHelper.addOne(contactfound)) {
                        dataBaseHelper2.modifyContactid(cl2.getDate(),contactfound);
                        Log.d("should be modified","mpdified");
                        Log.d("cl2",cl2.toString());
                        callReciever.openedOnNotFound = false;
                        gotoSaveLogs.setEnabled(true);
                        gotoSaveLogs.setVisibility(View.VISIBLE);

                        Log.d("page","should go back");
                    }
                    else {
                        Log.d("EERRROOOR","added to DATABASE");
                    }



                  //  fetchLogs(contactfound); he hatjblna ekher whde bas thatswhy ha aamela abel
                }
            });





        }catch(JSONException e){
            e.printStackTrace();
            Log.e("TAG",e.getMessage().toString());
        }

    }
    public void fetchLogs(ContactModel cntct){


        ContentResolver cr = getActivity().getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {
                    String direction;
                    Boolean directionBoolean = true;
                    String phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    String callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    Date dateFormat= new Date(Long.valueOf(callDate));
                    String callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

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
                    @SuppressLint("Range") int dircode = Integer.parseInt(c.getString(c.getColumnIndex(CallLog.Calls.TYPE)));


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
                    // cl2 = dataBaseHelper2.fetchByDate(dateString);
                    Log.d("modifying","....");
                    Log.d("modifying ",cntct.toString());
                    if(cntct == null){

                    }
                    //    dataBaseHelper2.modifyContactid(dateString,cntct);
                    Log.d("modifying","linking the referenced call log into a contact created ");
                }
            }
            c.close();
        }
    }


    private void createContactinCRM1(String firstname, String lastname, String company, String job, String email, String mobilephone) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                RequestQueue queue = Volley.newRequestQueue(getActivity());

                // on below line we are calling a string
                // request method to post the data to our API
                // in this we are calling a post method.
                StringRequest request = new StringRequest(Request.Method.POST, APIURL, new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // inside on response method we are
                        // hiding our progress bar
                        // and setting data to edit text as empty
                        ETfirstname.setText("");
                        ETlastname.setText("");
                        ETcompany.setText("");
                        ETjob.setText("");
                        ETemail.setText("");
                        ETphonenumber.setText("");
                        // on below line we are displaying a success toast message.
                        Toast.makeText(getContext(), "Data added to API", Toast.LENGTH_SHORT).show();
                        // on below line we are parsing the response
                        // to json object to extract data from it.

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressBar();
                        // method to handle errors.
                        Toast.makeText(getContext(), "Fail to get response = " + error, Toast.LENGTH_LONG).show();
                        Log.e("creating error",error.getStackTrace().toString());
                    }
                }) {

                    /**
                     * Passing some request headers
                     */
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        headers.put("OData-MaxVersion", "4.0");
                        headers.put("OData-Version", "4.0");
                        //  return headers;
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //super.getParams();
                        Map<String, String> params = new HashMap<>();

                        // on below line we are passing our key
                        // and value pair to our parameters.
                        params.put("firstname", firstname);
                        params.put("lastname", lastname);
                        params.put("cr051_companyname", company);
                        params.put("jobtitle", job);
                        params.put("emailaddress1", email);
                        params.put("mobilephone", mobilephone);
                        return params;

                        // at last we are
                        // returning our params.
                        //   return params;
                    }

                };
                // below line is to make
                // a json object request.
                queue.add(request);

            }
        });
    }


    private void fetchAllContacts() {



        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_GETALLCONTACTS,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr",">>"+response);
                        parseJSON(response);

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        hideProgressBar();
                        Toast.makeText(getActivity(),"error creating contact ,"+error.toString(),Toast.LENGTH_LONG).show();
                        //    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("errroor",">>"+error.toString());
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);

    }

    private void parseJSON(String response) {

        String firstname,lastname,contactid,jobTitle,company,email,mobilephone;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);

            //   if(jsonObject.getString("status").equals("true")){
            JSONArray callerid = jsonObject.getJSONArray("value");
            for (int i = 0; i < callerid.length(); i++) {
                //    String name,JobTitle,Company,etag,contactid;
                JSONObject dataobj = callerid.getJSONObject(i);
                firstname =dataobj.getString("firstname");
                lastname = dataobj.getString("lastname");
                jobTitle=dataobj.getString("jobtitle");
                company = dataobj.getString("cr051_companyname");
                contactid = dataobj.getString("contactid");
                //    etag = dataobj.getString("@odata.etag");
                email = dataobj.getString("emailaddress1");
                mobilephone = dataobj.getString("mobilephone").trim();
                ContactModel c = new ContactModel(contactid,firstname,lastname,company,jobTitle,email,mobilephone);
                if(dataBaseHelper.addOne(c)){
                    // Toast.makeText(,"added",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getContext(),"not added",Toast.LENGTH_LONG).show();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadAccount(){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                return;
            }
            //   showProgressBar();
            //       mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());



            //is_signedin=true;
            //   }
            //  openBrowserTabActivity();



            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showProgressBar();
                            dataBaseHelper3.deleteUser();
                            if(dataBaseHelper.deleteDB()==0){
                                Toast.makeText(getActivity(),"Account Changed!\nerror signing out,try again later",Toast.LENGTH_LONG).show();
                                return;
                            }

                            performOperationOnSignOut();
                        }
                    });
                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                        Toast.makeText(getActivity(),"error!try again later",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void performOperationOnSignOut() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        if (first.navigationView != null) {
                            Log.d("tTAG","should change the title");
                            Menu menu = first.navigationView.getMenu();
                            menu.findItem(R.id.nav_signin).setTitle("Sign in");
                            first.navigationView.setEnabled(true);
                            first.toolbar.setEnabled(true);
                            // graphData.setText("");
                            //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                            //  navigationView.setNavigationItemSelectedListener(getActivity());
                        }

                        performOperationOnSignOut();

                    }
                    @Override
                    public void onError(@NonNull MsalException exception){
                        hideProgressBar();

                        Toast.makeText(getActivity(),"error!try again later!",Toast.LENGTH_LONG).show();
                    }
                });

                if(dataBaseHelper3.deleteUser()==1) {
                    if(dataBaseHelper.deleteDB()==0){
                        Toast.makeText(getActivity(),"error signing out,try again later",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(getActivity(),"signed out",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),"error signing out",Toast.LENGTH_LONG).show();
                    return;
                }

                Menu menu = first.navigationView.getMenu();
                menu.findItem(R.id.nav_signin).setTitle("Sign in");
                menu.findItem(R.id.nav_updateDB).setVisible(false);
                Toast.makeText(getActivity(),"please sign in",Toast.LENGTH_LONG).show();

                final String signOutText = "Signed Out.";
                TextView userName = first.mHeaderView.findViewById(R.id.userName);
                TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);

                userName.setText("username");
                userEmail.setText("username@org.onmicrosoft.com");


                Toast.makeText(getActivity(), signOutText, Toast.LENGTH_SHORT)
                        .show();



            }
        });
    }



    private void showProgressBar() {

        getActivity().findViewById(R.id.progressBar12)
                .setVisibility(View.VISIBLE);
           getActivity().findViewById(id.framCreateContact2)
                 .setVisibility(View.GONE);

    }

    private void hideProgressBar() {
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {
        getActivity().findViewById(R.id.progressBar12)
                .setVisibility(View.GONE);
    }
});

    //        getActivity().findViewById(id.framCreateContact2)
    //            .setVisibility(View.VISIBLE);
    }

}


/*
* {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("firstname", firstname);
                params.put("lastname", lastname);
                params.put("cr051_companyname", company);
                params.put("jobtitle", job);
                params.put("emailaddress1", email);
                params.put("mobilephone", mobilephone);


                // at last we are
                // returning our params.
             //   return params;
            }
* */
