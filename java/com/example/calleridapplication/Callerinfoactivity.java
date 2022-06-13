package com.example.calleridapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.microsoft.aad.adal.AuthenticationContext;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CallerIdApplication.R;
import com.google.android.gms.common.api.Api;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.common.internal.net.HttpClient;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;

public class Callerinfoactivity extends AppCompatActivity {


    BottomSheetBehavior sheetBehavior;
   Button btnBottomSheet;
    View layoutBottom;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_callerinfoactivity);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        win.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        setContentView(com.example.CallerIdApplication.R.layout.activity_callerinfoactivity);

        btnBottomSheet=findViewById(R.id.btn);

        layoutBottom=findViewById(R.id.bottom_sheet);
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
    }
            //manually open sheet on button click
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
        }

