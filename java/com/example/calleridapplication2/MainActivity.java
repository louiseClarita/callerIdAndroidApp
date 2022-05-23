// Copyright (c) Microsoft Corporation.
// Licensed under the MIT License.

package com.example.calleridapplication2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.microsoft.identity.client.exception.MsalUiRequiredException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SAVED_IS_SIGNED_IN = "isSignedIn";
    private static final String SAVED_USER_NAME = "userName";
    private static final String SAVED_USER_EMAIL = "userEmail";
    private final static String[] SCOPES = {"Files.Read"};
    final static String AUTHORITY = "https://login.microsoftonline.com/common";
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    private static final String TAG=MainActivity.class.getSimpleName();

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private View mHeaderView;
    private boolean mIsSignedIn = false;
    private String mUserName = null;
    private String mUserEmail = null;
    private String mUserTimeZone = null;

    //private AuthenticationHelper mAuthHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PublicClientApplication.createSingleAccountPublicClientApplication(getApplicationContext(),R.raw.msal,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){
                mSingleAccountApp = application;
                loadAccount();
            }
            @Override
            public void onError(MsalException exception){
                Log.d("error",exception.toString());
            }
        });
        // Set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);

        // Add the hamburger menu icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = findViewById(R.id.nav_view);

        // Set user name and email
        mHeaderView = mNavigationView.getHeaderView(0);
        setSignedInState(mIsSignedIn);

        // Listen for item select events on menu
        mNavigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // Load the home fragment by default on startup
            openHomeFragment(mUserName);
        } else {
            // Restore state
            mIsSignedIn = savedInstanceState.getBoolean(SAVED_IS_SIGNED_IN);
            mUserName = savedInstanceState.getString(SAVED_USER_NAME);
            mUserEmail = savedInstanceState.getString(SAVED_USER_EMAIL);

            setSignedInState(mIsSignedIn);
        }

        // <InitialLoginSnippet>
        showProgressBar();
        /*
        // Get the authentication helper
        AuthenticationHelper.getInstance(getApplicationContext())
                .thenAccept(authHelper -> {
                    mAuthHelper = authHelper;
                    if (!mIsSignedIn) {
                        doSilentSignIn(false);
                    } else {
                        hideProgressBar();
                    }
                })
                .exceptionally(exception -> {
                    Log.e("AUTH", "Error creating auth helper", exception);
                    return null;
                });*/
        // </InitialLoginSnippet>
    }
    private void loadAccount(){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                setSignedInState(mIsSignedIn);
                openHomeFragment(mUserName);
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                  signOut();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
              Log.d("error",exception.toString());
            }
        });
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_IS_SIGNED_IN, mIsSignedIn);
        outState.putString(SAVED_USER_NAME, mUserName);
        outState.putString(SAVED_USER_EMAIL, mUserEmail);

    }

    // <OnNavItemSelectedSnippet>
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Load the fragment that corresponds to the selected item
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                openHomeFragment(mUserName);
                break;
            case R.id.sign_in:
                signIn();
                break;
            case R.id.sign_out:
                 signOut();
                break;

        }

        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }
    // </OnNavItemSelectedSnippet>

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showProgressBar()
    {
        FrameLayout container = findViewById(R.id.fragment_container);
        ProgressBar progressBar = findViewById(R.id.progressbar);
        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar()
    {
        FrameLayout container = findViewById(R.id.fragment_container);
        ProgressBar progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    // Update the menu and get the user's name and email
    private void setSignedInState(boolean isSignedIn) {
        mIsSignedIn = isSignedIn;

        mNavigationView.getMenu().clear();
        mNavigationView.inflateMenu(R.menu.activity_main_drawer);

        Menu menu = mNavigationView.getMenu();

        // Hide/show the Sign in, Calendar, and Sign Out buttons
        if (isSignedIn) {
            menu.removeItem(R.id.sign_in);
        } else {
            menu.removeItem(R.id.nav_home);

        }

        // Set the user name and email in the nav drawer
        TextView userName = mHeaderView.findViewById(R.id.username);
        TextView userEmail = mHeaderView.findViewById(R.id.userEmail);

        if (isSignedIn) {
            userName.setText(mUserName);
            userEmail.setText(mUserEmail);
        } else {
            mUserName = null;
            mUserEmail = null;
            mUserTimeZone = null;

            userName.setText("Please sign in");
            userEmail.setText("");
        }
    }

    // Load the "Home" fragment
    public void openHomeFragment(String userName) {
        HomeFragment fragment = HomeFragment.createInstance(userName);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
        mNavigationView.setCheckedItem(R.id.nav_home);
    }

    // Load the "Calendar" fragment

    // <SignInAndOutSnippet>
    private void signIn() {
        showProgressBar();
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.signIn(MainActivity.this,null,SCOPES,getAuthInteractiveCallback());

        // Attempt silent sign in first
        // if this fails, the callback will handle doing
        // interactive sign in
    }

    private void signOut() {
      //  mAuthHelper.signOut();
        if (mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
            @Override
            public void onSignOut() {
                setSignedInState(false);
                openHomeFragment(mUserName);

            }
            @Override
            public void onError(@NonNull MsalException exception){
               Log.d("error",exception.toString());
            }
        });

    }
    // </SignInAndOutSnippet>
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                /* Update UI */
               setSignedInState(mIsSignedIn);
                /* call graph */
                callGraphAPI(authenticationResult);

            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());

            }
            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }


    private SilentAuthenticationCallback getAuthSilentCallback() {
        return new SilentAuthenticationCallback() {
            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                Log.d(TAG, "Successfully authenticated");
                callGraphAPI(authenticationResult);

            }
            @Override
            public void onError(MsalException exception) {
                Log.d(TAG, "Authentication failed: " + exception.toString());

            }
        };
    }
    private void callGraphAPI(IAuthenticationResult authenticationResult) {

        final String accessToken = authenticationResult.getAccessToken();

        IGraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d(TAG, "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);
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
                       // DRIVEJSON = drive.getRawObject();
                        Log.d(TAG, "Found Drive " + drive.id);
                        Log.d(TAG,"json"+drive.getRawObject());

                        parsingJson(drive.getRawObject());
                       openHomeFragment(mUserName);
                    }

                    @Override
                    public void failure(ClientException ex) {
                        Log.d("error",ex.toString());

                    }
                });
    }
    private void parsingJson(@NonNull final JsonObject graphResponse){
        if(!graphResponse.equals(null)){
            JsonObject owner=graphResponse.getAsJsonObject("owner");
            JsonObject user = owner.getAsJsonObject("user");
            mUserName = user.get("displayName").toString();
            mUserEmail = user.get("mail").toString();

            // Intent intent = new Intent(MainActivity.this,BrowserTabActivity.class);
            // intent.putExtra("displayName",displayName);
            //  this.startActivity(intent);

        }else{
            Toast.makeText(MainActivity.this,"json empty",Toast.LENGTH_LONG).show();
        }


    }
    // Silently sign in - used if there is already a
    // user account in the MSAL cache
    //
    /*
    private void doSilentSignIn(boolean shouldAttemptInteractive) {
        mAuthHelper.acquireTokenSilently()
                .thenAccept(authenticationResult -> {
                    handleSignInSuccess(authenticationResult);
                })
                .exceptionally(exception -> {
                    // Check the type of exception and handle appropriately
                    Throwable cause = exception.getCause();
                    if (cause instanceof MsalUiRequiredException) {
                        Log.d("AUTH", "Interactive login required");
                        if (shouldAttemptInteractive) doInteractiveSignIn();
                    } else if (cause instanceof MsalClientException) {
                        MsalClientException clientException = (MsalClientException)cause;
                        if (clientException.getErrorCode() == "no_current_account" ||
                                clientException.getErrorCode() == "no_account_found") {
                            Log.d("AUTH", "No current account, interactive login required");
                            if (shouldAttemptInteractive) doInteractiveSignIn();
                        }
                    } else {
                        handleSignInFailure(cause);
                    }
                    hideProgressBar();
                    return null;
                });
    }

    // Prompt the user to sign in
    private void doInteractiveSignIn() {
        mAuthHelper.acquireTokenInteractively(this)
                .thenAccept(authenticationResult -> {
                    handleSignInSuccess(authenticationResult);
                })
                .exceptionally(exception -> {
                    handleSignInFailure(exception);
                    hideProgressBar();
                    return null;
                });
    }

    // <HandleSignInSuccessSnippet>
    /*
    // Handles the authentication result
    private void handleSignInSuccess(IAuthenticationResult authenticationResult) {
        // Log the token for debug purposes
        String accessToken = authenticationResult.getAccessToken();
        Log.d("AUTH", String.format("Access token: %s", accessToken));

        // Get Graph client and get user
        GraphHelper graphHelper = GraphHelper.getInstance();
        GraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(new IAuthenticationProvider() {
                            @Override
                            public void authenticateRequest(IHttpRequest request) {
                                Log.d("TAG", "Authenticating request," + request.getRequestUrl());
                                request.addHeader("Authorization", "Bearer " + accessToken);
                            }
                        })
                        .buildClient();



        graphHelper.getUser()
                .thenAccept(user -> {
                    mUserName = user.displayName;
                    mUserEmail = user.mail == null ? user.userPrincipalName : user.mail;
                    mUserTimeZone = user.mailboxSettings.timeZone;

                    runOnUiThread(() -> {
                        hideProgressBar();
                        setSignedInState(true);
                        openHomeFragment(mUserName);
                    });
                })
                .exceptionally(exception -> {
                    Log.e("AUTH", "Error getting /me", exception);

                    runOnUiThread(()-> {
                        hideProgressBar();
                        setSignedInState(false);
                    });

                    return null;
                });
    }
    // </HandleSignInSuccessSnippet>
/*
    private void handleSignInFailure(Throwable exception) {
        if (exception instanceof MsalServiceException) {
            // Exception when communicating with the auth server, likely config issue
            Log.e("AUTH", "Service error authenticating", exception);
        } else if (exception instanceof MsalClientException) {
            // Exception inside MSAL, more info inside MsalError.java
            Log.e("AUTH", "Client error authenticating", exception);
        } else {
            Log.e("AUTH", "Unhandled exception authenticating", exception);
        }
    }*/
}
