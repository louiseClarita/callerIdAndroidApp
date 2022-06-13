package com.example.calleridapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CallerIdApplication.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link signin_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class signin_fragment extends Fragment {

    private final static String[] SCOPES = {"Files.Read","Mail.Read"};
    private View view = null;
    private View v = null;
    //  final static String AUTHORITY = "https://login.microsoftonline.com/common";
    final static String AUTHORITY = "https://login.windows.net/common/oauth2/authorize?resource=https://api.businesscentral.dynamics.com";
    public  static String token;
    public  static String Email;
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
    JsonObject DRIVEJSON;
    public static String displayName=null;
    Boolean getgraph=false;
    int signed=0;
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
                if(!is_signedin){
                mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());
                is_signedin=true;
                newSign=true;
                }
                //  openBrowserTabActivity();

            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    performOperationOnSignOut();

                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
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
                is_signedin=true;
                /* Update UI */
                updateUI(authenticationResult.getAccount());
                /* call graph */
                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d("TAG", "Authentication failed: " + exception.toString());
                displayError(exception);
            }
            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d("TAG", "User cancelled login.");
            }
        };
    }

    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d("TAG", "Successfully authenticated");
                is_signedin=true;
                callGraphAPI(authenticationResult);
            }
            @Override
            public void onError(MsalException exception) {
                Log.d("TAG", "Authentication failed: " + exception.toString());
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
        graphClient
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
                } else {
                    signInButton.setEnabled(true);
                    signOutButton.setEnabled(false);
                    callGraphApiInteractiveButton.setEnabled(false);
                    callGraphApiSilentButton.setEnabled(false);
                    currentUserTextView.setText("");
                    logTextView.setText("");
                    is_signedin=false;
                }

                //Sign in user

          ////  }
    //   });

    }

    private void displayError(@NonNull final Exception exception) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.setText(exception.toString());
                //Sign in user

            }
        });
    }
    private  void displayGraphResult(@NonNull final JsonObject graphResponse) {
        is_signedin=true;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logTextView.setText(graphResponse.toString());
                //Sign in user

            }
        });
        // String displayName = graphResponse.get("displayName").toString();
        // logTextView.setText("hello"+ displayName);

    }
    private  void parsingJson(@NonNull final JsonObject graphResponse){
        if(!graphResponse.equals(null)){
            JsonObject owner=graphResponse.getAsJsonObject("owner");
            JsonObject user = owner.getAsJsonObject("user");
            displayName = user.get("displayName").toString().replaceAll("\"","").trim();
            Email = user.get("email").toString().replaceAll("\"","").trim();
            if(!displayName.isEmpty() ){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView userName = first.mHeaderView.findViewById(R.id.userName);
                      TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);
                        userName.setText(displayName);
                       userEmail.setText(Email);
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
        final String signOutText = "Signed Out.";
        currentUserTextView.setText("");
        is_signedin=false;
        logTextView.setText("");
        signed=0;
        getgraph=false;
        Toast.makeText(getActivity(), signOutText, Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         view = inflater.inflate(R.layout.fragment_signin_fragment, container, false);
          if(view == null)Log.e("EMT VW","EMT VIEW");

         initializeUI();
        fetchButtonClicks(view);
        PublicClientApplication.createSingleAccountPublicClientApplication(getActivity(), R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){

                if(getActivity() == null) Log.e("EMT","EMT");
                mSingleAccountApp = application;
                loadAccount();
            }
            @Override
            public void onError(MsalException exception){
                displayError(exception);
            }
        });
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
                if (mSingleAccountApp == null) {
                    return;
                }
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
                        updateUI(null);
                        performOperationOnSignOut();
                        newSign=false;
                    }
                    @Override
                    public void onError(@NonNull MsalException exception){
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

}