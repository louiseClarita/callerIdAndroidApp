package com.example.calleridapplication;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CallerIdApplication.R;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalException;

public class SplashActivity extends AppCompatActivity {
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    DataBaseHelper dataBaseHelper;
    DataBaseHelper2 dataBaseHelper2;
    DataBaseHelper3 dataBaseHelper3;
    static int TIMEOUT_MILLIS = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dataBaseHelper = new DataBaseHelper(SplashActivity.this);
        dataBaseHelper2 = new DataBaseHelper2(SplashActivity.this);
        dataBaseHelper3 = new DataBaseHelper3(SplashActivity.this);

        if(mSingleAccountApp==null){
            PublicClientApplication.createSingleAccountPublicClientApplication(SplashActivity.this, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
                @Override
                public void onCreated(ISingleAccountPublicClientApplication application){

                    if(SplashActivity.this == null) Log.e("EMT","EMT");

                    mSingleAccountApp = application;

                    if(mSingleAccountApp!=null){
                        Log.d("Tag","entereed againnn");
                        loadAccount();


                    }

                }
                @Override
                public void onError(MsalException exception){
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("TAG",exception.toString());


                            Toast.makeText(SplashActivity.this,"error!please try again later!",Toast.LENGTH_LONG).show();
                            return;
                        }
                    });

                    //  displayError(exception);

                }
            });
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, first.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, TIMEOUT_MILLIS);
    }



    private void loadAccount(){
        if(mSingleAccountApp == null){
            return;
        }
        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback(){
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount){
                return;
            }
            //   showProgressBar();
            //       mSingleAccountApp.acquireTokenSilentAsync(SCOPES, AUTHORITY, getAuthSilentCallback());



            //is_signedin=true;
            //   }
            //  openBrowserTabActivity();



            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount,@Nullable IAccount currentAccount){
                if(currentAccount == null){
                    SplashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            dataBaseHelper3.deleteUser();
                            if(dataBaseHelper.deleteDB()==0){
                                Toast.makeText(SplashActivity.this,"Account Changed!\nerror signing out,try again later",Toast.LENGTH_LONG).show();
                                return;
                            }

                            performOperationOnSignOut();
                        }
                    });
                    //      openBrowserTabActivity();
                }
            }
            @Override
            public void onError(@NonNull MsalException exception){
                SplashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(SplashActivity.this,"Warning!No internet!some functions will be unavailable!",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private void performOperationOnSignOut() {
        SplashActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mSingleAccountApp == null){
                    return;
                }
                mSingleAccountApp.signOut(new ISingleAccountPublicClientApplication.SignOutCallback() {
                    @Override
                    public void onSignOut() {
                        if (first.navigationView != null) {
                            Log.d("tTAG","should change the title");
                            Menu menu = first.navigationView.getMenu();
                            menu.findItem(R.id.nav_signin).setTitle("Sign in");
                            first.navigationView.setEnabled(true);
                            first.toolbar.setEnabled(true);
                            // graphData.setText("");
                            //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                            //  navigationView.setNavigationItemSelectedListener(getActivity());
                        }

                        performOperationOnSignOut();

                    }
                    @Override
                    public void onError(@NonNull MsalException exception){


                        Toast.makeText(SplashActivity.this,"error!try again later!",Toast.LENGTH_LONG).show();
                    }
                });

                if(dataBaseHelper3.deleteUser()==1) {
                    if(dataBaseHelper.deleteDB()==0){
                        Toast.makeText(SplashActivity.this,"error signing out,try again later",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Toast.makeText(SplashActivity.this,"signed out",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SplashActivity.this,"error signing out",Toast.LENGTH_LONG).show();
                    return;
                }

                Menu menu = first.navigationView.getMenu();
                menu.findItem(R.id.nav_signin).setTitle("Sign in");
                menu.findItem(R.id.nav_updateDB).setVisible(false);
                Toast.makeText(SplashActivity.this,"please sign in",Toast.LENGTH_LONG).show();

                final String signOutText = "Signed Out.";
                TextView userName = first.mHeaderView.findViewById(R.id.userName);
                TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);

                userName.setText("username");
                userEmail.setText("username@org.onmicrosoft.com");


                Toast.makeText(SplashActivity.this, signOutText, Toast.LENGTH_SHORT)
                        .show();



            }
        });
    }

}