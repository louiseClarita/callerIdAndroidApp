/*package com.example.calleridapplication;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import com.microsoft.graph.requests.extensions.MessageCollectionPage;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bottomSheetFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
/*
public class bottomSheetFrag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String URLline = "";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String accessToken;
    EditText ETgetnbre;
    Button Btnget,fetchEmails;
    String SCOPES[] ={"Mail.Read"};
    TextView jsonResponse;
    String numberToFetch;
    String name,JobTitle,Company,etag,contactid,email;
    String AUTHORITY;
    LinearLayout bottomsheetWannabe;
    TextView namecaller,companycaller,jobcaller,idcaller,recentMailscaller,etagcaller,getRecentEmails;
    BottomSheetBehavior sheetBehavior;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public bottomSheetFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *  //@param param1 Parameter 1.
     * // @param param2 Parameter 2.
     *  //@return A new instance of fragment bottomSheetFrag.
     */

    // TODO: Rename and change types and number of parameters
/*
    public static bottomSheetFrag newInstance() {
        bottomSheetFrag fragment = new bottomSheetFrag();

      ///  Bundle args = new Bundle();
      //  args.putString(ARG_PARAM1, param1);
    //    args.putString(ARG_PARAM2, param2);
      //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // if (getArguments() != null) {
       //    mParam1 = getArguments().getString(ARG_PARAM1);
      //      mParam2 = getArguments().getString(ARG_PARAM2);
      //  }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        ETgetnbre = view.findViewById(R.id.GETnbre);
        Btnget = view.findViewById(R.id.GETbtn);
        jsonResponse = view.findViewById(R.id.jsonresponse);
       // TextView namecaller,companycaller,idcaller,recentMailscaller;
        namecaller = view.findViewById(R.id.namecaller);
        companycaller = view.findViewById(R.id.companycaller);
        jobcaller = view.findViewById(R.id.jobcaller);
        idcaller = view.findViewById(R.id.idcaller);
        recentMailscaller = view.findViewById(R.id.recentMailscaller);
        etagcaller = view.findViewById(R.id.etagcaller);
        bottomsheetWannabe = view.findViewById(R.id.bottomsheetWannabe);
        getRecentEmails = view.findViewById(R.id.getRecentEmails);
        fetchEmails = view.findViewById(R.id.fetchEmails);
        Btnget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberToFetch = ETgetnbre.getText().toString().trim();
                        GetDataFromFunction(numberToFetch);
                    }
                });

            }



        });
        fetchEmails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getRecentEmails(email);
            }
        });






      /*  ButterKnife.bind(getActivity());
        sheetBehavior = BottomSheetBehavior.from(layoutBottom);
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState){
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:{
                        btnBottomSheet.setText("close sheet");
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED:{
                        btnBottomSheet.setText("EXPAND sheet");
                        break;
                    }


                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
*/
/*
        return view;
    }



    //manually open sheet on button click
/*
@OnClick(R.id.btn)
    public void toggleBottomSheet(){
        if(sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED){

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            btnBottomSheet.setText("close sheet");
        }else{


            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnBottomSheet.setText("open sheet");
        }
}

*/
  /*  private void GetDataFromFunction(String nbre) {

            URLline = "https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email="+nbre+"";
        //    URLline ="https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email=281-555-0161";

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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

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
        /*
    }
public void displayResponse(String response){
    getActivity().runOnUiThread(new Runnable() {
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
                    etag = dataobj.getString("@odata.etag");
                    email = dataobj.getString("emailaddress1");
                    updateLayout(name,JobTitle,Company,contactid,etag,email);
                //   getRecentEmails(email);
/*
                name =jsonObject.getString("fullname");
                JobTitle=jsonObject.getString("jobtitle");
                Company = jsonObject.getString("company");
                contactid = jsonObject.getString("company");
                etag = jsonObject.getString("@odata.etag");
                email = jsonObject.getString("emailaddress1");
                updateLayout(name,JobTitle,Company,contactid,etag,email);*/

              //  }


          //  }
        /*
        }catch(JSONException e){
            e.printStackTrace();
            Log.e(getActivity().toString(),e.getMessage().toString());
        }
        }
    });

}

    private void getRecentEmails(String email) {
        if(signin_fragment.graphClient == null){
            Toast.makeText(getActivity(),"client null",Toast.LENGTH_LONG).show();
            return;
        }
        LinkedList<Option> requestOptions = new LinkedList<Option>();
        requestOptions.add(new QueryOption("sender", "charbel.mattar@javista.com"));


        MessageCollectionPage messages = (MessageCollectionPage) signin_fragment.graphClient.me().messages()
                .buildRequest( requestOptions )
                .select("sender,subject").get();

       Message message = null;
      message.attachments.equals(messages);
        Log.d("subjects",message.subject);
        Log.d("recent emails",messages.getRawObject().toString());
        displayEmail(messages.getRawObject());



      /*  URLline = "https://graph.microsoft.com/v1.0/me/messages";
        //    URLline ="https://calleridfunction20220524032337.azurewebsites.net/api/ConnecttoD365?email=281-555-0161";

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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(stringRequest);
*/
      //  if(signin_fragment.token == null){
      //      Toast.makeText(getActivity(),"token msh mazbout null",Toast.LENGTH_LONG).show();
      //      return;
     //   }
    //    accessToken = signin_fragment.token;
/*
    accessToken = "eyJ0eXAiOiJKV1QiLCJub25jZSI6IkRwaUpVVjFfZV9ycFh6VkRSUTNtTFlONHNncV9PcHowN2o0Ykd2eTRqd00iLCJhbGciOiJSUzI1NiIsIng1dCI6ImpTMVhvMU9XRGpfNTJ2YndHTmd2UU8yVnpNYyIsImtpZCI6ImpTMVhvMU9XRGpfNTJ2YndHTmd2UU8yVnpNYyJ9.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTAwMDAtYzAwMC0wMDAwMDAwMDAwMDAiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9jOGNhZGI5OC0wYjIyLTQ1MjQtYjNlNS1iYzMzZjAxMWE4ZmMvIiwiaWF0IjoxNjUzNDEzOTIzLCJuYmYiOjE2NTM0MTM5MjMsImV4cCI6MTY1MzQxOTMwOSwiYWNjdCI6MCwiYWNyIjoiMSIsImFpbyI6IkFWUUFxLzhUQUFBQUdWaFg3elp2Vk9ZRUplZm9BMitYUE14M1B6UEprTnFLeTh5aHNjbmpjQWRHTXJUL0xsbm5aamVnQXFjc2ZKNllackNneEJGRWZxZS9CSGJINGxMWWVyTjYvYXBmSlpkMy9EeTNrb3hkczM4PSIsImFtciI6WyJwd2QiLCJtZmEiXSwiYXBwX2Rpc3BsYXluYW1lIjoiR3JhcGggRXhwbG9yZXIiLCJhcHBpZCI6ImRlOGJjOGI1LWQ5ZjktNDhiMS1hOGFkLWI3NDhkYTcyNTA2NCIsImFwcGlkYWNyIjoiMCIsImZhbWlseV9uYW1lIjoiTWF0dGFyIiwiZ2l2ZW5fbmFtZSI6IkNoYXJiZWwiLCJpZHR5cCI6InVzZXIiLCJpcGFkZHIiOiIxODUuMTA0LjI0NC4zMCIsIm5hbWUiOiJDaGFyYmVsIE1hdHRhciIsIm9pZCI6Ijk1MDM5ZmZmLTM2ZDktNGMxYy1hYTE2LTQ5YjZiNDkyMTA4ZiIsInBsYXRmIjoiMyIsInB1aWQiOiIxMDAzMjAwMUY3NzJCODhBIiwicmgiOiIwLkFYa0FtTnZLeUNJTEpFV3o1Ynd6OEJHb19BTUFBQUFBQUFBQXdBQUFBQUFBQUFDVUFOQS4iLCJzY3AiOiJNYWlsLlJlYWQgTWFpbC5SZWFkQmFzaWMgTWFpbC5SZWFkV3JpdGUgb3BlbmlkIHByb2ZpbGUgVXNlci5SZWFkIGVtYWlsIiwic2lnbmluX3N0YXRlIjpbImttc2kiXSwic3ViIjoiT201VTFyOEdCYkkza0w3cWxmVDcyQmRMcV9acVR1dGRyY2k2VWVfbElLZyIsInRlbmFudF9yZWdpb25fc2NvcGUiOiJFVSIsInRpZCI6ImM4Y2FkYjk4LTBiMjItNDUyNC1iM2U1LWJjMzNmMDExYThmYyIsInVuaXF1ZV9uYW1lIjoiY2hhcmJlbG1hdHRhckBjcmF6eXBob25lbGIub25taWNyb3NvZnQuY29tIiwidXBuIjoiY2hhcmJlbG1hdHRhckBjcmF6eXBob25lbGIub25taWNyb3NvZnQuY29tIiwidXRpIjoiZDV0eXJVcDR3VVdlS1BIY1JnMGlBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIiwiYjc5ZmJmNGQtM2VmOS00Njg5LTgxNDMtNzZiMTk0ZTg1NTA5Il0sInhtc19zdCI6eyJzdWIiOiI5elAtSzZHVkQydHRNQWNiZ3lxcFg3b242amRWaTVfdzdVNnptTEFLbUdjIn0sInhtc190Y2R0IjoxNjUxODI4MTM1fQ.EA1UwEegcFOmIHe920AeDb7uqPzdznFrHb5JMI2-dUaembPJPHJ7NU7Cxr2yTqcSVYGtwa3_e4pr-LI9sPVTV28UNUJQRRnkrIwdAVjGS5NSiFWNcWx_WomDlwALvLjhvfPHxQ1Fyf7W4Sb34hUwxU7JuNud9ZQ-bU7ye_sr6gCaBLiTq9VJTJls-on-1JZS_6j_5QidzBbHku_E8c12v1VaKYy9cS84A1UzPtHENTypiDJ2tPtZokEB_f26QNB4KRnkIavCyI24MjjzSTVI_Cb8xwxu0Z9sogxPElDmbmLPEDYlqgBUEyMHia5PpxqGF_AXBXC_anEX0UCPqnXQo";

        IGraphServiceClient graphClient =
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
        LinkedList<Option> requestOptions = new LinkedList<Option>();
        requestOptions.add(new QueryOption("sender", "email"));
        requestOptions.add(new HeaderOption("Authorization","Bearer"+accessToken));
        MessageCollectionPage messages = (MessageCollectionPage) graphClient.me().messages()
                .buildRequest( requestOptions )
                .select("sender,subject")
                .get();
        messages.getRawObject();
        Log.d("recent emails",messages.getRawObject().toString());
        displayEmail(messages.getRawObject());

/*
        if (mSingleAccountApp == null) {
            Toast.makeText(getActivity(),"account null",Toast.LENGTH_LONG).show();
            return;
        }
        AUTHORITY =;
        mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
*/
  /*  }

    private void displayEmail(JsonObject rawObject) {

        getRecentEmails.setText(rawObject.toString());

    }

    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
                //callGraphAPI(authenticationResult);
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString());
               // displayError(exception);
            }
        };
    }
    private void updateLayout(String name, String jobTitle, String company, String contactid, String etag,String email) {
        // TextView namecaller,companycaller,idcaller,recentMailscaller;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bottomsheetWannabe.setVisibility(View.VISIBLE);
                namecaller.setText(name);
                companycaller.setText(company);
                idcaller.setText(contactid);
                jobcaller.setText(jobTitle);
                recentMailscaller.setText(email);
                etagcaller.setText(etag);
            }
        });







    }

}


   */