package com.example.calleridapplication;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CallerIdApplication.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class relatedCallLogs extends AppCompatActivity {
TextView ctctName;
ListView listview;
DataBaseHelper2 dt2;
DataBaseHelper dt1;
TextView email;
ContactModel contact;
TextView emailStatus;
ImageView notAvailable;
ListView listview2;
Button btnGetEmails;
List<CallLogs> list;
ArrayAdapter adapter;
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
        btnGetEmails = findViewById(R.id.getEmails);
        dt2 = new DataBaseHelper2(relatedCallLogs.this);
        dt1 = new DataBaseHelper(relatedCallLogs.this);
        String name =null;
        Bundle bundle = getIntent().getExtras();
         String id=null;
        if(bundle.getString("id")!= null)
        {
            id =bundle.getString("id").toString().trim();
            Log.d("idinpage",id);
            name = dt1.getContactName(id);
            contact = dt1.fetchAllInfo(id);
            Log.d("opening",name);
            ctctName.setText(name );
            list = dt2.fetchCalllogsByContactid(id);
            email.setText(contact.getContact_email());
            adapter = new CallLogsAdapter(relatedCallLogs.this,R.layout.calllogsadapter_layout,list);
            listview.setAdapter(adapter);
           btnGetEmails.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   if(!contact.getContact_email().equals("") && !contact.getContact_email().isEmpty() && !contact.getContact_email().equals("null")){
                       if(!signin_fragment.is_signedin || signin_fragment.Email.isEmpty()){

                           emailStatus.setText("Please sign in to view the related email between you and "+contact.getContact_fname()+" " +contact.getContact_lname());
                           // notAvailable.setVisibility(View.VISIBLE);

                       }else{
                           Log.d("ok","signed in should be fetching emails");
                           emailStatus.setText("");
                           //          notAvailable.setVisibility(View.GONE);
                           showProgressBar();
                           Log.d("ok","fetching emails for this person "+contact.getContact_email());
                           getTokenForGraph(contact.getContact_email().trim());

                       }
                   }
               }
           });



        }



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
        // options.add(new QueryOption("search",
        //3.3     "mobilePhone:\""+number+"\""));
    Log.d("gettingemails","...");
        // Start and end times adjusted to user's time zone
        if(contact_email.isEmpty() ||contact_email.equals("null")){
            Toast.makeText(relatedCallLogs.this,"you dont know the email of this person",Toast.LENGTH_LONG).show();
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
        List<EmailModel> Emails=null;
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
            Emails = new ArrayList<>();
            JsonObject dataObject = (JsonObject) emails.get(i);

          String subject = dataObject.get("subject").toString();
        /*dateFormat= new Date(Long.valueOf(callDate));
                    callDayTimes = String.valueOf(dateFormat);
                    //DateTimeFormatter dt = new DateTimeFormatterBuilder(dateFormat);
                    //Log.d('DATETIME:',dt.formatGmt('yyyy-MM-dd\'T\'HH:mm:ss.SSS\'Z\''));

                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));*/
          String time = dataObject.get("sentDateTime").toString().replaceAll("\"","").trim();

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
                        new MailsAdapter(relatedCallLogs.this, R.layout.mailsadapter_layout, emailSubjects);
                listview2.setVisibility(View.VISIBLE);
                listview2.setAdapter(itemsAdapter);


            }
        });
    }

    private void showProgressBar() {

        relatedCallLogs.this.findViewById(R.id.progressBar123)
                .setVisibility(View.VISIBLE);
        relatedCallLogs.this.findViewById(R.id.emailLayout)
                .setVisibility(View.GONE);

    }

    private void hideProgressBar() {

        relatedCallLogs.this.findViewById(R.id.progressBar123)
                .setVisibility(View.GONE);
        relatedCallLogs.this.findViewById(R.id.emailLayout)
                .setVisibility(View.VISIBLE);
    }
}
