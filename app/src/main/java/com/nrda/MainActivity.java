package com.nrda;
/**
 * Created by Diwash Choudhary on 20-07-2017.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nrda.database.DatabaseRoute;
import com.nrda.services.RouteCoordinate;
import com.nrda.smart_card.SmartCardDashBoard;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks{
    private NavigationView navigationView;
    private TextView tool_title, txt_footerMsg;
    LinearLayout term_condition,star_us,more,half_pie;
    private RelativeLayout route_eta, route_info, bus_timing, bus_fare, live_track, nearstop, emergency, tourguide, aboutus, suggestion;
    private AlertDialog internetDialog;
    private boolean isNetDialogShowing = false;
    DatabaseRoute databaseRoute ;
    ImageView Img_lang, logo;
    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseRoute = new DatabaseRoute(getApplicationContext());
         manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitleTextColor(Color.WHITE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        term_condition           = (LinearLayout) findViewById(R.id.pie_chart);
        //star_us                 = (LinearLayout) findViewById(R.id.star_us);
       // more                    = (LinearLayout) findViewById(R.id.more);
        half_pie                 = (LinearLayout) findViewById(R.id.half_pie);

        route_eta                = (RelativeLayout) findViewById(R.id.route_eta);
        route_info               = (RelativeLayout) findViewById(R.id.route_info);
        bus_timing               = (RelativeLayout) findViewById(R.id.bus_timing);
        bus_fare                 = (RelativeLayout) findViewById(R.id.bus_fare);
        live_track               = (RelativeLayout) findViewById(R.id.live_track);
        nearstop                 = (RelativeLayout) findViewById(R.id.nearstop);

        emergency                = (RelativeLayout) findViewById(R.id.emergency);
        tourguide                = (RelativeLayout) findViewById(R.id.tourguide);
        aboutus                  = (RelativeLayout) findViewById(R.id.aboutus);
        suggestion               = (RelativeLayout) findViewById(R.id.suggestion);
        logo                     = (ImageView) findViewById(R.id.logo);
        Img_lang                 = (ImageView) findViewById(R.id.Img_lang);

        tool_title = (TextView) findViewById(R.id.appTitle);
        //tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        //tool_back_icon.setVisibility(View.INVISIBLE);
        tool_title.setText(getResources().getString(R.string.tatpar));

        logo.setVisibility(View.GONE);
        Img_lang.setVisibility(View.VISIBLE);

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // get menu from navigationView
       Menu menu = navigationView.getMenu();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {int id = item.getItemId();

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

               if (id == R.id.nav_home) {

                    drawer.closeDrawer(GravityCompat.START);
                }/*else if (id == R.id.nav_trmcondition) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, TermsAndConditionActivity.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

                }*/
                /*else if (id == R.id.nav_rateus) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           *//*Intent inty = new Intent(MainActivity.this,LiveTrackingActivity_OnMap.class);
                           startActivity(inty);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*//*

                           Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                           sharingIntent.setType("text/plain");
                           sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Tatper");
                           sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hey, Check out the new Tatper Nrda  app on Android Play Store.  https://play.google.com/store/apps/details?id=com.ridz&hl=en");
                           startActivity(Intent.createChooser(sharingIntent, "Share via"));
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);
               }*/
               else if (id == R.id.nav_gallery) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, ImageGallery.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

               }else if (id == R.id.nav_pis) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, Nrda_Route_Eta.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

               }else if (id == R.id.nav_routemap) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, RouteMap.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

               }  else if (id == R.id.nav_smart) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, SmartCardDashBoard.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

                }  /*else if (id == R.id.nav_service) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, SpecialService.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

                }*//*else if (id == R.id.nav_support){
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                   Intent intent = new Intent(MainActivity.this, Other_Info.class);
                   startActivity(intent);
                   overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

               }*//*else if (id == R.id.nav_setting){
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, Language_Setting.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

               }*//*else if (id == R.id.nav_pis_route){
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           Intent intent = new Intent(MainActivity.this, Nrda_RouteList.class);
                           startActivity(intent);
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);

               }*/
               /* else if (id == R.id.nav_feedback) {
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                         *//* Intent i = new Intent(Intent.ACTION_SEND);
                           i.setData(Uri.parse("email"));
                           String[] s = {"abc@abc.com", "abc@abc.com"};
                           i.putExtra(Intent.EXTRA_EMAIL, s);
                           i.putExtra(Intent.EXTRA_SUBJECT, "Tatper Enquiry");
                           i.putExtra(Intent.EXTRA_TEXT, "Hi Thanks for Equiry With us \n ");
                           i.setType("message/rfc822");
                           Intent chooser = Intent.createChooser(i, "Launch Email");
                           startActivities(new Intent[]{chooser});
                           overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*//*
                       }
                   }, 50);
                   drawer.closeDrawer(GravityCompat.START);
               }*/

                return true;
            }
        });


        route_eta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Nrda_PIS_Activity.class);
                //intent.putExtra("ROUTE_NAME", "Route List");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        route_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NRDA_Route_info.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        bus_timing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Nrda_Bus_Timin.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        bus_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Nrda_Fare_cal.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        live_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showChooseTypeDialog();
                Intent intent = new Intent(MainActivity.this, Nrda_Routewise_liveTracking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        nearstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //  buildAlertMessageNoGps();
                    noLocation();
                }else {

                    Intent intent = new Intent(MainActivity.this, Nrda_NearBusStopOnMap.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }

            }
        });
        suggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EmergencyContact.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Nrda_AboutUs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        tourguide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TourGuide.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        Img_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Language_Setting.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                //Img_lang.setImageResource(R.drawable.ic_langic_hi);
            }
        });

       // int upRoutecount = databaseRoute.Get_UPRouteCount();
       /* if(isConnection() && upRoutecount == 0) {
            new GetAllRoutePoints().execute();
        }*/


    }

    public boolean noLocation() {
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();
            googleApiClient = null;
            enableLoc();

            return true;
        }
        return false;

    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().

                                status.startResolutionForResult((Activity) MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Intent intent = new Intent(MainActivity.this, Nrda_NearBusStopOnMap.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    break;
                case Activity.RESULT_CANCELED:
                    // TODO
                    //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();

                    break;
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            setResult(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.press_back, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public BroadcastReceiver internetConnectionReciever = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo activeWIFIInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);

            if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
                removeInternetDialog();
            } else {
                if (isNetDialogShowing) {
                    return;
                }
                showInternetDialog();
            }
        }
    };

    private void removeInternetDialog() {
        if (internetDialog != null && internetDialog.isShowing()) {
            internetDialog.dismiss();
            isNetDialogShowing = false;
            internetDialog = null;
        }
    }

    private void showInternetDialog() {
        isNetDialogShowing = true;
        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(this);
        internetBuilder.setCancelable(false);
        internetBuilder
                .setTitle(getString(R.string.dialog_no_internet))
                .setMessage(getString(R.string.dialog_no_inter_message))
                .setPositiveButton(getString(R.string.dialog_enable_3g),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete

                                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                                removeInternetDialog();
                            }
                        })
                .setNeutralButton(getString(R.string.dialog_enable_wifi),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // User pressed Cancel button. Write
                                // Logic Here
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                                removeInternetDialog();
                            }
                        })
                .setNegativeButton(getString(R.string.dialog_exit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeInternetDialog();
                                finish();
                            }
                        });
        internetDialog = internetBuilder.create();
        internetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(internetConnectionReciever, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        if (isConnection()) {
            Intent intent = new Intent(getApplicationContext(), RouteCoordinate.class);
            startService(intent);
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(internetConnectionReciever);
    }

  /*  private void exitme(){
        new AlertDialog.Builder(this, R.style.AppDialog)
                .setIcon(R.drawable.ic_alert)
                .setTitle("")
                .setMessage("Are you sure you want to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //preferenceHelper.clearAllData();
                        *//*Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);*//*

                    }
                })
                .setNegativeButton("No", null)
                .show();
    }*/

    /*class GetAllRoutePoints extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Please ...", true);

        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETROUTEPOINT;
                HttpPost httppost = new HttpPost(url);

                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {

            try {
                JSONObject jsonObj = new JSONObject(getResponse);
                boolean success = jsonObj.getBoolean("Success");

                if (success) {
                    JSONArray jarray = jsonObj.getJSONArray("routepoints");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        String routeid = object.getString("routeid");
                        String routeName = object.getString("routeName");

                        JSONArray innerArray = object.getJSONArray("Details");
                        for (int j = 0; j < innerArray.length(); j++) {
                            JSONObject pontobj = innerArray.getJSONObject(j);

                            databaseRoute.insertUPRoute(new DatabaseRoutemodel(routeid, pontobj.getString("latitude"), pontobj.getString("longitude")));
                        }
                    }
                }

            } catch (Exception ev) {
                Log.e("response issue", ev.getMessage());
            }
            progressDialog.dismiss();
        }
    }*/

        protected boolean isConnection() {
            ConnectivityManager manage = (ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manage.getActiveNetworkInfo();
            if (info != null && info.isConnectedOrConnecting()) {

                return true;
            } else {
                Toast.makeText(MainActivity.this, R.string.check_internet, Toast.LENGTH_LONG).show();
                return false;
            }
        }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


   /* public void showChooseTypeDialog(){
        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.live_search_track);

        TextView btn_search_all  = (TextView) dialog.findViewById(R.id.btn_search_all);
        TextView btn_search_route       = (TextView) dialog.findViewById(R.id.btn_search_route);

        btn_search_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, LiveTrackingActivity_OnMap.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        btn_search_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(MainActivity.this, Nrda_Routewise_liveTracking.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        dialog.show();
    }*/
}
