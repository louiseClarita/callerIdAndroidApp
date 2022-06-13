package com.example.calleridapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;
import com.microsoft.graph.requests.extensions.IMessageCollectionRequest;
import com.microsoft.graph.requests.extensions.IMessageCollectionRequestBuilder;
import com.microsoft.graph.requests.extensions.MessageCollectionPage;
import com.microsoft.graph.requests.extensions.MessageCollectionRequestBuilder;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CallingPage extends AppCompatActivity {
    private String URLline = "";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String accessToken;
    EditText ETgetnbre;
    Button Btnget,fetchEmails;
    String SCOPES[] ={"Mail.Read","Files.Read"};
    TextView jsonResponse;
    String numberToFetch;
    String name,JobTitle,Company,etag,contactid,email;
    String AUTHORITY;
    ListView emailList;
    LinearLayout bottomsheetWannabe;
    String[] EmailSubjects = {};
    String subject;
    PopupWindow popUp;
    FrameLayout layout;
    TextView namecaller,companycaller,jobcaller,recentMailscaller,getRecentEmails;
    BottomSheetBehavior sheetBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.CallerIdApplication.R.layout.activity_calling_page);
        Bundle bundle =getIntent().getExtras();

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);

        popUp = new PopupWindow(CallingPage.this);
        layout = new FrameLayout(this);

        //changing the height and width of the pop up window
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.3));


         //   ETgetnbre = findViewById(R.id.GETnbre);

        if(!bundle.getString("number").toString().isEmpty()){
            numberToFetch = bundle.getString("number").toString();
//            ETgetnbre.setEnabled(false);
        }


    //    Btnget = findViewById(R.id.GETbtn1);
      //  jsonResponse =findViewById(R.id.jsonresponse);
        // TextView namecaller,companycaller,idcaller,recentMailscaller;
        namecaller = findViewById(R.id.namecaller);
        companycaller = findViewById(R.id.companycaller);
        jobcaller = findViewById(R.id.jobcaller);
        emailList = findViewById(R.id.emailList);
      //  idcaller = findViewById(R.id.idcaller);
       // recentMailscaller = findViewById(R.id.recentMailscaller);
      //  etagcaller = findViewById(R.id.etagcaller);
        bottomsheetWannabe = findViewById(R.id.bottomsheetWannabe);
       // getRecentEmails = findViewById(R.id.getRecentEmails);
        fetchEmails = findViewById(R.id.fetchEmails1);

fetchEmails.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
});
       // Btnget.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View view) {
                if(bundle.getString("number").toString().isEmpty()){
                 //   numberToFetch = ETgetnbre.getText().toString().trim();
                   // ETgetnbre.setEnabled(false);
                }


                   runOnUiThread(new Runnable() {
                       @Override
                       public void run() {

                           GetDataFromFunction(numberToFetch);
                       }
                   });


         //   }




        fetchEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTokenForGraph();
            }
        });



    }

   public void getTokenForGraph(){

           if (signin_fragment.mSingleAccountApp == null){
               Toast.makeText(CallingPage.this,"accc null eeyoune",Toast.LENGTH_LONG).show();
               return;
           }

           // url = ("https://graph.microsoft.com/v1.0/me/messages?$filter=(from/emailAddress/address) eq 'charbel.mattar@javista.com'&$select=subject,body,receivedDateTime");
           AUTHORITY = "https://graph.microsoft.com/v1.0/";
         //  signin_fragment.mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
           getAuthSilentCallback();
   }



    private void GetDataFromFunction(String nbre) {

       URLline = "https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email="+nbre+"";
        //    URLline ="https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email=70753661";

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLline,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr",">>"+response);
                        displayResponse(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(CallingPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
        /*
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://calleridfunction20220524033023.azurewebsites.net/api/ConnecttoD365?email=281-555-0161")
                .method("GET", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            displayResponse(response);


        } catch (IOException e) {
            e.printStackTrace();
        }

*/
    }
    public void displayResponse(String response){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //jsonResponse.setText(response);
                Log.d("json",response);
                try{    JSONObject jsonObject = new JSONObject(response);
                    //   if(jsonObject.getString("status").equals("true")){
                    JSONArray callerid = jsonObject.getJSONArray("value");
                    //for (int i = 0; i < callerid.length(); i++) {
                    //    String name,JobTitle,Company,etag,contactid;
                    JSONObject dataobj = callerid.getJSONObject(0);
                    name =dataobj.getString("fullname");
                    JobTitle=dataobj.getString("jobtitle");
                    Company = dataobj.getString("cr051_companyname");
                   contactid = dataobj.getString("contactid");
                //    etag = dataobj.getString("@odata.etag");
                    email = dataobj.getString("emailaddress1");
                    updateLayout(name,JobTitle,Company);
                    //   getRecentEmails(email);

                    /*
                name =jsonObject.getString("fullname");
                JobTitle=jsonObject.getString("jobtitle");
                Company = jsonObject.getString("company");
                contactid = jsonObject.getString("company");
                etag = jsonObject.getString("@odata.etag");
                email = jsonObject.getString("emailaddress1");
                updateLayout(name,JobTitle,Company,contactid,etag,email);
                */

                    //  }


                    //  }
                }catch(JSONException e){
                    e.printStackTrace();
                    Log.e("TAG",e.getMessage().toString());
                }
            }
        });

    }
    private void displayEmail(JsonObject rawObject) {


            //   if(jsonObject.getString("status").equals("true")){
           // JSONArray callerid = rawObject.getJSONArray("value");
            //for (int i = 0; i < callerid.length(); i++) {
            JsonArray emails = rawObject.getAsJsonArray("value");

            JsonObject dataObject = (JsonObject) emails.get(0);
            subject = dataObject.get("subject").toString();

            EmailSubjects = new String[]{subject};
            loadListView(EmailSubjects);
            //    String name,JobTitle,Company,etag,contactid;
         /*   for(int i=0;i<3;i++){
         JSONObject dataobj = emails.getJSONObject(i);
            name =dataobj.getString("fullname");
            JobTitle=dataobj.getString("jobtitle");
            Company = dataobj.getString("cr051_companyname");
              contactid = dataobj.getString("contactid");
            //    etag = dataobj.getString("@odata.etag");
            email = dataobj.getString("emailaddress1");
            }
*/
    }

    private void loadListView(String[] emailSubjects) {
runOnUiThread(new Runnable() {
    @Override
    public void run() {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(CallingPage.this, android.R.layout.simple_list_item_1, emailSubjects);
        emailList.setAdapter(itemsAdapter);
    }
});



    }

    // private SilentAuthenticationCallback getAuthSilentCallback() {
        private void getAuthSilentCallback() {
        /*ù
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
               */ getRecentEmails();/*
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString());
                // displayError(exception);
            }
        };*/
    }

    private void updateLayout(String name, String jobTitle, String company) {
        // TextView namecaller,companycaller,idcaller,recentMailscaller;

                bottomsheetWannabe.setVisibility(View.VISIBLE);
                namecaller.setText(name);
                companycaller.setText(company);
             //  idcaller.setText(contactid);
                jobcaller.setText(jobTitle);
             //   recentMailscaller.setText(email);
             //  etagcaller.setText(etag);

    }
    private void getRecentEmails() {



       // final String accessToken = authenticationResult.getAccessToken();

        //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());

       /*IGraphServiceClient graphClient =
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
                        .buildClient();*/


        signin_fragment.graphClient
                .me()
                .messages()
                .buildRequest()
                .select("sender,subject")
                .top(1)
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































        //if(signin_fragment.graphClient == null){
        //    Toast.makeText(CallingPage.this,"client null",Toast.LENGTH_LONG).show();
        //    return;
        //}
        /*accessToken = signin_fragment.token;

        String url = null;

            url = ("https://graph.microsoft.com/v1.0/me/messages?$filter=(from/emailAddress/address) eq 'charbel.mattar@javista.com'&$select=subject,body,receivedDateTime");



        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals(null)) {
                    Log.e("Your Array Response", response);
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "bearer"+accessToken);
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
             //   params.put("User", UserName);
             //   params.put("Pass", PassWord);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
*/
    }


    public void goToDynamics(View v){
        if(contactid.isEmpty()){
            Toast.makeText(CallingPage.this,"page not found!",Toast.LENGTH_LONG).show();
            return;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://orgc452f0fa.crm4.dynamics.com/main.aspx?appid=b9966deb-62c8-ec11-a7b5-0022489de0f3&forceUCI=1&pagetype=entityrecord&etn=contact&id="+contactid+""));
        startActivity(browserIntent);
    }

}

