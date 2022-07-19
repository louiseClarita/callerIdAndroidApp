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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

//import com.example.azurefirstapp.databinding.ActivityMainBinding;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class first extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    String URL_GETALLCONTACTS = "https://getallcontacts20220530100450.azurewebsites.net/api/Function1?code=vQ39xN3XM4syuBk6ACNCDkUwpwAsS-EiiQi3Trc4028RAzFuOQbYKQ==";
    public static boolean isSignedIn = false;
    public static NavigationView navigationView;
    public static View mHeaderView;
    TextView signinStatus;
    TextView titles;
    public static Toolbar toolbar;
    public static Menu menu;
    private static final String TAG = callReciever.class.getSimpleName();
     DrawerLayout drawerLayout ;
     ImageView dehaze;
    public static ISingleAccountPublicClientApplication mSingleAccountApp;
    DataBaseHelper dataBaseHelper;
    DataBaseHelper3 dataBaseHelper3;
    DataBaseHelper4 dataBaseHelper4;

    ProgressBar progressBar;
    ImageView refresh;
    ImageView clear;
    public static boolean firsttoCreate=false;
    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.browsertab_activity);
        dataBaseHelper = new DataBaseHelper(first.this);
        dataBaseHelper3 = new DataBaseHelper3(first.this);
        initializeUI();
        checkOverlayPermission();
       signinStatus =findViewById(R.id.signinstatus);
       progressBar = (ProgressBar)findViewById(R.id.progressBarDrawer);
       refresh = findViewById(R.id.refreshing);
       titles = findViewById(R.id.titles);
       clear = findViewById(R.id.clearing);

       titles.setText("");
       refresh.setVisibility(View.GONE);
       AddCountryCodes();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALL_LOG},1);
        }

        this.stopService(new Intent(this, ForegroundService.class));
         startService2();
        if (getApplicationContext().checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission has not been granted, therefore prompt the user to grant permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        if (getApplicationContext().checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission has not been granted, therefore prompt the user to grant permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                    1);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }
        // startService();


        if(CallLogsAdapter.openCreate){
            Bundle bundle=first.this.getIntent().getExtras();
            CallLogsAdapter.openCreate=false;
           String ids = bundle.getString("id").trim();
            Log.d("idinfirst",ids);
            Log.d("open1","true");
            openCreateContactsFragment2(ids);
            return;
        }
       if(relatedCallLogs.openCallLogs) {
             openCallLogsFragment();
               return;
              }

              if(ContactsAdapter.openContactFrag){
                  Log.d("open2","true");
                  ContactsAdapter.openContactFrag=false;
                openContactFragment();

      //  if(callReciever.openCreate){
      //      Log.d("using","openCreate");

     //         openCreateContactsFragment();
                      return;
              }
       // openCallLogsFragment();
        //  openSignInFragment();
        /*
        if(Window.found){
            Log.d("using","window.found"+Window.found);
            Window.found=false;
            openCreateContactsFragment();
                 return;
        }


         */
           if(callReciever.openedOnNotFound){
               callReciever.openedOnNotFound=false;
               Log.d("using","openedOnNotFound");
             firsttoCreate=true;
             openCreateContactsFragment();
          return;
   // callReciever.openedOnNotFound=false;

                 }


/*
        PublicClientApplication.createSingleAccountPublicClientApplication(first.this, R.raw.auth_config_single_account,new IPublicClientApplication.ISingleAccountApplicationCreatedListener(){
            @Override
            public void onCreated(ISingleAccountPublicClientApplication application){

                if(first.this == null) Log.e("EMT","EMT");
                signinStatus.setVisibility(View.VISIBLE);
                mSingleAccountApp = application;
                signinStatus.setVisibility(View.GONE);
                openSignInFragment();
            }
            @Override
            public void onError(MsalException exception){
                signinStatus.setVisibility(View.VISIBLE);
                Toast.makeText(first.this,exception.toString(),Toast.LENGTH_LONG).show();

            }
        });
*/
        if(!(dataBaseHelper3.getCount() == 0)){

            showProgressBar();
            TextView userName = first.mHeaderView.findViewById(R.id.userName);
            TextView userEmail = first.mHeaderView.findViewById(R.id.userEmail);
            userName.setText(dataBaseHelper3.getUser().getName());
            userEmail.setText(dataBaseHelper3.getUser().getEmail());
            hideProgressBar();

            if (navigationView != null) {
                Log.d("tTAG","should change the title");
                 menu = navigationView.getMenu();
                menu.findItem(R.id.nav_signin).setTitle("Sign Out");
                //menu.findItem(R.id.nav_pkg_manage).setVisible(false);//In case you want to remove menu item
                //  navigationView.setNavigationItemSelectedListener(getActivity());
                try{
                    menu.findItem(R.id.nav_updateDB).setVisible(true);
                  //  menu.add(R.id.nav_updateDB);
                }catch(Exception e){
                    Log.d("TAG","Exception :"+e.toString());
                }
            }
        }else {
             menu = navigationView.getMenu();
            menu.findItem(R.id.nav_signin).setTitle("Sign In");
          //  menu.removeItem(R.id.nav_updateDB);
            menu.findItem(R.id.nav_updateDB).setVisible(false);
        }


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(first.this,"refreshing...",Toast.LENGTH_SHORT).show();
                showProgressBar();
                openCallLogsFragment();
            }
        });
        openCallLogsFragment();
    }

    private void AddCountryCodes() {
        List<CountryCode> countryCodeList = new ArrayList<>();
/**+1 –  Canada
 +1 –  United States, including United States territories:
 +1 340 –  United States Virgin Islands
 +1 670 –  Northern Mariana Islands
 +1 671 –  Guam
 +1 684 –  American Samoa
 +1 787 / 939 –  Puerto Rico
 +1 Many, but not all, Caribbean nations and some Caribbean Dutch and British Overseas Territories:
 +1 242 –  Bahamas
 +1 246 –  Barbados
 +1 264 –  Anguilla
 +1 268 –  Antigua and Barbuda
 +1 284 –  British Virgin Islands
 +1 345 –  Cayman Islands
 +1 441 –  Bermuda
 +1 473 –  Grenada
 +1 649 –  Turks and Caicos Islands
 +1 658 / 876 –  Jamaica
 +1 664 –  Montserrat
 +1 721 –  Sint Maarten
 +1 758 –  Saint Lucia
 +1 767 –  Dominica
 +1 784 –  Saint Vincent and the Grenadines
 +1 809 / 829 / 849 –  Dominican Republic
 +1 868 –  Trinidad and Tobago
 +1 869 –  Saint Kitts and Nevis

 Zone 2: Mostly Africa
 (but also Aruba, Faroe Islands, Greenland and British Indian Ocean Territory)

 +20 –  Egypt
 +210 – unassigned
 +211 –  South Sudan
 +212 –  Morocco (including Western Sahara)
 +213 –  Algeria
 +214 – unassigned
 +215 – unassigned
 +216 –  Tunisia
 +217 – unassigned
 +218 –  Libya
 +219 – unassigned
 +220 –  Gambia
 +221 –  Senegal
 +222 –  Mauritania
 +223 –  Mali
 +224 –  Guinea
 +225 –  Ivory Coast
 +226 –  Burkina Faso
 +227 –  Niger
 +228 –  Togo
 +229 –  Benin
 +230 –  Mauritius
 +231 –  Liberia
 +232 –  Sierra Leone
 +233 –  Ghana
 +234 –  Nigeria
 +235 –  Chad
 +236 –  Central African Republic
 +237 –  Cameroon
 +238 –  Cape Verde
 +239 –  São Tomé and Príncipe
 +240 –  Equatorial Guinea
 +241 –  Gabon
 +242 –  Republic of the Congo
 +243 –  Democratic Republic of the Congo
 +244 –  Angola
 +245 –  Guinea-Bissau
 +246 –  British Indian Ocean Territory
 +247 –  Ascension Island
 +248 –  Seychelles
 +249 –  Sudan
 +250 –  Rwanda
 +251 –  Ethiopia
 +252 –  Somalia
 +253 –  Djibouti
 +254 –  Kenya
 +255 –  Tanzania
 +255 24 –  Zanzibar, in place of never-implemented +259
 +256 –  Uganda
 +257 –  Burundi
 +258 –  Mozambique
 +259 – unassigned (was intended for People's Republic of Zanzibar but never implemented – see +255 Tanzania)
 +260 –  Zambia
 +261 –  Madagascar
 +262 –  Réunion
 +262 269 / 639 –  Mayotte (formerly at +269 Comoros)
 +263 –  Zimbabwe
 +264 –  Namibia (formerly +27 6x as South West Africa)
 +265 –  Malawi
 +266 –  Lesotho
 +267 –  Botswana
 +268 –  Eswatini
 +269 –  Comoros (formerly assigned to Mayotte, now at +262)
 +27 –  South Africa
 +28x – unassigned (reserved for country code expansion)[1]
 +290 –  Saint Helena
 +290 8 –  Tristan da Cunha
 +291 –  Eritrea
 +292 – unassigned
 +293 – unassigned
 +294 – unassigned
 +295 – unassigned (formerly assigned to San Marino, now at +378)
 +296 – unassigned
 +297 –  Aruba
 +298 –  Faroe Islands
 +299 –  Greenland

 Zones 3–4: Europe
 See also: Telephone numbers in Europe
 Originally, larger countries such as Spain, the United Kingdom and France were assigned two-digit codes to compensate for their usually longer domestic numbers. Small countries, such as Iceland, were assigned three-digit codes. Since the 1980s, all new assignments have been three-digit regardless of countries' populations.

 +30 –  Greece
 +31 –  Netherlands
 +32 –  Belgium
 +33 –  France
 +34 –  Spain
 +350 –  Gibraltar
 +351 –  Portugal
 +351 291 –  Madeira (landlines only)
 +351 292 –  Azores (landlines only, Horta, Azores area)
 +351 295 –  Azores (landlines only, Angra do Heroísmo area)
 +351 296 –  Azores (landlines only, Ponta Delgada and São Miguel Island area)
 +352 –  Luxembourg
 +353 –  Ireland
 +354 –  Iceland
 +355 –  Albania
 +356 –  Malta
 +357 –  Cyprus (including  Akrotiri and Dhekelia)
 +358 –  Finland
 +358 18 –  Åland
 +359 –  Bulgaria
 +36 –  Hungary (formerly assigned to Turkey, now at +90)
 +37 – unassigned (formerly assigned to East Germany until its reunification with West Germany, now part of +49 Germany)
 +370 –  Lithuania (formerly +7 012 as Lithunaian SSR)
 +371 –  Latvia (formerly +7 013 as Latvian SSR)
 +372 –  Estonia (formerly +7 014 as Estonian SSR)
 +373 –  Moldova (formerly +7 042 as Moldavian SSR)
 +374 –  Armenia (formerly +7 885 as Armenian SSR)
 +374 47 –  Artsakh (landlines, formerly +7 893)
 +374 97 –  Artsakh (mobile phones)
 +375 –  Belarus
 +376 –  Andorra (formerly +33 628)
 +377 –  Monaco (formerly +33 93)
 +378 –  San Marino (interchangeably with +39 549; earlier was allocated +295 but never used)
 +379 –   Vatican City (assigned but uses +39 06698).
 +38 – unassigned (formerly assigned to Yugoslavia until its break-up in 1991)
 +380 –  Ukraine
 +381 –  Serbia
 +382 –  Montenegro
 +383 –  Kosovo
 +384 – unassigned
 +385 –  Croatia
 +386 –  Slovenia
 +387 –  Bosnia and Herzegovina
 +388 – unassigned (formerly assigned to the European Telephony Numbering Space)[1][2]
 +389 –  North Macedonia
 +39 –  Italy
 +39 06 698 –   Vatican City (assigned +379 but not in use)
 +39 0549 –  San Marino (interchangeably with +378)
 +40 –  Romania
 +41 –   Switzerland
 +41 91 – Flag of Campione d'Italia.svg Campione d'Italia, an Italian enclave
 +42 – unassigned (formerly assigned to Czechoslovakia until its breakup in 1993)
 +420 –  Czech Republic
 +421 –  Slovakia
 +422 – unassigned
 +423 –  Liechtenstein (formerly +41 75)
 +424 – unassigned
 +425 – unassigned
 +426 – unassigned
 +427 – unassigned
 +428 – unassigned
 +429 – unassigned
 +43 –  Austria
 +44 –  United Kingdom
 +44 1481 –  Guernsey
 +44 1534 –  Jersey
 +44 1624 –  Isle of Man
 +45 –  Denmark
 +46 –  Sweden
 +47 –  Norway
 +47 79 –  Svalbard
 +48 –  Poland
 +49 –  Germany

 Zone 5: Americas outside the NANP
 +500 –  Falkland Islands
 +500 x –  South Georgia and the South Sandwich Islands
 +501 –  Belize
 +502 –  Guatemala
 +503 –  El Salvador
 +504 –  Honduras
 +505 –  Nicaragua
 +506 –  Costa Rica
 +507 –  Panama
 +508 –  Saint-Pierre and Miquelon
 +509 –  Haiti
 +51 –  Peru
 +52 –  Mexico
 +53 –  Cuba
 +54 –  Argentina
 +55 –  Brazil
 +56 –  Chile
 +57 –  Colombia
 +58 –  Venezuela
 +590 –  Guadeloupe (including Saint Barthélemy, Saint Martin)
 +591 –  Bolivia
 +592 –  Guyana
 +593 –  Ecuador
 +594 –  French Guiana
 +595 –  Paraguay
 +596 –  Martinique (formerly assigned to Peru, now at +51)
 +597 –  Suriname
 +598 –  Uruguay
 +599 – Former  Netherlands Antilles, now grouped as follows:
 +599 3 –  Sint Eustatius
 +599 4 –  Saba
 +599 5 – unassigned (formerly assigned to Sint Maarten, now included in NANP as +1 721)
 +599 7 –  Bonaire
 +599 8 – unassigned (formerly assigned to Aruba, now at +297)
 +599 9 –  Curaçao

 Zone 6: Southeast Asia and Oceania
 +60 –  Malaysia
 +61 –  Australia (see also +672 below)
 +61 8 9162 –  Cocos Islands
 +61 8 9164 –  Christmas Island
 +62 –  Indonesia
 +63 –  Philippines
 +64 –  New Zealand
 +64 xx –  Pitcairn Islands
 +65 –  Singapore
 +66 –  Thailand
 +670 –  East Timor (formerly +62 39 during the Indonesian occupation); formerly assigned to Northern Mariana Islands, now included in NANP as +1-670 (See Zone 1, above)
 +671 – unassigned (formerly assigned to Guam, now included in NANP as +1 671)
 +672 – Australian External Territories (see also +61 Australia above); formerly assigned to Portuguese Timor (see +670)
 +672 1x – Australia Australian Antarctic Territory
 +672 3 –  Norfolk Island
 +673 –  Brunei
 +674 –  Nauru
 +675 –  Papua New Guinea
 +676 –  Tonga
 +677 –  Solomon Islands
 +678 –  Vanuatu
 +679 –  Fiji
 +680 –  Palau
 +681 –  Wallis and Futuna
 +682 –  Cook Islands
 +683 –  Niue
 +684 – unassigned (formerly assigned to American Samoa, now included in NANP as +1 684)
 +685 –  Samoa
 +686 –  Kiribati
 +687 –  New Caledonia
 +688 –  Tuvalu
 +689 –  French Polynesia
 +690 –  Tokelau
 +691 –  Federated States of Micronesia
 +692 –  Marshall Islands
 +693 – unassigned
 +694 – unassigned
 +695 – unassigned
 +696 – unassigned
 +697 – unassigned
 +698 – unassigned
 +699 – unassigned

 Zone 7: Russia and neighboring countries
 +7 –  Russia (formerly assigned to the Soviet Union until its dissolution in 1991)
 +7 840 / 940 –  Abkhazia (interchangeably with +995 44)
 +7 850 / 9298 –  South Ossetia (interchangeably with +995 34)
 +7 6xx / 7xx –  Kazakhstan (planned transition to +997 from 2023)

 Zone 8: East Asia and special services
 +800 – Universal International Freephone Service (UIFN)
 +801 – unassigned
 +802 – unassigned
 +803 – unassigned
 +804 – unassigned
 +805 – unassigned
 +806 – unassigned
 +807 – unassigned
 +808 – Universal International Shared Cost Service (UISC)
 +809 – unassigned
 +81 –  Japan
 +82 –  South Korea
 +83x – unassigned (reserved for country code expansion)[1]
 +84 –  Vietnam
 +850 –  North Korea
 +851 – unassigned
 +852 –  Hong Kong
 +853 –  Macau
 +854 – unassigned
 +855 –  Cambodia
 +856 –  Laos
 +857 – unassigned (formerly ANAC satellite service)
 +858 – unassigned (formerly ANAC satellite service)
 +859 – unassigned
 +86 –  China
 +870 – Inmarsat "SNAC" service
 +871 – unassigned (formerly assigned to Inmarsat Atlantic East, discontinued in 2008)
 +872 – unassigned (formerly assigned to Inmarsat Pacific, discontinued in 2008)
 +873 – unassigned (formerly assigned to Inmarsat Indian, discontinued in 2008)
 +874 – unassigned (formerly assigned to Inmarsat Atlantic West, discontinued in 2008)
 +875 – unassigned (reserved for future maritime mobile service)
 +876 – unassigned (reserved for future maritime mobile service)
 +877 – unassigned (reserved for future maritime mobile service)
 +878 – Universal Personal Telecommunications Service (UPTS)
 +879 – unassigned (reserved for national non-commercial purposes)
 +880 –  Bangladesh
 +881 – Global Mobile Satellite System
 +882 – International Networks
 +883 – International Networks
 +884 – unassigned
 +885 – unassigned
 +886 –  Taiwan
 +887 – unassigned
 +888 – unassigned[3] (was Telecommunications for Disaster Relief by OCHA)
 +889 – unassigned
 +89x – unassigned (reserved for country code expansion)[1]

 Zone 9: Mostly Middle East and parts of southern Asia
 +90 –  Turkey
 +90 392 –  Northern Cyprus
 +91 –  India
 +92 –  Pakistan
 +92 581 –  Gilgit Baltistan[4]
 +92 582 –  Azad Kashmir[4]
 +93 –  Afghanistan
 +94 –  Sri Lanka
 +95 –  Myanmar
 +960 –  Maldives
 +961 –  Lebanon
 +962 –  Jordan
 +963 –  Syria
 +964 –  Iraq
 +965 –  Kuwait
 +966 –  Saudi Arabia
 +967 –  Yemen
 +968 –  Oman
 +969 – unassigned (formerly assigned to South Yemen until its unification with North Yemen, now part of +967 Yemen)
 +970 –  Palestine
 +971 –  United Arab Emirates
 +972 –  Israel
 +973 –  Bahrain
 +974 –  Qatar
 +975 –  Bhutan
 +976 –  Mongolia
 +977 –    Nepal
 +978 – unassigned (formerly assigned to Dubai, now part of +971 United Arab Emirates)
 +979 – Universal International Premium Rate Service (UIPRS); (formerly assigned to Abu Dhabi, now part of +971 United Arab Emirates)
 +98 –  Iran
 +990 – unassigned
 +991 – International Telecommunications Public Correspondence Service trial (ITPCS)
 +992 –  Tajikistan
 +993 –  Turkmenistan
 +994 –  Azerbaijan
 +995 –  Georgia
 +995 34 –  South Ossetia (interchangeably with +7 850, +7 9298)
 +995 44 –  Abkhazia[5][6] (interchangeably with +7 840, +7 940)
 +996 –  Kyrgyzstan
 +997 –  Kazakhstan from 2023 (currently using +7 6xx / 7xx)
 +998 –  Uzbekistan**/
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("United States","+1"));
        countryCodeList.add(new CountryCode("Egypt","+20"));
        countryCodeList.add(new CountryCode("Lybya","+218"));
        countryCodeList.add(new CountryCode("France","+33"));
        countryCodeList.add(new CountryCode("Spain","+34"));
        countryCodeList.add(new CountryCode("Belgium","+32"));
        countryCodeList.add(new CountryCode("Luxembourg","+352"));
        countryCodeList.add(new CountryCode("Ireland","+353"));
        countryCodeList.add(new CountryCode("Cyprus","+357"));
        countryCodeList.add(new CountryCode("Italia","+39"));
        countryCodeList.add(new CountryCode("Germany","+49"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));
        countryCodeList.add(new CountryCode("Lebanon","+961"));

for(int i=0;i<countryCodeList.size();i++){





}
    }


    private void initializeUI(){
       drawerLayout = findViewById(R.id.drawerLayout);
       dehaze = findViewById(R.id.ImageMenu);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
     switch(item.getItemId()){
      //   case R.id.mainFragment:
      ///       openMainFragment();
       //      break;

         case R.id.nav_signin:
             item.setChecked(true);
             if(signin_fragment.is_signedin){
                 item.setTitle("Sign out");
             }
             openSignInFragment();

             break;
     //    case R.id.log_calls:
     //        fetchLogs();
     //        break;
      case R.id.nav_contacts:
          item.setChecked(true);
        //  showProgressBar();
       //   fetchAllContacts();
            openContactFragment();
           break;

         case R.id.nav_updateDB:
             item.setChecked(true);
             titles.setText("Updating...");
             showProgressBar();
             fetchAllContacts();
             openContactFragment();
             break;

         case R.id.phonecalls:
             item.setChecked(true);

             openCallLogsFragment();
             break;
            }
       drawerLayout.closeDrawer(GravityCompat.START);

        return true;
     }


    private void openCallLogsFragment() {
        //toolbar.setTitle("Phone Calls");
       titles.setText("Phone calls");
        refresh.setVisibility(View.VISIBLE);
        navigationView.setCheckedItem(R.id.opensavelogs);

        CallLogsFrag frag = CallLogsFrag.newInstance();
        first.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBar();
            }
        });

     //   navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();


    }


    private  void openCreateContactsFragment() {
        titles.setText("Create Contact");
        refresh.setVisibility(View.GONE);

        createContact frag = createContact.newInstance();

    //    navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
     //   navigationView.setItemIconTintList(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

    }
    private  void openCreateContactsFragment2(String id) {
        titles.setText("Create Contact");
        refresh.setVisibility(View.GONE);
        createContact2 frag = createContact2.newInstance(id);

      //  navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

    }
    private void openContactFragment() {
        titles.setText("Contacts");
        refresh.setVisibility(View.GONE);
        navigationView.setCheckedItem(R.id.nav_contacts);
        Contacts frag = Contacts.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
    //    navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));

    }

    private void openBottomFragment() {


      /*  Intent i = new Intent(first.this,Callerinfoactivity.class);
        this.startActivity(i);*/

        ///bottomSheetFrag frag = bottomSheetFrag.newInstance();
      ////  getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();
       // navigationView.setCheckedItem(R.id.nav_bottom);
    }

    private void openSignInFragment() {
        titles.setText("");
        refresh.setVisibility(View.GONE);
        navigationView.setCheckedItem(R.id.nav_signin);
        signin_fragment frag = signin_fragment.newInstance();
 //       navigationView.setItemTextColor(ColorStateList.valueOf(first.this.getResources().getColor(R.color.blue)));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,frag).commit();

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
                //    Toast.makeText(first.this,"build version akbar mn .O",Toast.LENGTH_LONG).show();
                    startForegroundService(new Intent(this, ForegroundService.class));
                } else {
               //     Toast.makeText(first.this,"build version azghar mn .O",Toast.LENGTH_LONG).show();
                    startService(new Intent(first.this, ForegroundService.class));
                }
            }
        }else{
         //   Toast.makeText(first.this,"build version azghar mn .M",Toast.LENGTH_LONG).show();
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
           //         Toast.makeText(getBaseContext(),  dateString , Toast.LENGTH_SHORT).show(); // you can use strings in this line
                }
            }
            c.close();
        }
    }

    private void fetchAllContacts() {





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
                .header("filter","firstname DESC")
               // .get()
                //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okhttp1",e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.e("okhttp2",responseBody);
                    hideProgressBar();
                    //Toast.makeText(first.this,"not successful:"+responseBody,Toast.LENGTH_LONG).show();

                    throw new IOException("Unexpected code " + response);
                }else {
                    String responseBody = response.body().string();
                    Log.d("strr->>0",responseBody);
                    parseJSON(responseBody);

                }
            }


        });
    }

/*
*
* OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{" +
                        "\r\n\"firstname\": \""+firstname.toUpperCase().trim().charAt(0)+""+firstname.substring(1).toLowerCase().trim()+"\"\r\n," +
                        "\r\n \"lastname\": \""+lastname.toUpperCase().trim()+"\"\r\n," +
                        "\r\n    \"cr051_companyname\": \""+company.trim()+"\"\r\n," +
                        "\r\n    \"emailaddress1\": \""+email.trim()+"\"\r\n," +
                        "\r\n    \"jobtitle\": \""+job.trim()+"\"\r\n," +
                        "\r\n    \"mobilephone\": \""+mobilephone.trim()+"\"\r\n}");

                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://calleridcrmapi.azure-api.net/contacts")
                        .method("POST", body)
                        .addHeader("Content-Type", "application/json")
                        //.addHeader("Cookie", "ReqClientId=30c78179-6c3a-4708-8376-907a89493c54; last_commit_time=2022-05-31 12:54:18Z; orgId=8b7545a7-1d2b-48d7-be9d-832648fff0e3")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("okhttp1",e.toString());
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {

                            Log.e("okhttp2",response.toString());

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    createStatus.setTextColor(Integer.parseInt("#eb4b4b"));
                                    createStatus.setText("unsuccessfull!");
                                    btnCreateContact.setEnabled(false);
                                }
                            });

                            throw new IOException("Unexpected code " + response);
                        }else{
                            //  Toast.makeText(getActivity(),"sucess",Toast.LENGTH_LONG).show();

                            Log.e("okhttp2",response.toString());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    hideProgressBar();
                                    ETfirstname.setText("");
                                    ETlastname.setText("");
                                    ETcompany.setText("");
                                    ETjob.setText("");
                                    ETemail.setText("");
                                    ETphonenumber.setText("");
                                    btnCreateContact.setText("saved!");
                                    btnCreateContact.setEnabled(true);

                                    createStatus.setText("added successfully!");
                                    cancel.setText("back");
                                    addthisContactToDB(mobilephone);

                                }
                            });

                            Log.d("create:","success");
                        }

                        // you code to handle response
                    }

                });
            }
        });
*
* */
/*
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
                        hideProgressBar();
                        Toast.makeText(first.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("errroor",">>"+error.toString());
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
        }
*/


    private void parseJSON(String response) {

        String firstname,lastname,contactid,jobTitle,company,email,mobilephone;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int size = 500;
            //   if(jsonObject.getString("status").equals("true")){
            JSONArray callerid = jsonObject.getJSONArray("value");
            //  for (int i = 0; i < callerid.length(); i++) {
            for (int i = callerid.length()-1; i > callerid.length() - 501; i--) {
             //    for (int i = 0; i < size; i++) {
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
        Log.d("stopppped...","updating");
        hideProgressBar();
    }
    public void startService2(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if the user has already granted$^$
            // the Draw over other apps permission
            if(Settings.canDrawOverlays(this)) {
                // start the service based on the android version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                  //  Toast.makeText(first.this,"build version akbar mn .O",Toast.LENGTH_LONG).show();
                    startForegroundService(new Intent(first.this, ForegroundService2.class));
                } else {
                //   Toast.makeText(first.this,"build version azghar mn .O",Toast.LENGTH_LONG).show();
                    startService(new Intent(first.this, ForegroundService2.class));
                }
            }
        }else{
          //  Toast.makeText(first.this,"build version azghar mn .M",Toast.LENGTH_LONG).show();
            startService(new Intent(first.this, ForegroundService2.class));
        }
    }

    private void showProgressBar() {
        first.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                first.this.findViewById(R.id.progressBarDrawer)
                        .setVisibility(View.VISIBLE);
                first.this.findViewById(R.id.fragment_container)
                        .setEnabled(false);
                first.this.findViewById(R.id.fragment_container)
                        .setVisibility(View.INVISIBLE);
            }
        });


    }

    private void hideProgressBar() {
        first.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                first.this.findViewById(R.id.progressBarDrawer)
                        .setVisibility(View.GONE);
                first.this.findViewById(R.id.fragment_container)
                        .setEnabled(true);
                first.this.findViewById(R.id.fragment_container)
                        .setVisibility(View.VISIBLE);
            }
        });


    }
}



