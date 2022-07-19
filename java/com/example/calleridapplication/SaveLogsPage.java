package com.example.calleridapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.CallerIdApplication.R;
import com.google.android.material.navigation.NavigationView;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SaveLogsPage extends AppCompatActivity {
    EditText ETdescription;
    String desc;
    String phNumber;
    String callDate;
    String callDuration;
    Date dateFormat;
    CallLogs cl2;
    CallLogs cl=null;
    String callDayTimes;
    String dateString;
    Boolean directionBoolean = true;
    String id;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    Button cancel;
    Button savephonecall;
    ContactModel contactFound;
    private final static String[] SCOPES = {"Files.Read","Mail.Read"};
    String useremail;
    // String secondPersonContactid = Window.contactid;
    String secondPersonContactid="858576c3-59db-ec11-bb3d-000d3a66d2a8";
    String number = Window.numberToFetch;
    TextView durationTxtView;
    String owner_id;
    String duration,time,date;
    DataBaseHelper dataBaseHelper;
    DataBaseHelper2 dataBaseHelper2;
    DataBaseHelper3 dataBaseHelper3;
    String datebeforeformat = "";
    TextView logsStatus,logstatus1,contact,phonrNumber,timeDate;
    EditText subject;
    LinearLayout logsCardView;
    String direction;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.savelog_layout);
        useremail = "";
        dataBaseHelper = new DataBaseHelper(SaveLogsPage.this);
        dataBaseHelper2 = new DataBaseHelper2(SaveLogsPage.this);
        dataBaseHelper3 = new DataBaseHelper3(SaveLogsPage.this);
        getAccount();
        if(Window.found){
            contactFound=Window.contactFound;
        }else if(createContact.openFromCreate){
            contactFound = createContact.contactfound;
        }else if(createContact2.openFromCreate){
            contactFound = createContact2.contactfound;
        }else if(Window4Api.found) {
            contactFound = Window4Api.contactFound;
        }else if(callReciever.open){
            callReciever.open=false;
            contactFound = callReciever.contactfound;
        }
        // set onClickListener on the remove button, which removes
        // the view from the window

        logsCardView = findViewById(R.id.logscardView);
        logstatus1 = findViewById(R.id.logsStatus1);
        ETdescription = findViewById(R.id.description);
        // contact=findViewById(R.id.contact);
        timeDate = findViewById(R.id.date);
        contact = findViewById(R.id.ctct);
        phonrNumber = findViewById(R.id.phNumber);
        durationTxtView =findViewById(R.id.duration);
        logsStatus = findViewById(R.id.logsStatus);
        subject = findViewById(R.id.subject);
        cancel = findViewById(R.id.cancel);
        savephonecall =findViewById(R.id.savephonecall);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // SaveLogsPage.this.finish();
                Intent i = new Intent(SaveLogsPage.this,first.class);
                SaveLogsPage.this.startActivity(i);
            }
        });
        //subject.setTextColor(Integer.parseInt("0x00FF00"));//green

        ETdescription.setText("");
        subject.setText("");
        // contact=findViewById(R.id.contact);
        timeDate.setText("");
        contact.setText("");
        phonrNumber.setText("");
        durationTxtView.setText("");
        logsStatus.setText("");
        subject.setText("");
        if(!CallLogsAdapter.openedfromfrag) {
            if(!contactFound.getContact_id().equals("")){
                if(!createContact2.openFromCreate) {
                    fetchLogs();
                }
            }

        }

        if(CallLogsAdapter.openedfromfrag || createContact2.openFromCreate ){

            Bundle bundle=getIntent().getExtras();
            id = bundle.getString("id").trim();
            Log.d("idsss",id);
            fetchCallLog(id);


        }

        //durationTxtView.setText(callDuration);
        //  contact.setText("Call with : \n"+Window.contactFound.getContact_fname()+" " +Window.contactFound.getContact_lname()+"\n "+Window.contactFound.getContact_job()+"@"+Window.contactFound.getContact_company());

        if(signin_fragment.is_signedin){
            // contact.setText( signin_fragment.Email.replaceAll("\"","").trim());

        }

        savephonecall.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(subject.getText().toString().isEmpty()){
                    logstatus1.setVisibility(View.VISIBLE);
                    logstatus1.setText("please enter a subject");

                    return;
                }
                logstatus1.setText("");
                showProgressBar();
if(subject.getText().toString().isEmpty()){
    logstatus1.setVisibility(View.VISIBLE);
    logstatus1.setText("please enter a subject");
    hideProgressBar();
    return;
}
                if(CallLogsAdapter.openedfromfrag){

                        //usemail = signin_fragment.Email.replaceAll("\"","").trim();

                        String sub = subject.getText().toString().trim();
                        desc = ETdescription.getText().toString().trim();
                        saveLogs(sub,desc,Integer.parseInt(cl.getDuration()),cl.getPhoneNbre(),cl.getDate(), Boolean.valueOf(cl.getDirection()));
                        //  dataBaseHelper2.modifySaved(cl.getDate());
                        CallLogsAdapter.openedfromfrag = false;
                        //  logsStatus.setTextColor(0xFFFF0000);
                        //     logsStatus.setText("you need to sign in before u save a log call!");

                }else{

                        //opening from call
                        // -> straight yaane contact found
                        //aw after creation of contact
                        if(createContact.openFromCreate ||createContact2.openFromCreate){
                        //    useremail = signin_fragment.Email.replaceAll("\"","").trim();
                            String subs =  subject.getText().toString().trim();
                            desc = ETdescription.getText().toString().trim();
                            subject.setText("");
                          //  ETdescription.setText("");
                            // contact=findViewById(R.id.contact);
                            String timedate=  timeDate.getText().toString();
                            // contact.getText().toString().trim();
                            String phone=  phonrNumber.getText().toString().trim();

                            String durations = durationTxtView.getText().toString().trim();
                            if(durations.equals("MISSED")) {
                                Log.d("timedate ",timedate);
                                //saveLogs(subs,desc,Integer.parseInt(durations),phone,timedate, Boolean.valueOf(direction));
                                saveLogs(subs,desc, 0,phone,timedate,directionBoolean);

                            }else {
                                Log.d("timedate ",timedate);
                                //saveLogs(subs,desc,Integer.parseInt(durations),phone,timedate, Boolean.valueOf(direction));
                                saveLogs(subs,desc, Integer.parseInt(durations),phone,timedate,directionBoolean);
                            }

                            createContact.openFromCreate=false;
                            createContact2.openFromCreate=false;

                        }else{
                           // useremail = signin_fragment.Email.replaceAll("\"","").trim();

                            desc = ETdescription.getText().toString().trim();

                            ETdescription.setText("");
                            // contact=findViewById(R.id.contact);
                            String timedate=  timeDate.getText().toString().trim();
                            // contact.getText().toString().trim();
                            String phone=  phonrNumber.getText().toString().trim();
                            String durations = durationTxtView.getText().toString().trim();
                            String subs =  subject.getText().toString().trim();
                            if(durations.equals("MISSED")) {

                                saveLogs(subs,desc,0,phone,timedate,directionBoolean);
                            }else {

                                saveLogs(subs,desc,Integer.parseInt(durations),phone,timedate,directionBoolean);
                            }

                        }

                    }
            }


        });




    }

    private void fetchCallLog(String id) {
        cl =dataBaseHelper2.getLoginfo(id);
        Log.d("cl->>",cl.toString());
        updateUI2(cl.getDuration(),cl.getPhoneNbre(),cl.getDate(), Boolean.valueOf(cl.getDirection()));
    }

    private void signIn() {

        //  signin_fragment.mSingleAccountApp.signIn(SaveLogsPage.this, null, SCOPES, getAuthInteractiveCallback());
    }
    private void loadAccount(){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                useremail = activeAccount.getUsername().trim().toString();

            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    showProgressBar();
                    dataBaseHelper3.deleteUser();
                    if(dataBaseHelper.deleteDB()==0){
                        hideProgressBar();
                    Toast.makeText(SaveLogsPage.this,"you need to sign in before you perform this action.",Toast.LENGTH_LONG).show();
               performOnSignOutAction();
                  return;
                    }



                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                hideProgressBar();
                Toast.makeText(SaveLogsPage.this,"Try again later!",Toast.LENGTH_LONG).show();

               // displayError(exception);
                return;
            }
        });
    }

    private void performOnSignOutAction() {
SaveLogsPage.this.runOnUiThread(new Runnable() {
    @Override
    public void run() {
        hideProgressBar();
        mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                if (first.navigationView != null) {
                    Log.d("tTAG","should change the title");
                    Menu menu = first.navigationView.getMenu();
                    menu.findItem(R.id.nav_signin).setTitle("Sign in");
                    first.navigationView.setEnabled(true);
                    first.toolbar.setEnabled(true);

                    //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                    //  navigationView.setNavigationItemSelectedListener(getActivity());
                }

            }
            @Override
            public void onError(@NonNull MsalException exception){
                hideProgressBar();

                displayError(exception);
            }
        });


        if(dataBaseHelper3.deleteUser()==1) {
            if(dataBaseHelper.deleteDB()==0){
                Toast.makeText(SaveLogsPage.this,"error signing out,try again later",Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(SaveLogsPage.this,"signed out",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(SaveLogsPage.this,"error signing out",Toast.LENGTH_LONG).show();
            return;
        }


    }
});

    }

    private void saveLogs(String sub,String desc, int duration, String phNumber, String dateFormat1,Boolean direction) {
        boolean result = false;
        logsCardView.setEnabled(false);
        String userContactid = null;
        Toast.makeText(SaveLogsPage.this,"direction"+direction,Toast.LENGTH_LONG).show();
        if(!phNumber.contains("+961") && !phNumber.contains("+") && !phNumber.equals("111") ){
            if(phNumber.trim().startsWith("0")){
                phNumber = phNumber.replace(String.valueOf(phNumber.charAt(0)),"");
            }
            String ninesixone="+961";
            phNumber= ninesixone.concat(phNumber);
            Log.d("concat",phNumber);
            //  phNumber=phNumber.replace("+961","");
        }
//if(createContact.openFromCreate ){
        //   createContact.openFromCreate = false;
        //    userContactid = createContact.contactid;
//}else if(createContact2.openFromCreate ){
        ///          createContact2.openFromCreate = false;
        //          userContactid = createContact2.contactid;
        //     }else{

        System.out.print("useremail" +useremail);
        if(useremail.equals("")){
            if(dataBaseHelper3.getCount()==1){
                useremail = dataBaseHelper3.getUser().getEmail();




                if(mSingleAccountApp==null){
                    PublicClientApplication.createSingleAccountPublicClientApplication(SaveLogsPage.this, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
                        @Override
                        public void onCreated(ISingleAccountPublicClientApplication application){

                            if(SaveLogsPage.this == null) Log.e("EMT","EMT");

                            mSingleAccountApp = application;

                            if(mSingleAccountApp!=null){
                                Log.d("Tag","entereed againnn");
                                loadAccount();


                            }

                        }
                        @Override
                        public void onError(MsalException exception){
                            hideProgressBar();
                            Toast.makeText(SaveLogsPage.this,"error saving phone call , try again later!",Toast.LENGTH_LONG).show();
                            Toast.makeText(SaveLogsPage.this,"you need to sign in before you can save a phone call",Toast.LENGTH_LONG).show();

                            finish();

                         //   Log.d("TAG",exception.toString());
                            displayError(exception);
                            return;
                        }
                    });
                }





            }else{


            Toast.makeText(SaveLogsPage.this,"you need to sign in before you can save a phone call",Toast.LENGTH_LONG).show();
            hideProgressBar();
            finish();
            return;
            }
        }
        userContactid = fetchContactid(useremail);
//}
        // String userContactid ="cdcfa450-cb0c-ea11-a813-000d3a1b1223";

        String clientContactid = fetchContactidByphone(phNumber.trim());
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body=null;
Log.d("contact_idBill",userContactid);
Log.d("contact_idUSER",clientContactid);
        if(direction)
        {
            //outgoing
            body = RequestBody.create(mediaType," {   \"subject\": \" "+sub+"\",\n" +
                    "    \"phonenumber\": \""+phNumber+"\",\n" +
                    "    \"description\": \""+desc+"\",\n" +
                    "    \"directioncode\": "+direction+", //Direction : 0-->False/Incomming, 1-->True/Outgoing,\n" +
                    "    \"scheduledstart\":\""+dateFormat1+"\",\n" +
                    "    \"actualdurationminutes\":\""+duration+"\",\n" +
                    "   \"regardingobjectid_contact@odata.bind\": \"/contacts("+userContactid+")\", //Regarding is a contact\n" +
                    "    \"phonecall_activity_parties\": [\n" +
                    "       {\n" +
                    "            \"partyid_contact@odata.bind\": \"/contacts("+userContactid+")\", // call started by a sustemuser\n" +
                    "            \"participationtypemask\" : 1 // From\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"partyid_contact@odata.bind\": \"/contacts("+clientContactid+")\", // call to by a contact\n" +
                    "            \"participationtypemask\": 2 // To\n" +
                    "        }\n" +
                    "    ]}");

        }else{

            body = RequestBody.create(mediaType, "{    \"subject\": \""+sub+"\",\r\n  " +
                    "  \"phonenumber\": \""+phNumber+"\",\r\n   " +
                    " \"description\": \""+desc+"\",\r\n " +
                    "  \"directioncode\": "+direction+", //Direction : 0-->False/Incomming, 1-->True/Outgoing,\r\n  " +
                    "  \"scheduledstart\":\""+dateFormat1+"\",\r\n    \"actualdurationminutes\":\"50\",\r\n  " +
                    "    \"actualdurationminutes\":\""+duration+"\",\n" +
                    " \"regardingobjectid_contact@odata.bind\": \"/contacts("+userContactid+")\", " +
                    "//Regarding is a contact\r\n   " +
                    " \"phonecall_activity_parties\": [\r\n       {\r\n     " +
                    "       \"partyid_contact@odata.bind\": \"/contacts("+clientContactid+")\", " +
                    "// call started by a sustemuser\r\n         " +
                    "   \"participationtypemask\" : 1 // From\r\n        },\r\n        {\r\n      " +
                    "      \"partyid_contact@odata.bind\": \"/contacts("+userContactid+")\"," +
                    "// call to by a contact\r\n       " +
                    "     \"participationtypemask\": 2 // To\r\n        }\r\n    ]\r\n    }");

        }
/*body =RequestBody.crfate(mediaType,"{    \"subject\": \" "+desc+"\",\n" +
        "    \"phonenumber\": \" "+phNumber+"\",\n" +
        "    \"description\": \"My description\",\n" +
        "    \"directioncode\": "+direction+", //Direction : 0-->False/Incomming, 1-->True/Outgoing,\n" +
        "    \"scheduledstart\":\""+dateFormat+"\",\n" +
        "    \"actualdurationminutes\":\"50\",\n" +
        "   \"regardingobjectid_contact@odata.bind\": \"/contacts(cdcfa450-cb0c-ea11-a813-000d3a1b1223)\", //Regarding is a contact\n" +
        "    \"phonecall_activity_parties\": [\n" +
        "       {\n" +
        "            \"partyid_contact@odata.bind\": \"/contacts(cdcfa450-cb0c-ea11-a813-000d3a1b1223)\", // call started by a sustemuser\n" +
        "            \"participationtypemask\" : 1 // From\n" +
        "        },\n" +
        "        {\n" +
        "            \"partyid_contact@odata.bind\": \"/contacts(9fd4a450-cb0c-ea11-a813-000d3a1b1223)\", // call to by a contact\n" +
        "            \"participationtypemask\": 2 // To\n" +
        "        }\n" +
        "    ]\n" +
        "    }");*/
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://calleridcrmapi.azure-api.net/phonecalls")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              SaveLogsPage.this.runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      hideProgressBar();
                      Toast.makeText(SaveLogsPage.this,"error saving this phone call, Try again later!",Toast.LENGTH_LONG).show();
                      Log.e("okhttp1",e.toString());
                      e.printStackTrace();
                  }
              });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    SaveLogsPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SaveLogsPage.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    hideProgressBar();
                                    Toast.makeText(SaveLogsPage.this,"error saving this phone call, Try again later!",Toast.LENGTH_LONG).show();

                                    Log.e("okhttp2",response.toString());
                                    logsStatus.setTextColor(0xFFFF0000);
                                    logsStatus.setText("unsuccessfull!");
                                }
                            });

                        }
                    });


                    //   Toast.makeText(SaveLogsPage.this,"successfullt saved",Toast.LENGTH_LONG).show();
                    throw new IOException("Unexpected code " + response);
                }else{
                    //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                    Log.e("success",response.toString());
                    //logsStatus.setText("successfully added to CRM!");
                    SaveLogsPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SaveLogsPage.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("dateFormatSave",dateFormat1);

                                    dataBaseHelper2 = new DataBaseHelper2(SaveLogsPage.this);
                                    //  cl2 = dataBaseHelper2.fetchByDate(dateFormat1);
                                    //    Log.d("fetch by date",cl2.toString());
                                    Log.d("sub",sub);
                                    dataBaseHelper2.modifySaved(dateFormat1);
                                    dataBaseHelper2.modifySubject(dateFormat1,sub);
                                    Log.d("sub",sub);
                                    subject.setText("");
                                    ETdescription.setText("");
                                    logsStatus.setVisibility(View.VISIBLE);
                                    // logsStatus.setTextColor(Integer.parseInt("#00FF00"));//green
                                    logsStatus.setText("successfully added!");
                                    savephonecall.setVisibility(View.GONE);
                                    savephonecall.setEnabled(false);
                                    cancel.setText("back");
                                    hideProgressBar();
                                }
                            });

                        }
                    });
                    // Toast.makeText(SaveLogsPage.this,"successfullt saved",Toast.LENGTH_LONG).show();
                    Log.d("create:","success");
                }

                // you code to handle response
            }

        });
    }

    private void getAccount() {
        SaveLogsPage.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PublicClientApplication.createSingleAccountPublicClientApplication(SaveLogsPage.this , R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application){

                        if(SaveLogsPage.this == null) Log.e("EMT","EMT");
                        mSingleAccountApp = application;
                       loadAccount();
                    }
                    @Override
                    public void onError(MsalException exception){
                        hideProgressBar();
                        Toast.makeText(SaveLogsPage.this,"error creating contact ,"+exception.toString(),Toast.LENGTH_LONG).show();
                        displayError(exception);
                    }
                });
            }
        });

    }

    private void displayError(MsalException exception) {
        SaveLogsPage.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                durationTxtView.setText("error");
                ETdescription.setText("error");
                logsCardView.setVisibility(View.GONE);
                logsStatus.setTextColor(0xFFFF0000);
                logsStatus.setText("you need to sign in before u save a log call!\n\n"+ exception.toString());
            }
        });

    }

   /* private void loadAccount() {

        if(mSingleAccountApp == null){
            return ;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                if(activeAccount!=null){
              useremail = activeAccount.getUsername();
              System.out.println("useremail in function:"+useremail);
            }else{
                    logsCardView.setVisibility(View.GONE);
                    logstatus1.setText("you need to sign in to save this phone call");
                }
            }
            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){



                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                hideProgressBar();
                Toast.makeText(SaveLogsPage.this,"error creating contact ,"+exception.toString(),Toast.LENGTH_LONG).show();
                displayError(exception);

            }
        });
    }


    */



    public void fetchLogs(){


        ContentResolver cr = SaveLogsPage.this.getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {
                    directionBoolean = true;
                    phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    if(!phNumber.contains("+961") && !phNumber.contains("+") && !phNumber.equals("111") ){
                        if(phNumber.trim().startsWith("0")){
                            phNumber = phNumber.replace(String.valueOf(phNumber.charAt(0)),"");
                        }
                        String ninesixone="+961";
                        phNumber= ninesixone.concat(phNumber);
                        Log.d("concat",phNumber);
                        //  phNumber=phNumber.replace("+961","");
                    }
                    callDuration = String.valueOf((Integer.parseInt(String.valueOf(Integer.parseInt(callDuration)/60))));
                    datebeforeformat = callDate;
                    dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm aa");
                    dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));
                    String stringType;
                    try{
                      stringType = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE));
                        //       Toast.makeText(SaveLogsPage.this,stringType,Toast.LENGTH_LONG).show();
                        switch (stringType) {
                            case "2":
                                direction = "OUTGOING";
                                directionBoolean = true;
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = true ->"+directionBoolean);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = outgoing ->"+direction);
                                System.out.println("durection boolean = true ->"+directionBoolean);
                                break;
                            case "1":
                                direction = "INCOMING";
                                directionBoolean = false;
                                System.out.println("durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = INCOMING ->"+direction);
                                break;

                            case "3":
                                direction = "MISSED";
                                directionBoolean = false;
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = INCOMING ->"+direction);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false ->"+directionBoolean);
                                break;

                            default:
                                direction = "DEFAULT";
                                directionBoolean = false;
                                Log.d(String.valueOf(SaveLogsPage.this),"durection value = INCOMING DEFAULT ->"+direction);
                                Log.d(String.valueOf(SaveLogsPage.this),"durection boolean = false DEFAULT->"+directionBoolean);
                                break;
                        }
                    }catch(Exception e){
                        Log.d("direction",e.toString());

                    }


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
                    Log.d("dateStringlogs",dateString);
  //                  if(contactFound!=null){
                   CallLogs cl =new CallLogs(callDuration,String.valueOf(directionBoolean),dateString,phNumber,"false",contactFound.getContact_id());

                 if(dataBaseHelper2.addOne(cl)){
                     showToast(SaveLogsPage.this,"success");
                    Log.d("cl->>>>",cl.toString());
                  }else{
                     showToast(SaveLogsPage.this,"unsuccessful");
                 }
//                    Log.d("dateString",dateString);
//
//                    }else{
//                        Log.d("contactfound","is null");
//                    }

                    dataBaseHelper2.modifyContactid(dateString,contactFound);

                    updateUI(callDuration,phNumber,dateString,directionBoolean);
                }
            }
            c.close();
        }
    }

    private void updateUI(String callDuration, String phNumber, String dateFormat, Boolean direction) {

//by default its outgoing
        durationTxtView.setText(callDuration);
        desc = ETdescription.getText().toString().trim();
        int durationInt = (int)Double.parseDouble(callDuration);
        timeDate.setText(dateFormat.trim());

        contact.setText(contactFound.getContact_fname() + " " + contactFound.getContact_lname() );
        phonrNumber.setText(callReciever.numbertofetch);
        durationTxtView.setText(callDuration);
        //  if(cl2.getDuration()=="0") {
        durationTxtView.setText("MISSED");
        // }
        String sub = subject.getText().toString().trim();
        //logsStatus.setText("error fetching the contact!");

        if(!CallLogsAdapter.openedfromfrag){
            //saveLogs(sub,desc,durationInt,phNumber,dateFormat,direction);

        }

    }
    private void updateUI2(String callDuration, String phNumber, String dateFormat, Boolean direction) {

//by default its outgoing
        durationTxtView.setText(callDuration);
        desc = ETdescription.getText().toString().trim();
        int durationInt = (int)Double.parseDouble(callDuration);
        timeDate.setText(cl.getDate());

        contact.setText(dataBaseHelper.getContactName(cl.getCallerid()));
        phonrNumber.setText(phNumber);
        durationTxtView.setText(cl.getDuration());
        // if(cl.getDuration()=="0") {
        //   durationTxtView.setText("MISSED");
        // }
        String sub = subject.getText().toString().trim();
        //logsStatus.setText("error fetching the contact!");

        if(!CallLogsAdapter.openedfromfrag){
            //saveLogs(sub,desc,durationInt,phNumber,dateFormat,direction);

        }

    }

    public String fetchContactid(String email){
        //will return contactid of the person with the following email
//Log.d("fetchContactid",dataBaseHelper.getContactId("clarita.hawat@javista.com"));
        return dataBaseHelper.getContactId(email);

        // return "f9c25f54-59db-ec11-bb3d-000d3a66d2a8";
    }

    public String fetchContactidByphone(String phone){
        //will return contactid of the person with the following email
        // Log.d("fetchContactid",dataBaseHelper.getContactId("clarita.hawat@javista.com"));
        return dataBaseHelper.getContactIdByPhone(phone);

        // return "f9c25f54-59db-ec11-bb3d-000d3a66d2a8";
    }
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d("TAG", "Successfully authenticated");

            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                hideProgressBar();
                Toast.makeText(SaveLogsPage.this,"error creating contact ,"+exception.toString(),Toast.LENGTH_LONG).show();
                Log.d("TAG", "Authentication failed: " + exception.toString());

            }
            @Override
            public void onCancel() {
                /* User canceled the authentication */
                hideProgressBar();
                Toast.makeText(SaveLogsPage.this,"Canceled!",Toast.LENGTH_LONG).show();
                Log.d("TAG", "User cancelled login.");
            }
        };
    }

    void showToast(Context context,String message){
        Toast toast=Toast.makeText(context,message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }


    private void showProgressBar() {
        SaveLogsPage.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SaveLogsPage.this.findViewById(R.id.saveLogsProgress)
                        .setVisibility(View.VISIBLE);
                SaveLogsPage.this.findViewById(R.id.SaveLogsPage)
                        .setVisibility(View.GONE);

            }
        });

    }

    private void hideProgressBar() {
        SaveLogsPage.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SaveLogsPage.this.findViewById(R.id.saveLogsProgress)
                        .setVisibility(View.GONE);
           //     SaveLogsPage.this.findViewById(R.id.SaveLogsPage)
          //              .setVisibility(View.VISIBLE);
            }
        });

    }
}