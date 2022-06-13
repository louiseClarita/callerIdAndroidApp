package com.example.calleridapplication;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

//import com.example.azurefirstapp.databinding.ActivityMainBinding;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.CallerIdApplication.R;
import com.example.CallerIdApplication.R.color;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.concurrency.ICallback;
import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.http.IHttpRequest;
import com.microsoft.graph.models.extensions.Drive;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IContactCollectionPage;
import com.microsoft.identity.client.*;
import com.microsoft.identity.client.exception.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class first extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String URL_GETALLCONTACTS = "https://getallcontacts20220530100450.azurewebsites.net/api/Function1?code=vQ39xN3XM4syuBk6ACNCDkUwpwAsS-EiiQi3Trc4028RAzFuOQbYKQ==";
    public static boolean isSignedIn = false;
    NavigationView navigationView;
    public static View mHeaderView;
    private static final String TAG = callReciever.class.getSimpleName();
     DrawerLayout drawerLayout ;
     ImageView dehaze;
    DataBaseHelper dataBaseHelper;

    public static boolean firsttoCreate=false;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsertab_activity);
        dataBaseHelper = new DataBaseHelper(first.this);
        initializeUI();
        checkOverlayPermission();


        this.stopService(new Intent(this, ForegroundService.class));
         startService2();

        // startService();
   if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED  ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
     if( ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
        != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.READ_PHONE_NUMBERS},1);
    }


         if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
              != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_NETWORK_STATE},1);
                        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},1);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
               == PackageManager.PERMISSION_GRANTED ){
            fetchLogs();
        }


        if(CallLogsAdapter.openCreate){
            Bundle bundle=first.this.getIntent().getExtras();
           String ids = bundle.getString("id").trim();
            Log.d("idinfirst",ids);

            openCreateContactsFragment2(ids);

        }
if(ContactsAdapter.openContactFrag){

    openContactFragment();
    ContactsAdapter.openContactFrag=false;
      //  if(callReciever.openCreate){
      //      Log.d("using","openCreate");

     //         openCreateContactsFragment();

       }
       // openCallLogsFragment();
        //  openSignInFragment();
        if(com.example.calleridapplication.Window.found){
            Log.d("using","window.found");
            Window.found=false;
            openCreateContactsFragment();

        }

if(callReciever.openedOnNotFound){

   Log.d("using","openedOnNotFound");
    firsttoCreate=true;
    openCreateContactsFragment();

   // callReciever.openedOnNotFound=false;

}
    }



    private void initializeUI(){
       drawerLayout = findViewById(R.id.drawerLayout);
       dehaze = findViewById(R.id.ImageMenu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                (R.string.open_drawer), (R.string.Close_navigation_drawer));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.NavigationView);
        mHeaderView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);




      navigationView.setItemIconTintList(null);
/**
        NavController navController = Navigation.findNavController(this,R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView,navController);
       navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
           @Override
           public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
               Toast.makeText(getBaseContext(),navDestination.getLabel(),Toast.LENGTH_LONG).show();
           }
       });**/
         }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
     switch(item.getItemId()){
      //   case R.id.mainFragment:
      ///       openMainFragment();
       //      break;
         case R.id.nav_signin:
             openSignInFragment();
             break;
     //    case R.id.log_calls:
     //        fetchLogs();
     //        break;
      case R.id.nav_contacts:
            openContactFragment();
           break;
         case R.id.nav_createContacts:
             openCreateContactsFragment();
             break;
         case R.id.nav_updateDB:
             fetchAllContacts();
             break;
         case R.id.phonecalls:
             openCallLogsFragment();
             break;
            }
       drawerLayout.closeDrawer(GravityCompat.START);

        return true;
     }

    private void openCallLogsFragment() {
        CallLogsFrag frag = CallLogsFrag.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
        navigationView.setCheckedItem(R.id.opensavelogs);

    }

    @SuppressLint("ResourceAsColor")
    private  void openCreateContactsFragment() {
        createContact frag = createContact.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
        navigationView.setCheckedItem(R.id.nav_createContacts);
    }
    private  void openCreateContactsFragment2(String id) {
        createContact2 frag = createContact2.newInstance(id);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
        navigationView.setCheckedItem(R.id.nav_createContacts);
    }
    private void openContactFragment() {
        Contacts frag = Contacts.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
        navigationView.setCheckedItem(R.id.nav_contacts);
    }

    private void openBottomFragment() {


      /*  Intent i = new Intent(first.this,Callerinfoactivity.class);
        this.startActivity(i);*/

        ///bottomSheetFrag frag = bottomSheetFrag.newInstance();
      ////  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
       // navigationView.setCheckedItem(R.id.nav_bottom);
    }

    private void openSignInFragment() {
        signin_fragment frag = signin_fragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
        navigationView.setCheckedItem(R.id.nav_signin);
    }

    private void openMainFragment() {
        MainFragment frag = MainFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
   //     navigationView.setCheckedItem(R.id.mainFragment);

    }

    public void checkOverlayPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                // send user to the device settings
                Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(myIntent);
            }
        }
    }
    public void startService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
           if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Toast.makeText(first.this,"build version akbar mn .O",Toast.LENGTH_LONG).show();
                    startForegroundService(new Intent(this, ForegroundService.class));
                } else {
                    Toast.makeText(first.this,"build version azghar mn .O",Toast.LENGTH_LONG).show();
                    startService(new Intent(first.this, ForegroundService.class));
                }
            }
        }else{
            Toast.makeText(first.this,"build version azghar mn .M",Toast.LENGTH_LONG).show();
            startService(new Intent(first.this, ForegroundService.class));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    //    startService();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void fetchLogs(){


        ContentResolver cr = getBaseContext().getContentResolver();
        Cursor c = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, null);

        int totalCall = 10;

        if (c != null) {
            totalCall = 1; // intenger call log limit

            if (c.moveToLast()) { //starts pulling logs from last - you can use moveToFirst() for first logs
                for (int j = 0; j < totalCall; j++) {


                    String phNumber = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER));
                    String callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE));
                    String callDuration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION));
                    Date dateFormat= new Date(Long.valueOf(callDate));
                    String callDayTimes = String.valueOf(dateFormat);
                    SimpleDateFormat dt = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss");


                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String dateString = formatter.format(new Date(Long
                            .parseLong(callDate)));
                    String date=null;

                      // date = String.valueOf(dt.format(dateString));

                    //String date=null;
                   // String oldstring = "2011-01-18 00:00:00.0";
                   // LocalDateTime datetime = LocalDateTime.parse(callDayTimes, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
                      //  date = dt.parse(callDate);
                  //  String newstring = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                 //   System.out.println("new date string isss:"+newstring); // 2011
                    //  date =   dt.format(callDate);

                   // String dateString = String.valueOf(date);
                    String direction = null;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)))) {
                        case CallLog.Calls.OUTGOING_TYPE:
                            direction = "OUTGOING";
                            break;
                        case CallLog.Calls.INCOMING_TYPE:
                            direction = "INCOMING";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            direction = "MISSED";
                            break;
                        default:
                            break;
                    }

                    //  c.moveToPrevious(); // if you used moveToFirst() for first logs, you should this line to moveToNext

                    //  //Toast.makeText(getBaseContext(), phNumber + callDuration + callDate + direction, Toast.LENGTH_SHORT).show(); // you can use strings in this line
                    Toast.makeText(getBaseContext(),  dateString , Toast.LENGTH_SHORT).show(); // you can use strings in this line
                }
            }
            c.close();
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
                        Toast.makeText(first.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("errroor",">>"+error.toString());
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                mobilephone = dataobj.getString("mobilephone");

                ContactModel c11 = new ContactModel(contactid,lastname,firstname,company,jobTitle,email,mobilephone);
                Log.d("contact model:",c11.toString());
                dataBaseHelper.addOne(c11);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void startService2(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Toast.makeText(first.this,"build version akbar mn .O",Toast.LENGTH_LONG).show();
                    startForegroundService(new Intent(first.this, ForegroundService2.class));
                } else {
                    Toast.makeText(first.this,"build version azghar mn .O",Toast.LENGTH_LONG).show();
                    startService(new Intent(first.this, ForegroundService2.class));
                }
            }
        }else{
            Toast.makeText(first.this,"build version azghar mn .M",Toast.LENGTH_LONG).show();
            startService(new Intent(first.this, ForegroundService2.class));
        }
    }
}



