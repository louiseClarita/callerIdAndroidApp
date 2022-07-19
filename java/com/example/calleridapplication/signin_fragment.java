package com.example.calleridapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CallerIdApplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.SilentAuthenticationCallback;
import com.microsoft.identity.client.exception.MsalException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signin_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signin_fragment extends Fragment {

    private final static String[] SCOPES = {"Files.Read","Mail.Read"};
    private View view = null;
    private View v = null;
    int count = 0;
    NavigationView navigationView;
    //  final static String AUTHORITY = "https://login.microsoftonline.com/common";
    final static String AUTHORITY = "https://login.windows.net/common/oauth2/authorize?resource=https://api.businesscentral.dynamics.com";
    public  static String token;
    public  static String Email;
    public static boolean signed = false;
    public static  IGraphServiceClient graphClient;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    //public static IGraphServiceClient graphClient;
    static Boolean is_signedin = false;
    Button signInButton;
    Button signOutButton;
    Button callGraphApiInteractiveButton;
    Button callGraphApiSilentButton;
    TextView logTextView;
    TextView currentUserTextView;
    TextView hello;
    Dialog dialog;
    DataBaseHelper3 dataBaseHelper3;
    DataBaseHelper dataBaseHelper;
    JsonObject DRIVEJSON;
    TextView graphData;
    public static String displayName=null;
    Boolean getgraph=false;
    //int signed=0;
    boolean newSign=false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public signin_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * /// @param param1 Parameter 1.
     * ///@param param2 Parameter 2.
     * ////@return A new instance of fragment signin_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static signin_fragment newInstance() {
        signin_fragment fragment = new signin_fragment();
        //  Bundle args = new Bundle();
        //  args.putString(ARG_PARAM1, param1);
        //  args.putString(ARG_PARAM2, param2);
        //   fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   initializeUI();
        //  if (getArguments() != null) {
        //      mParam1 = getArguments().getString(ARG_PARAM1);
        //      mParam2 = getArguments().getString(ARG_PARAM2);
        //  }
        //   setRetainInstance(true);
    }
    private void loadAccount(){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                updateUI(activeAccount);
              //  if(!is_signedin){

                    signed = true;
                    Log.d("count=", String.valueOf(dataBaseHelper3.getCount()));
                    if(!(dataBaseHelper3.getCount() == 0)){

                        showProgressBar();
                        TextView userName = first.mHeaderView.findViewById(R.id.userName);
                        TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);
                        userName.setText(dataBaseHelper3.getUser().getName());
                        userEmail.setText(dataBaseHelper3.getUser().getEmail());
                        hideProgressBar();
                            signInButton.setVisibility(View.GONE);
                            signOutButton.setVisibility(View.VISIBLE);

                            if (navigationView != null) {
                                navigationView = (NavigationView)first.navigationView;
                                Log.d("tTAG","should change the title");
                                Menu menu = navigationView.getMenu();
                                menu.findItem(R.id.nav_signin).setTitle("Sign out");
                                menu.findItem(R.id.nav_updateDB).setVisible(true);
                                //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                              //  navigationView.setNavigationItemSelectedListener(getActivity());
                            }
                        return;

                    }else {
                     //   navigationView = (NavigationView)view.findViewById(R.id.NavigationView);
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_signin).setTitle("Sign in");
                        menu.findItem(R.id.nav_updateDB).setVisible(false);
                        Toast.makeText(getActivity(),"please sign in",Toast.LENGTH_LONG).show();

                    }
             //   showProgressBar();
              //       mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());



                    //is_signedin=true;
             //   }
                //  openBrowserTabActivity();

            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    showProgressBar();
                    dataBaseHelper3.deleteUser();
                    if(dataBaseHelper.deleteDB()==0){
                        Toast.makeText(getActivity(),"error signing out,try again later",Toast.LENGTH_LONG).show();
                        return;
                    }

                    performOperationOnSignOut();

                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                hideProgressBar();
                displayError(exception);
            }
        });
    }





    private void initializeUI(){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {


                //Sign in user

            }
        });
    }
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d("TAG", "Successfully authenticated");
                is_signedin=false;
                /* Update UI */
                updateUI(authenticationResult.getAccount());

                /* call graph */
                callGraphAPI(authenticationResult);
            }



            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d("TAG", "Authentication failed: " + exception.toString());
                hideProgressBar();
                Toast.makeText(getActivity(),"please sign in",Toast.LENGTH_LONG).show();
                displayError(exception);
            }
            @Override
            public void onCancel() {
                hideProgressBar();
                /* User canceled the authentication */
                Log.d("TAG", "User cancelled login.");
            }
        };
    }

    private void uploadContacts() {

               showProgressBar();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://calleridcrmapi.azure-api.net/contacts")
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                // .get()
                //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideProgressBar();
                Toast.makeText(getActivity(),"error uploading data , please try again later \n"+e.toString(),Toast.LENGTH_LONG).show();
                Log.e("okhttp1",e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String responseBody = null;
                            try {
                                responseBody = response.body().string();

                            Log.d("strr->>0",responseBody);

                            Log.e("okhttp2",responseBody);
                            hideProgressBar();
                            Toast.makeText(getActivity(),"error uploading data , please try again later \n"+response.toString(),Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    //Toast.makeText(first.this,"not successful:"+responseBody,Toast.LENGTH_LONG).show();

                    throw new IOException("Unexpected code " + response);
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String responseBody = "";

                                responseBody = response.body().toString();
                                Log.d("strr->>0",responseBody);
                                parseJSON(responseBody);
                             //   Toast.makeText(getActivity(),"error uploading data , please try again later \n"+response.toString(),Toast.LENGTH_LONG).show();


                        }
                    });

                   // hideProgressBar();
                }
            }


        });
    }
    private void parseJSON(String response) {

        String firstname,lastname,contactid,jobTitle,company,email,mobilephone;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int size = 300;
            //   if(jsonObject.getString("status").equals("true")){
            JSONArray callerid = jsonObject.getJSONArray("value");
            //  for (int i = 0; i < callerid.length(); i++) {

                     for (int i = 0; i < size; i++) {
                         Log.d("i->>", String.valueOf(i));
                //    String name,JobTitle,Company,etag,contactid;
                JSONObject dataobj = callerid.getJSONObject(i);
                firstname =dataobj.getString("firstname");
                lastname = dataobj.getString("lastname");
                jobTitle=dataobj.getString("jobtitle");
                company = dataobj.getString("cr051_companyname");
                contactid = dataobj.getString("contactid");
                //    etag = dataobj.getString("@odata.etag");
                email = dataobj.getString("emailaddress1");
                mobilephone = dataobj.getString("mobilephone");

                ContactModel c11 = new ContactModel(contactid,lastname,firstname,company,jobTitle,email,mobilephone);
                Log.d("contact model:",c11.toString());
                dataBaseHelper.addOne(c11);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            hideProgressBar();
        }
        first.navigationView.setEnabled(true);
        first.toolbar.setEnabled(true);
        graphData.setText("Your account is fully Loaded!\nWelcome "+dataBaseHelper3.getUser().getName()+". ");
        Log.d("stopppped...","updating");
        hideProgressBar();
    }
    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
                // is_signedin=true;

                callGraphAPI(authenticationResult);
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString());
                hideProgressBar();
                Toast.makeText(getActivity(),"please sign in",Toast.LENGTH_LONG).show();
                displayError(exception);
            }
        };
    }


    public void SignInButton(View v){

    }


    private void callGraphAPI(IAuthenticationResult authenticationResult) {

        final String accessToken = authenticationResult.getAccessToken();
        token=authenticationResult.getAccessToken();
        //  storage.SaveAuthenticationState(authenticationResult.getAccessToken());

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
      try{  graphClient
                .me()
                .drive()
                .buildRequest()
                .get(new ICallback<Drive>() {
                    @Override
                    public void success(final Drive drive) {
                        Log.d("TAG", "Found Drive " + drive.id);
                        displayGraphResult(drive.getRawObject());
                        Log.d("TAG JSON FILE",drive.getRawObject().toString());
                        parsingJson(drive.getRawObject());
                        first.isSignedIn=true;

                    }

                    @Override
                    public void failure(ClientException ex) {
                        displayError(ex);
                    }
                });
      }catch(Exception e){
          Log.d("exepttt",e.toString());
      }
      }



    private void updateUI(@Nullable final IAccount account) {

/*
        if(getActivity() == null){
            Log.e("EMPT","empty actv");
            return;
        }*/
        //  getActivity().runOnUiThread(new Runnable() {
        //     @Override
        //   public void run() {
        if (account != null) {

            signInButton.setEnabled(false);
            signOutButton.setEnabled(true);
            callGraphApiInteractiveButton.setEnabled(true);
            callGraphApiSilentButton.setEnabled(true);
            currentUserTextView.setText(account.getUsername());
            Email = account.getUsername();
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_signin).setTitle("Sign out");
            menu.findItem(R.id.nav_updateDB).setVisible(true);
            if(!Email.contains("crazyphonelb")){
                signInButton.setEnabled(true);
                signOutButton.setEnabled(false);

                callGraphApiInteractiveButton.setEnabled(false);
                callGraphApiSilentButton.setEnabled(false);
                currentUserTextView.setText("");
                logTextView.setText("");
                is_signedin=false;
                 menu = navigationView.getMenu();
                menu.findItem(R.id.nav_signin).setTitle("Sign in");
                menu.findItem(R.id.nav_updateDB).setVisible(false);
                dataBaseHelper3.deleteUser();
                dataBaseHelper.deleteDB();
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {   signout();
                    }
                });


                thread.start();

            }
        } else {
            signInButton.setEnabled(true);
            signOutButton.setEnabled(false);

            callGraphApiInteractiveButton.setEnabled(false);
            callGraphApiSilentButton.setEnabled(false);
            currentUserTextView.setText("");
            logTextView.setText("");
            is_signedin=false;
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_signin).setTitle("Sign in");
            menu.findItem(R.id.nav_updateDB).setVisible(false);
        }

        //Sign in user

        ////  }
        //   });

    }
public void signout(){

            try  {  if (mSingleAccountApp == null){
                return;
            }
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {

                            updateUI(null);
                            Intent i = new Intent(getActivity(),first.class);
                            getActivity().startActivity(i);
                            Toast.makeText(getActivity(),"account does not belong to the organisaton!\ntry signing in again using another account.",Toast.LENGTH_LONG).show();


                    }

                    @Override
                    public void onError(@NonNull MsalException exception) {
                        return;

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

}
    private void displayError(@NonNull final Exception exception) {
       Log.d("error",exception.toString());
        /*  getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.setText(exception.toString());
                //Sign in user

            }
        });

         */
    }
    private  void displayGraphResult(@NonNull final JsonObject graphResponse) {
        is_signedin=true;

       /* getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.setText(graphResponse.toString());
                //Sign in user

            }
        });*/
        // String displayName = graphResponse.get("displayName").toString();
        // logTextView.setText("hello"+ displayName);

    }
    private  void parsingJson(@NonNull final JsonObject graphResponse){
        if(!graphResponse.equals(null)){
            is_signedin=true;
            JsonObject owner=graphResponse.getAsJsonObject("owner");
            JsonObject user = owner.getAsJsonObject("user");
            displayName = user.get("displayName").toString().replaceAll("\"","").trim();
            Email = user.get("email").toString().replaceAll("\"","").trim();
            if(!displayName.isEmpty() ){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        signInButton.setVisibility(View.GONE);
                        signOutButton.setVisibility(View.VISIBLE);
                        TextView userName = first.mHeaderView.findViewById(R.id.userName);
                        TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);

                        if(dataBaseHelper3.addOne(new User(displayName,Email))){
                            Log.d("user","added successfully");
                        }
                        if (navigationView != null) {

                            Menu menu = navigationView.getMenu();
                            menu.findItem(R.id.nav_signin).setTitle("Sign Out");

                            //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                            //  navigationView.setNavigationItemSelectedListener(getActivity());
                        }
                        userName.setText(displayName);
                        userEmail.setText(Email);

                        uploadContacts();
                        hideProgressBar();
                        //Sign in user

                    }
                });

            }else{
                Toast.makeText(getActivity(),"name or email are empty",Toast.LENGTH_LONG).show();
            }
            //   Constants.USERNAME=displayName;
//            hello.setText(" salut!");
            // Intent intent = new Intent(MainActivity.this,BrowserTabActivity.class);
            // intent.putEx1tra("displayName",displayName);
            //  this.startActivity(intent);

        }else{
            Toast.makeText(getActivity(),"json empty",Toast.LENGTH_LONG).show();
        }


    }
    private void performOperationOnSignOut() {

        hideProgressBar();

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

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_signin).setTitle("Sign in");
        menu.findItem(R.id.nav_updateDB).setVisible(false);
        Toast.makeText(getActivity(),"please sign in",Toast.LENGTH_LONG).show();
        graphData.setText("");
        signInButton.setVisibility(View.VISIBLE);
        signOutButton.setVisibility(View.GONE);
        signInButton.setEnabled(true);
        final String signOutText = "Signed Out.";
        TextView userName = first.mHeaderView.findViewById(R.id.userName);
        TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);

        userName.setText("username");
        userEmail.setText("username@org.onmicrosoft.com");

        currentUserTextView.setText("");
        is_signedin=false;
        logTextView.setText("");
        signed = false;
        //signed=0;
        getgraph=false;
        Toast.makeText(getActivity(), signOutText, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        is_signedin=false;
        view = inflater.inflate(R.layout.fragment_signin_fragment, container, false);
        navigationView = (NavigationView)first.navigationView;
        graphData = (TextView)view.findViewById(R.id.graphData);
        if(view == null)Log.e("EMT VW","EMT VIEW");
        graphData.setText("");
        initializeUI();
        fetchButtonClicks(view);
        dataBaseHelper3 = new DataBaseHelper3(getActivity());
        dataBaseHelper = new DataBaseHelper(getActivity());
        if(dataBaseHelper3.getCount()==1){
            graphData.setText("Welcome "+dataBaseHelper3.getUser().getName());
        }
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
                hideProgressBar();
                Log.d("TAG",exception.toString());
                displayError(exception);

            }
        });
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_PHONE_STATE} ,1);
        }

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void fetchButtonClicks(View view) {
        signInButton = view.findViewById(R.id.signInbtn);
        callGraphApiSilentButton = view.findViewById(R.id.callGraphSilent);
        callGraphApiInteractiveButton = view.findViewById(R.id.callGraphInteractive);
        signOutButton = view.findViewById(R.id.clearCache);
        logTextView = view.findViewById(R.id.txt_log);
        currentUserTextView = view.findViewById(R.id.current_user);

        signInButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                showProgressBar();
                if (mSingleAccountApp == null) {
                    return;
                }
                first.navigationView.setEnabled(false);
                first.toolbar.setEnabled(false);
                graphData.setText("Please wait untill your account is loaded.");
                mSingleAccountApp.signIn(getActivity(), null, SCOPES, getAuthInteractiveCallback());

                newSign=true;

            }
        });

        //Sign out user
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        if (navigationView != null) {
                            Log.d("tTAG","should change the title");
                            Menu menu = navigationView.getMenu();
                            menu.findItem(R.id.nav_signin).setTitle("Sign in");
                            first.navigationView.setEnabled(true);
                            first.toolbar.setEnabled(true);
                            graphData.setText("");
                            dataBaseHelper3.deleteUser();
                            //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                            //  navigationView.setNavigationItemSelectedListener(getActivity());
                        }
                        signInButton.setEnabled(true);
                        signInButton.setVisibility(View.VISIBLE);
                        signOutButton.setVisibility(View.GONE);
                        updateUI(null);
                        performOperationOnSignOut();
                        newSign=false;
                        Intent i = new Intent(getActivity(),first.class);
                        getActivity().startActivity(i);
                    }
                    @Override
                    public void onError(@NonNull MsalException exception){
                        hideProgressBar();

                        displayError(exception);
                    }
                });
            }
        });

        //Interactive
        callGraphApiInteractiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null) {
                    return;
                }
                mSingleAccountApp.acquireToken(getActivity(), SCOPES, getAuthInteractiveCallback());
            }
        });

        //Silent
        callGraphApiSilentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
            }
        });
    }
    private void showProgressBar() {
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {
        getActivity().findViewById(R.id.progressSignIn)
                .setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.FrameSignIn)
                .setEnabled(false);
        getActivity().findViewById(R.id.FrameSignIn)
                .setVisibility(View.INVISIBLE);
    }
});



    }

    private void hideProgressBar() {
getActivity().runOnUiThread(new Runnable() {
    @Override
    public void run() {
        getActivity().findViewById(R.id.progressSignIn)
                .setVisibility(View.GONE);
        getActivity().findViewById(R.id.FrameSignIn)
                .setEnabled(true);
        first.navigationView.setEnabled(false);

        getActivity().findViewById(R.id.FrameSignIn)
                .setVisibility(View.VISIBLE);

    }
});
            }
}