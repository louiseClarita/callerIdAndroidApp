package com.example.calleridapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.google.android.material.navigation.NavigationView;
import com.microsoft.graph.models.extensions.Contact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    String URL_GETALLCONTACTS = "https://getallcontacts20220530100450.azurewebsites.net/api/Function1?code=vQ39xN3XM4syuBk6ACNCDkUwpwAsS-EiiQi3Trc4028RAzFuOQbYKQ==";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String displayName1;
    DataBaseHelper dataBaseHelper;
    TextView welcome;
    TextView logs;
    Button btngoTo;
    Button btnfetchcaller,btnsaveLogs;
    Dialog dialog;
    public static boolean isSignedIn = false;
    private String[] SCOPES = { "User.Read","Contacts.Read","Contacts.ReadWrite" };
    String AUTHORITY = "https://login.microsoftonline.com/v1.0/me";
    private static final String TAG = callReciever.class.getSimpleName();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   public String number = "70753661";
    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * //@param param1 Parameter 1.
     * //@param param2 Parameter 2.
     * //@return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
      //  Bundle args = new Bundle();
      //  args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
     //  fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     //   if (getArguments() != null) {
      //      mParam1 = getArguments().getString(ARG_PARAM1);
      //      mParam2 = getArguments().getString(ARG_PARAM2);

     //   }



    }
    private void initializeUI(){
        if(isSignedIn){
            if(signin_fragment.displayName != null ){

                displayName1 =signin_fragment.displayName;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        welcome = getActivity().findViewById(R.id.welcome);
                        logs =  getActivity().findViewById(R.id.logs);


                String firstName = displayName1.replaceAll("\"","");
                String[] name= firstName.split(" ");
                welcome.setText("welcome " + name[0]);
                    }
                });

                }
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_main, container, false);
         dataBaseHelper = new DataBaseHelper(getActivity().getApplicationContext());
        btngoTo = view.findViewById(R.id.goTo);
        btnfetchcaller = view.findViewById(R.id.btnfetchcaller);
        btnsaveLogs = view.findViewById(R.id.opensavelogs);
        btngoTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
               fetchAllContacts();
                //    Log.d("getting everyone",dataBaseHelper.getEveryone().toString()) ;
            }
        });

        btnfetchcaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_LONG).show();
                fetchcaller(number);
            }
        });
        initializeUI();


      btnsaveLogs.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              opensaveLogs();
          }
      });


        return view;
    }

    private void fetchcaller(String number) {


       ContactModel c = dataBaseHelper.fetchcontact("70753661");
       if(c==null){
           Toast.makeText(getActivity(),"contact was not found ",Toast.LENGTH_LONG).show();
       }else{
        Toast.makeText(getActivity(),c.toString(),Toast.LENGTH_LONG).show();
       }

    }

    private void fetchAllContacts() {



        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL_GETALLCONTACTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("strrrrr",">>"+response);
                        parseJSON(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
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
                ContactModel c = new ContactModel(contactid,lastname,firstname,company,jobTitle,email,mobilephone);
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

public void  opensaveLogs(){
        Intent i = new Intent(getActivity(),SaveLogsPage.class);
        getActivity().startActivity(i);
}
}