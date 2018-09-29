package com.nrda;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Splash_Screen extends AppCompatActivity {
   // ImageView head;
  // ImageView image;
    Thread background;
    //RelativeLayout rela_main;
    //boolean is_click=false;

    //final static int REQUEST_LOCATION = 199;
    //private GoogleApiClient googleApiClient;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE };

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
   // int windowwidth;
   // int windowheight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        //image = (ImageView) findViewById(R.id.logo);

       // windowwidth = getWindowManager().getDefaultDisplay().getWidth();
       // windowheight = getWindowManager().getDefaultDisplay().getHeight();
       // this.image = (ImageView) findViewById(R.id.splash_Rotate);
        //this.head = (ImageView) findViewById(R.id.rl);
       // rela_main=(RelativeLayout)findViewById(R.id.rela_main);

            if (ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[4]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[5]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[6]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[2])
                        || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[3])
                        || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[4])
                        || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[5])
                        || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this, permissionsRequired[6])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(Splash_Screen.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Storage and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(Splash_Screen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder.show();
                } /*else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(Splash_Screen.this);
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Storage and Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Storage and Location permission", Toast.LENGTH_LONG).show();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

                    builder.show();
                }*/ else {
                    //just request the permission
                    ActivityCompat.requestPermissions(Splash_Screen.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }

                // txtPermissions.setText("Permissions Required");

                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }


      //  rotateImage();
       // this.head.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));

    }

   /* private void StartAnimations() {
        RelativeLayout root = (RelativeLayout)findViewById( R.id.rela_main);
        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);
        @SuppressWarnings("unused")
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        image.getLocationOnScreen( originalPos );

        TranslateAnimation anim = new TranslateAnimation(0, 0, dm.heightPixels, (float) (dm.heightPixels/4.0) );
        anim.setDuration(3000);
        anim.setFillAfter(true);
        image.startAnimation(anim);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {

                Intent i = new Intent(Splash_Screen.this, MainActivity.class);
                startActivity(i);
                finish();

            }

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(), "Start", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
        });
    }*/

   /* public void rotateImage() {
        Animation animationToLeft = new TranslateAnimation(200.0f, -200.0f, 0.0f, 0.0f);
        animationToLeft.setDuration(10000);
        animationToLeft.setRepeatMode(2);
        animationToLeft.setRepeatCount(-1);
        //this.image.setAnimation(animationToLeft);
    }*/
/*
    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();

            enableLoc();

            return true;
        }
        return false;

    }
*/
   /* private void enableLoc() {

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

                                status.startResolutionForResult((Activity) Splash_Screen.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });

        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == REQUEST_LOCATION) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // TODO
                    //Toast.makeText(getApplicationContext(), "accept", Toast.LENGTH_LONG).show();
                    background = new Thread() {
                        public void run() {

                            try {
                                sleep(4 * 1000);
                                Intent inty = new Intent(getApplication(), MainActivity.class);
                                startActivity(inty);
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                                finish();

                            } catch (Exception e) {
                                System.out.print(e.getMessage());
                            }
                        }
                    };

                    // start thread

                    background.start();

                    //StartAnimations();
                    break;
                case Activity.RESULT_CANCELED:
                    // TODO
                    //Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                    finish();
                    break;
            }
        }*/

        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

    }

    private void proceedAfterPermission() {
       /* LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();
            noLocation();
        }else {*/
            background = new Thread() {
                public void run() {
                    try {
                        sleep(4 * 1000);
                        Intent inty = new Intent(getApplication(), MainActivity.class);
                        startActivity(inty);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                        finish();

                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            };
            background.start();

            //StartAnimations();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Splash_Screen.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){

                proceedAfterPermission();

            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[3])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[4])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[5])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,permissionsRequired[6])){
                //txtPermissions.setText("Permissions Required");
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash_Screen.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Storage and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Splash_Screen.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();

            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
