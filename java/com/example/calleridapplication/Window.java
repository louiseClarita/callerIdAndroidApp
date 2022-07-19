package com.example.calleridapplication;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Contact;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;

import static android.content.Context.WINDOW_SERVICE;

import static com.example.calleridapplication.signin_fragment.is_signedin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import kotlinx.coroutines.Job;

public class Window {

    private Context context;
    private View mView;
    private final static String[] SCOPES = {"Files.Read","Mail.Read"};
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    ListView emailList;
    static Boolean opened = false;
    String Token="";
    static ContactModel contactFound = null;
    public static boolean closed = false;
    public static boolean found=false;
    public IGraphServiceClient graphClient=null;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    EmailModel e1 = new EmailModel("emailsubject","time");
    EmailModel e2 = new EmailModel("payment issus","25-05-2022");
    EmailModel e3 = new EmailModel("payment validated","25-05-2022");
    List<EmailModel> list = null;
    EmailModel emailsss;
    Button openInApp;
    Button fetchEmails;
    String time;
    LinearLayout bottomsheetWannabe;
    String[] EmailSubjects = {};
    String subject;
    final static String AUTHORITY = "https://login.windows.net/common/oauth2/authorize?resource=https://api.businesscentral.dynamics.com";
    String name,JobTitle,Company,etag,email;
    ImageView dynamics;
    static String contactid;
    TextView namecaller,companycaller,jobcaller,recentMailscaller,getRecentEmails;
    String URLline;
   static String numberToFetch;
   DataBaseHelper dataBaseHelper;
    public Window(Context context){
        System.out.println("i am inside");

        closed=false;
        found=false;

        this.context = context;
      //  found = false;
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
     /*   .addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

      */
        //getting layout inflater
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.activity_calling_page,null);
         list = new ArrayList<EmailModel>();
        list.add(e1);
        //list.add(e2);list.add(e3);
         opened = true;
         System.out.println("opened1"+opened);
        dataBaseHelper = new DataBaseHelper(context);
        // set onClickListener on the remove button, which removes
        // the view from the window
        mView.findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closed = true;
                close();
            }
        });
        namecaller = mView.findViewById(R.id.namecaller);
        companycaller = mView.findViewById(R.id.companycaller);
        jobcaller = mView.findViewById(R.id.jobcaller);
        emailList = mView.findViewById(R.id.emailList);
        openInApp =  mView.findViewById(R.id.openinapp);
        dynamics = mView.findViewById(R.id.dynamicslogo);
        dynamics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDynamics();
            }
        });
        //loadListView(list);
        emailList.setVisibility(View.GONE);
        openInApp.setVisibility(View.GONE);
        //  idcaller = findViewById(R.id.idcaller);
        // recentMailscaller = findViewById(R.id.recentMailscaller);
        //  etagcaller = findViewById(R.id.etagcaller);
        bottomsheetWannabe = mView.findViewById(R.id.bottomsheetWannabe);
        // getRecentEmails = findViewById(R.id.getRecentEmails);
        fetchEmails = mView.findViewById(R.id.fetchEmails1);
        PublicClientApplication.createSingleAccountPublicClientApplication(context, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){

                if(context == null) Log.e("EMT","EMT");
                mSingleAccountApp = application;

            }
            @Override
            public void onError(MsalException exception){
              Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show();
            }
        });



        mParams.gravity = Gravity.BOTTOM;

        mWindowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        numberToFetch = callReciever.numbertofetch;
        search4contactLocaly(numberToFetch);
        Log.d("numbertofetch",numberToFetch.trim());
        // search4contactLocaly("71759182");

    }

public void open(){


        try{
            //checking if the view is already inflated or present in the window
        //    if(mView.getWindowToken() == null){
        //        if(mView.getParent() == null){
                    mWindowManager.addView(mView,mParams);
        //        }
        //    }

        }catch(Exception e){
            Log.d("Error openning ",e.toString());
        }


}

public void close(){
        try{
            // remove the view from the window
            ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).removeView(mView);
            // invalidate the view
            //mView.invalidate();
            // remove all views
            ForegroundService.notification = null;
            //((ViewGroup)mView.getParent()).removeAllViews();

            // the above steps are necessary when you are adding and removing
            // the view simultaneously, it might give some exceptions

        }catch (Exception e) {
            Log.d("Error2",e.toString());
        }
}



public void search4contactLocaly(String number){
    found=false;
     contactFound =dataBaseHelper.fetchcontact(number.trim());



        if(contactFound.getContact_id().equals("")){
            Log.d("found ctct","nope not found");
            found =false;
            Log.d("window.1",String.valueOf(found));
            Toast.makeText(context,"not found locally will search dynamics",Toast.LENGTH_LONG).show();
            GetDataFromFunction(number);
            return;
        }
    found =true;
        Log.d("window.found",String.valueOf(found));
        updateLayout2(contactFound.getContact_fname(),contactFound.getContact_lname(),contactFound.getContact_job(),contactFound.getContact_company());

        openInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openCalllogsRelated(context,contactFound,Token);


            }
        });
   getTokenForGraph(contactFound.getContact_email());

}

    private void openApp(String contact_id) {
        Intent i = new Intent(context,relatedCallLogs.class);
        i.putExtra("id",contact_id);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

    }

    public void getTokenForGraph(String email){
Toast.makeText(context,"getting emails...",Toast.LENGTH_LONG).show();
Log.d("emaiiiilss","fetching emails");
        PublicClientApplication.createSingleAccountPublicClientApplication(context, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){

                if(context == null) Log.e("EMT","EMT");
                //showProgressBar();
                mSingleAccountApp = application;
                Log.d("loadaccouunt","in progress");
                loadAccount(email);
            }
            @Override
            public void onError(MsalException exception){
                Log.d("msalexce",exception.toString());
                Toast.makeText(context,exception.toString(),Toast.LENGTH_LONG).show();
            }
        });
        // url = ("https://graph.microsoft.com/v1.0/me/messages?$filter=(from/emailAddress/address) eq 'charbel.mattar@javista.com'&$select=subject,body,receivedDateTime");

        //  signin_fragment.mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());

       // getAuthSilentCallback(email);
    }
    private void getAuthSilentCallback(String email) {
        /*ù
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
               */ getRecentEmails(email);/*
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString());
                // displayError(exception);
            }
        };*/
    }


    private void getRecentEmails(String email1122) {
        final List<Option> options = new LinkedList<>();
        // options.add(new QueryOption("search",
        //3.3     "mobilePhone:\""+number+"\""));

        // Start and end times adjusted to user's time zone
        if(contactFound.getContact_email().isEmpty() ||contactFound.getContact_email().equals("null")){
            Toast.makeText(context,"you dont know the email of this person",Toast.LENGTH_LONG).show();
        }
        if(email1122.isEmpty() ||email1122.equals("null") || email1122.equals("")){
            Toast.makeText(context,"you dont know the email of this person",Toast.LENGTH_LONG).show();
            emailList.setVisibility(View.GONE);

            return;
        }
        options.add(new QueryOption("filter",
                "(from/emailAddress/address) eq '"+email1122+"'"));
        options.add(new QueryOption("orderby",
                "sentDateTime desc"));

        signin_fragment.graphClient
                .me()
                .messages()
                .buildRequest(options)
                .top(2)
                .get(new ICallback<IMessageCollectionPage>() {

                    @Override
                    public void success(IMessageCollectionPage iMessageCollectionPage) {
                        Log.d("emails::",iMessageCollectionPage.getRawObject().toString());

                        displayEmail(iMessageCollectionPage.getRawObject());

                    }

                    @Override
                    public void failure(ClientException ex) {
                        Log.d("errorEmail",ex.getMessage().toString());
                    }
                });



    }
    private void displayEmail(JsonObject rawObject) {


        List<EmailModel> Emails= new ArrayList<>();
        EmailModel emailsss = null;
        JsonArray emails = rawObject.getAsJsonArray("value");

        if(emails.size()==0){
            Emails = new ArrayList<>();
            emailsss = new EmailModel("no emails","");
            Log.d("emails","no emails");

            Emails.add(emailsss);
            return;
        }
        for(int i =0 ;i<emails.size();i++){

            emailsss=null;
            JsonObject dataObject = (JsonObject) emails.get(i);

            String subject = dataObject.get("subject").toString().replaceAll("\"","").trim();
        /*dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));*/
            String time = dataObject.get("sentDateTime").toString().replaceAll("\"","").trim();
            String read = dataObject.get("isRead").toString().replaceAll("\"","").trim();
            System.out.println("read"+read);
            String body = dataObject.get("bodyPreview").toString().replaceAll("\"","").trim();
           /* DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm", Locale.ENGLISH);
            LocalDate date = LocalDate.parse("2018-04-10T04:00:00.000Z", inputFormatter);
            String formattedDate = outputFormatter.format(date);
            System.out.println(formattedDate);*/
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm aa");
                Date date = null;

                date = inputFormat.parse(time);

                String dateString = outputFormat.format(date);
                System.out.println(dateString);
                Log.d("time",dateString);
// public EmailModel(String subject, String time, Boolean read, String bodyPreview) {
                System.out.println("Boolean.valueOf(read)"+Boolean.valueOf(read));
                emailsss = new EmailModel(subject,dateString,Boolean.valueOf(read),body);
                Emails.add(emailsss);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        loadListView(Emails);

    }



    private void loadListView(List<EmailModel> emailSubjects) {

/** private Context context;
 private View mView;
 private final static String[] SCOPES = {"Files.Read","Mail.Read"};
 private WindowManager.LayoutParams mParams;
 private WindowManager mWindowManager;
 private LayoutInflater layoutInflater;**/

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter itemsAdapter =
                        new MailsAdapter(context, R.layout.mailsadapter_layout, emailSubjects);
                emailList.setVisibility(View.VISIBLE);
                emailList.setAdapter(itemsAdapter);
                Helper.getListViewSize(emailList);
            }
        });





        }



    private void GetDataFromFunction(String nbre) {
        found=false;
        Log.d("window.2",String.valueOf(found));
        URLline = "https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email="+nbre+"";
        //    URLline ="https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email=70753661";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                        Log.d("window.response1",String.valueOf(found));
                        //found =true;
                        displayResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Log.d("window.response2",String.valueOf(found));
                        found=false;
                        Log.d("window.response2.0",String.valueOf(found));
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(stringRequest);

    }

    public void displayResponse(String response){

                //jsonResponse.setText(response);
                Log.d("json",response);
                try{    JSONObject jsonObject = new JSONObject(response);
                    //   if(jsonObject.getString("status").equals("true")){
                    JSONArray callerid = jsonObject.getJSONArray("value");
                    if(callerid.isNull(0)){
                       // found = false;
                     updateLayout1("not found","contact not in your CRM","");
                        list.clear();
                        loadListView(list);
                        return;
                    }
                    //for (int i = 0; i < callerid.length(); i++) {
                    //    String name,JobTitle,Company,etag,contactid;
                    JSONObject dataobj = callerid.getJSONObject(0);
                    name =dataobj.getString("firstname");
                    String lname = dataobj.getString("lastname");
                    JobTitle=dataobj.getString("jobtitle");
                    Company = dataobj.getString("cr051_companyname");
                    contactid = dataobj.getString("contactid");
                    String mobilephone= dataobj.getString("mobilephone");
                    //    etag = dataobj.getString("@odata.etag");
                    email = dataobj.getString("emailaddress1");
                    contactFound = new ContactModel(contactid,name,lname,Company,Company,email,mobilephone);
                    if(dataBaseHelper.addOne(contactFound)) {
                        Toast.makeText(context,"success",Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(context,"unsuccessfull",Toast.LENGTH_LONG).show();
                    }
                    if(!email.equals("") || !email.equals("null") || !email.isEmpty()){
                        openInApp.setVisibility(View.VISIBLE);
                        openInApp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openApp(contactFound.getContact_id());
                            }
                        });
                    }

                    if(Window.contactFound.getContact_id().equals(null)||Window.contactFound.getContact_id().isEmpty()){
                          Log.d("jsonnn","empty");
                    }else{
                        Log.d("jsonnn",contactFound.toString());
                    }
                    found=true;
                   // updateLayout1(name,JobTitle,Company);
                    updateLayout2(name,lname, JobTitle,Company);
                  //  getRecentEmails (email);
                    getTokenForGraph(contactFound.getContact_email());
                }catch(JSONException e){
                    e.printStackTrace();
                    Log.e("TAG",e.getMessage().toString());
                }
            }

//this will be when fetching from crm
    private void updateLayout1(String name, String jobTitle, String company) {
        // TextView namecaller,companycaller,idcaller,recentMailscaller;

        bottomsheetWannabe.setVisibility(View.VISIBLE);
        namecaller.setText(name);
        if(!company.equals("null") || !company.isEmpty())companycaller.setText(company);
        //  idcaller.setText(contactid);
        if(!jobTitle.equals("null") || !jobTitle.isEmpty())jobcaller.setText(jobTitle);
        //   recentMailscaller.setText(email);
        //  etagcaller.setText(etag);

    }
    // this will be when fetching from local database
    private void updateLayout2(String fname,String lname, String jobTitle, String company) {
        // TextView namecaller,companycaller,idcaller,recentMailscaller;

        bottomsheetWannabe.setVisibility(View.VISIBLE);
        namecaller.setText(fname +" "+ lname);
        if(!company.equals("null") || !company.isEmpty())companycaller.setText(company);
        //  idcaller.setText(contactid);
        if(!jobTitle.equals("null") || !jobTitle.isEmpty())jobcaller.setText(jobTitle);
        //   recentMailscaller.setText(email);
        //  etagcaller.setText(etag);

    }
    private void loadAccount(String email1){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
               // updateUI(activeAccount);
               // if(!is_signedin){
                    mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback2(email1));


                   // newSign=true;
              //  }
                //  openBrowserTabActivity();

            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    Toast.makeText(context,"you need to sign in!Account Info Changed",Toast.LENGTH_LONG).show();

                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                Toast.makeText(context, exception.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private SilentAuthenticationCallback getAuthSilentCallback2(String email) {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
                is_signedin=true;
               callGraphAPI(authenticationResult,email);
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString() + exception.getMessage().toString());

            }
        };
    }
    private void callGraphAPI(IAuthenticationResult authenticationResult,String emails) {
        //GraphHelper graphHelper= GraphHelper.getInstance();
        //   graphHelper.get
        Token = authenticationResult.getAccessToken();
        openInApp.setVisibility(View.VISIBLE);

        final List<Option> options = new LinkedList<>();
        // options.add(new QueryOption("search",
        //3.3     "mobilePhone:\""+number+"\""));

        // Start and end times adjusted to user's time zone
        if(contactFound.getContact_email().isEmpty() ||contactFound.getContact_email().equals("null")){
            Toast.makeText(context,"you dont know the email of this person",Toast.LENGTH_LONG).show();
        }
        options.add(new QueryOption("filter",
                "sentDateTime ge 2022-01-01T00:00:00Z and (from/emailAddress/address) eq '"+emails+"'"));
        options.add(new QueryOption("$orderby",
                "sentDateTime DESC"));
        //token=authenticationResult.getAccessToken();
        //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());

        graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d("TAG", "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " +Token);
                                Log.d("token is", Token);
                            }
                        })
                        .buildClient();
        graphClient
                .me()
                .messages()
                .buildRequest(options)
                .top(2)
                .get(new ICallback<IMessageCollectionPage>() {

                    @Override
                    public void success(IMessageCollectionPage iMessageCollectionPage) {
                        Log.d("response",iMessageCollectionPage.getRawObject().toString());

                        displayEmail(iMessageCollectionPage.getRawObject());

                    }

                    @Override
                    public void failure(ClientException ex) {
                        Log.d("error",ex.getMessage().toString());
                    }
                });
    }

    public void openCalllogsRelated(Context ctx,ContactModel c,String Token){
        Intent i = new Intent(ctx,relatedCallLogs.class);
        Log.d("opening",c.getContact_fname()+""+c.getContact_lname());
        Log.d("idinadapter",c.getContact_id());
        i.putExtra("id",c.getContact_id());
        i.putExtra("token",Token);
        ctx.startActivity(i);
    }
    public void goToDynamics(){
        if(contactFound.getContact_id().isEmpty() || contactFound.getContact_id().equals("")){
            Toast.makeText(context,"page not found!",Toast.LENGTH_LONG).show();
            return;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://orgc452f0fa.crm4.dynamics.com/main.aspx?appid=b9966deb-62c8-ec11-a7b5-0022489de0f3&forceUCI=1&pagetype=entityrecord&etn=contact&id="+contactFound.getContact_id()+""));
        context.startActivity(browserIntent);
    }
}
