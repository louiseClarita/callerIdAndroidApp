package com.example.calleridapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CallerIdApplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class relatedCallLogs extends AppCompatActivity {
TextView ctctName;
ListView listview;
DataBaseHelper2 dt2;
DataBaseHelper dt1;
TextView email;
ContactModel contact;
String token;
TextView emailStatus,phonecallstatus;
ImageView back;
ListView listview2;
Button btnGetEmails;
List<CallLogs> list;

ArrayAdapter adapter;
    private final static String[] SCOPES = {"Files.Read","Mail.Read"};
    public static Boolean openCallLogs = false;
    private View view = null;
    private View v = null;
    //  final static String AUTHORITY = "https://login.microsoftonline.com/common";
    final static String AUTHORITY = "https://login.windows.net/common/oauth2/authorize?resource=https://api.businesscentral.dynamics.com";
    public static IGraphServiceClient graphClient;

    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatedcalllogs_layout);
        ctctName = findViewById(R.id.contactrelated);
        listview = findViewById(R.id.listview) ;
        email = findViewById(R.id.Relatedemail);
       // notAvailable =findViewById(R.id.notAvaiable);
        listview2 = findViewById(R.id.listview2);
        emailStatus = findViewById(R.id.emailstatus);
        back = findViewById(R.id.backtoapp);
        phonecallstatus = findViewById(R.id.phonecallstatus);
        dt2 = new DataBaseHelper2(relatedCallLogs.this);
        dt1 = new DataBaseHelper(relatedCallLogs.this);
        String name =null;
        Bundle bundle = getIntent().getExtras();
         String id=null;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent i = new Intent(relatedCallLogs.this,first.class);
              //  openCallLogs = true;
                relatedCallLogs.this.finish();
            }
        });
        if(!bundle.getString("token").equals("") && !bundle.getString("id").equals("")){
            id =bundle.getString("id").toString().trim();
            token =bundle.getString("token").toString().trim();
            Log.d("idinpage",id);
            name = dt1.getContactName(id);
            contact = dt1.fetchAllInfo(id);
            Log.d("opening",name);
            ctctName.setText(name );
            list = dt2.fetchCalllogsByContactid(id);
            email.setText(contact.getContact_email());
            if(list.size()==0){
                //CallLogs(String calllogid,String duration, String direction, String date, String phoneNbre, String saved, String callername)
                phonecallstatus.setVisibility(View.VISIBLE);
            }else{
                email.setText(contact.getContact_email());
                adapter = new CallLogsAdapter(relatedCallLogs.this,R.layout.calllogsadapter_layout,list);

                listview.setVisibility(View.VISIBLE);
                listview.setAdapter(adapter);
                Helper.getListViewSize(listview);
            }
            PublicClientApplication.createSingleAccountPublicClientApplication(relatedCallLogs.this, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
                @Override
                public void onCreated(ISingleAccountPublicClientApplication application){

                    if(relatedCallLogs.this == null) Log.e("EMT","EMT");
                    mSingleAccountApp = application;

                    showProgressBar();
                    listview2.setVisibility(View.VISIBLE);
                    getRecentWithToken(contact.getContact_email(),token);

                }
                @Override
                public void onError(MsalException exception){
                    hideProgressBar();
                    emailStatus.setText("Error Getting your emails with "+contact.getContact_fname()+"! Check your internet connection and try again later!");
                    Toast.makeText(relatedCallLogs.this,"error getting emails... ,"+exception.toString(),Toast.LENGTH_LONG).show();
                    Log.d("exception",exception.toString());
                }
            });

        }
        if(!bundle.getString("id").equals(""))
        {
            id =bundle.getString("id").toString().trim();
            Log.d("idinpage",id);
            name = dt1.getContactName(id);
            contact = dt1.fetchAllInfo(id);
            Log.d("opening",name);
            ctctName.setText(name );
            list = dt2.fetchCalllogsByContactid(id);
            email.setText(contact.getContact_email());
            if(list.size()==0){
                //CallLogs(String calllogid,String duration, String direction, String date, String phoneNbre, String saved, String callername)
                phonecallstatus.setVisibility(View.VISIBLE);
            }else{
            email.setText(contact.getContact_email());
            adapter = new CallLogsAdapter(relatedCallLogs.this,R.layout.calllogsadapter_layout,list);

            listview.setVisibility(View.VISIBLE);
            listview.setAdapter(adapter);
            Helper.getListViewSize(listview);
            }
                 //  if(!contact.getContact_email().equals("") && !contact.getContact_email().isEmpty() && !contact.getContact_email().equals("null")){
                    //   if(!signin_fragment.signed){

                    //       emailStatus.setText("Please sign in to view the related email between you and "+contact.getContact_fname()+" " +contact.getContact_lname());
                           // notAvailable.setVisibility(View.VISIBLE);

                //       }
       // else{
                           Log.d("ok","signed in should be fetching emails");
                           emailStatus.setText("");
                           //          notAvailable.setVisibility(View.GONE);

                           Log.d("ok","fetching emails for  "+contact.getContact_email());
            if(contact.getContact_email().isEmpty() ||contact.getContact_email().equals("null") || contact.getContact_email().equals("")){
                Toast.makeText(relatedCallLogs.this,"you dont know the email of this person",Toast.LENGTH_LONG).show();
                listview2.setVisibility(View.GONE);
                emailStatus.setText("you don't know the email of "+contact.getContact_fname()+"!");
                return;
            }else{
                           PublicClientApplication.createSingleAccountPublicClientApplication(relatedCallLogs.this, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
                               @Override
                               public void onCreated(ISingleAccountPublicClientApplication application){

                                   if(relatedCallLogs.this == null) Log.e("EMT","EMT");
                                   mSingleAccountApp = application;

                                       showProgressBar();
                                  listview2.setVisibility(View.VISIBLE);
                                   loadAccount(contact.getContact_email());

                               }
                               @Override
                               public void onError(MsalException exception){
                                   hideProgressBar();
                                   emailStatus.setText("Error Getting your emails with "+contact.getContact_fname()+"! Check your internet connection and try again later!");
                                   Toast.makeText(relatedCallLogs.this,"error getting emails... ,"+exception.toString(),Toast.LENGTH_LONG).show();
                                   Log.d("exception",exception.toString());
                               }
                           });
                          // getTokenForGraph(contact.getContact_email().trim());

                  //     }
                //   }
               }


        }




    }

    private void getRecentWithToken(String contact_email, String token) {

        callGraphAPI2(token,contact_email);






    }
    private void callGraphAPI2(String accessTokenn,String emailsss) {


        //  token=authenticationResult.getAccessToken();
        //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());
        final List<Option> options = new LinkedList<>();
        graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d("TAG", "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessTokenn);
                                Log.d("token is", accessTokenn);
                            }
                        })
                        .buildClient();

        options.add(new QueryOption("filter",
                "sentDateTime ge 2022-01-01T00:00:00Z and (from/emailAddress/address) eq '"+emailsss+"'"));
        options.add(new QueryOption("$orderby",
                "sentDateTime DESC"));
        graphClient
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
    public void getTokenForGraph(String email){

        if (signin_fragment.mSingleAccountApp == null){
            Toast.makeText(relatedCallLogs.this,"you need to be signed in to find your emails with this contact",Toast.LENGTH_LONG).show();
            return;
        }

        // url = ("https://graph.microsoft.com/v1.0/me/messages?$filter=(from/emailAddress/address) eq 'charbel.mattar@javista.com'&$select=subject,body,receivedDateTime");
       // AUTHORITY = "https://graph.microsoft.com/v1.0/";
        //  signin_fragment.mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
        getAuthSilentCallback(email);
    }
    private void getAuthSilentCallback(String email) {
        /*ù
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticati onResult) {
                Log.d("TAG", "Successfully authenticated");
               */ getRelatedEmails(email);/*
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString());
                // displayError(exception);
            }
        };*/
    }
    private void getRelatedEmails(String contact_email) {

       Log.d("graphclient",signin_fragment.graphClient.toString()) ;

        final List<Option> options = new LinkedList<>();
        Log.d("gettingemails","...");
        // Start and end times adjusted to user's time zone
        if(contact_email.isEmpty() ||contact_email.equals("null") || contact_email.equals("")){
            Toast.makeText(relatedCallLogs.this,"you dont know the email of this person",Toast.LENGTH_LONG).show();
            listview2.setVisibility(View.GONE);

            return;
        }
        options.add(new QueryOption("filter",
                "(from/emailAddress/address) eq '"+contact_email+"'"));
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
        hideProgressBar();

        List<EmailModel> Emails= new ArrayList<>();
       EmailModel emailsss = null;
        JsonArray emails = rawObject.getAsJsonArray("value");

        if(emails.size()==0){
            Emails = new ArrayList<>();
            emailsss = new EmailModel("no emails","");
            Log.d("emails","no emails");
            emailStatus.setText("You have no emails with "+contact.getContact_fname()+".");
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
relatedCallLogs.this.runOnUiThread(new Runnable() {
    @Override
    public void run() {
        ArrayAdapter itemsAdapter =
                new EmailsAdapter2(relatedCallLogs.this, R.layout.activity_emails_adapter2, emailSubjects);
        listview2.setVisibility(View.VISIBLE);
        listview2.setAdapter(itemsAdapter);
        Helper.getListViewSize(listview2);

    }
});


    }


    private void loadAccount(String email){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                //   updateUI(activeAccount);
                //    if(!is_signedin){
                mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback2(email));

                //is_signedin=true;
                //      newSign=true;
                //    }
                //  openBrowserTabActivity();

            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                //    performOperationOnSignOut();
Toast.makeText(relatedCallLogs.this,"account changed, please sign in again!",Toast.LENGTH_LONG).show();
                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                hideProgressBar();
                Toast.makeText(relatedCallLogs.this,"error creating contact ,"+exception.toString(),Toast.LENGTH_LONG).show();

                Log.d("exception",exception.toString());
            }
        });
    }
    private SilentAuthenticationCallback getAuthSilentCallback2(String email) {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
                // is_signedin=true;
                callGraphAPI(authenticationResult,email);
            }
            @Override

            public void onError(MsalException exception) {
                hideProgressBar();
                Toast.makeText(relatedCallLogs.this,"error creating contact ,"+exception.toString(),Toast.LENGTH_LONG).show();

                Log.d("TAG", "Authentication failed: " + exception.toString());
                Log.d("emailerror",exception.toString());
            }
        };
    }
    private void callGraphAPI(IAuthenticationResult authenticationResult,String emailsss) {

        final String accessToken = authenticationResult.getAccessToken();
      //  token=authenticationResult.getAccessToken();
        //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());
        final List<Option> options = new LinkedList<>();
        graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d("TAG", "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);
                                Log.d("token is", accessToken);
                            }
                        })
                        .buildClient();

        options.add(new QueryOption("filter",
                "sentDateTime ge 2022-01-01T00:00:00Z and (from/emailAddress/address) eq '"+emailsss+"'"));
        options.add(new QueryOption("$orderby",
                "sentDateTime DESC"));
        graphClient
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
    private void showProgressBar() {
        relatedCallLogs.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                relatedCallLogs.this.findViewById(R.id.progressBar123)
                        .setVisibility(View.VISIBLE);
                relatedCallLogs.this.findViewById(R.id.emailLayout)
                        .setVisibility(View.GONE);

            }
        });

    }

    private void hideProgressBar() {
      relatedCallLogs.this.runOnUiThread(new Runnable() {
    @Override
    public void run() {
        relatedCallLogs.this.findViewById(R.id.progressBar123)
                .setVisibility(View.GONE);
        relatedCallLogs.this.findViewById(R.id.emailLayout)
                .setVisibility(View.VISIBLE);
    }
});

    }
}
