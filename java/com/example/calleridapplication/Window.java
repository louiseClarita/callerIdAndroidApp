package com.example.calleridapplication;

import android.content.Context;
import android.graphics.PixelFormat;
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
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.extensions.Contact;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;

import static android.content.Context.WINDOW_SERVICE;

import androidx.annotation.UiThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Window {

    private Context context;
    private View mView;
    private WindowManager.LayoutParams mParams;
    private WindowManager mWindowManager;
    private LayoutInflater layoutInflater;
    ListView emailList;
    static ContactModel contactFound = null;
    public static boolean found=false;
    EmailModel e1 = new EmailModel("emailsubject","time");
    EmailModel e2 = new EmailModel("payment issus","25-05-2022");
    EmailModel e3 = new EmailModel("payment validated","25-05-2022");
    List<EmailModel> list = null;
    EmailModel emailsss;
    Button fetchEmails;
    String time;
    LinearLayout bottomsheetWannabe;
    String[] EmailSubjects = {};
    String subject;
    String AUTHORITY;
    String name,JobTitle,Company,etag,email;
    static String contactid;
    TextView namecaller,companycaller,jobcaller,recentMailscaller,getRecentEmails;
    String URLline;
   static String numberToFetch;
   DataBaseHelper dataBaseHelper;
    public Window(Context context){
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
        //getting layout inflater
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.activity_calling_page,null);
         list = new ArrayList<EmailModel>();
        list.add(e1);
        //list.add(e2);list.add(e3);

        dataBaseHelper = new DataBaseHelper(context);
        // set onClickListener on the remove button, which removes
        // the view from the window
        mView.findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
        namecaller = mView.findViewById(R.id.namecaller);
        companycaller = mView.findViewById(R.id.companycaller);
        jobcaller = mView.findViewById(R.id.jobcaller);
        emailList = mView.findViewById(R.id.emailList);
        loadListView(list);
        //  idcaller = findViewById(R.id.idcaller);
        // recentMailscaller = findViewById(R.id.recentMailscaller);
        //  etagcaller = findViewById(R.id.etagcaller);
        bottomsheetWannabe = mView.findViewById(R.id.bottomsheetWannabe);
        // getRecentEmails = findViewById(R.id.getRecentEmails);
        fetchEmails = mView.findViewById(R.id.fetchEmails1);


        fetchEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  if(email.isEmpty()){
                  //  Toast.makeText(context,"empty email",Toast.LENGTH_LONG).show();
                //    return;
            //    }

                getRecentEmails(email);
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
            if(mView.getWindowToken() == null){
                if(mView.getParent() == null){
                    mWindowManager.addView(mView,mParams);
                }
            }

        }catch(Exception e){
            Log.d("Error openning ",e.toString());
        }


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
   getTokenForGraph(contactFound.getContact_email());

}

    public void getTokenForGraph(String email){

        if (signin_fragment.mSingleAccountApp == null){
            Toast.makeText(context,"you need to be signed in to find your emails with this contact",Toast.LENGTH_LONG).show();
            return;
        }

        // url = ("https://graph.microsoft.com/v1.0/me/messages?$filter=(from/emailAddress/address) eq 'charbel.mattar@javista.com'&$select=subject,body,receivedDateTime");
        AUTHORITY = "https://graph.microsoft.com/v1.0/";
        //  signin_fragment.mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
        getAuthSilentCallback(email);
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


    private void getRecentEmails(String email) {
        final List<Option> options = new LinkedList<>();
        // options.add(new QueryOption("search",
        //3.3     "mobilePhone:\""+number+"\""));

        // Start and end times adjusted to user's time zone
        if(contactFound.getContact_email().isEmpty() ||contactFound.getContact_email().equals("null")){
            Toast.makeText(context,"you dont know the email of this person",Toast.LENGTH_LONG).show();
        }
        options.add(new QueryOption("filter",
                "(from/emailAddress/address) eq '"+email+"'"));
        signin_fragment.graphClient
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
    private void displayEmail(JsonObject rawObject) {

        List<EmailModel> Emails=null;
        EmailSubjects = new String[]{};
        JsonArray emails = rawObject.getAsJsonArray("value");

        if(emails.size()==0){
            Emails = new ArrayList<>();
            emailsss = new EmailModel("no emails","");
            Emails.add(emailsss);
            return;
        }
        for(int i =0 ;i<emails.size();i++){
            Emails = new ArrayList<>();
        JsonObject dataObject = (JsonObject) emails.get(i);

        subject = dataObject.get("subject").toString();
        /*dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));*/
        time = dataObject.get("sentDateTime").toString().replaceAll("\"","").trim();

    /*  Date date = new Date(Long.valueOf(time));
              SimpleDateFormat formatter = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            String dateString = formatter.format(new Date(Long
                    .parseLong(String.valueOf(date))));
            Log.d("unmodified","date: "+time);
         Log.d("time",dateString);
*/
        emailsss = new EmailModel(subject,time);
        Emails.add(emailsss);
       }
        loadListView(Emails);

    }



    private void loadListView(List<EmailModel> emailSubjects) {


            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    ArrayAdapter itemsAdapter =
                            new MailsAdapter(context, R.layout.mailsadapter_layout, emailSubjects);

                    emailList.setAdapter(itemsAdapter);


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
                    if(Window.contactFound.getContact_id().equals(null)||Window.contactFound.getContact_id().isEmpty()){
                          Log.d("jsonnn","empty");
                    }else{
                        Log.d("jsonnn",contactFound.toString());
                    }
                    found=true;
                    updateLayout1(name,JobTitle,Company);
                    getRecentEmails(email);
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
        companycaller.setText(company);
        //  idcaller.setText(contactid);
        jobcaller.setText(jobTitle);
        //   recentMailscaller.setText(email);
        //  etagcaller.setText(etag);

    }
    // this will be when fetching from local database
    private void updateLayout2(String fname,String lname, String jobTitle, String company) {
        // TextView namecaller,companycaller,idcaller,recentMailscaller;

        bottomsheetWannabe.setVisibility(View.VISIBLE);
        namecaller.setText(fname +" "+ lname);
        companycaller.setText(company);
        //  idcaller.setText(contactid);
        jobcaller.setText(jobTitle);
        //   recentMailscaller.setText(email);
        //  etagcaller.setText(etag);

    }

}
