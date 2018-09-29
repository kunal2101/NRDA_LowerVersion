package com.nrda;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.nrda.database.DatabaseRoute;
import com.nrda.utils.Nrda_Url_Conts;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class Nrda_NearBusStopOnMap extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private TextView tool_title, current_address, btn_search;
    private ImageView tool_back_icon, address_clear, btnNavigate;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private Address address;
    private LatLng curretLatLng;
    public static boolean isMapTouched = false;
    private float currentZoom = -1;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    //private FloatingActionButton myLocationButton;
    private Button myLocationButton;
    DatabaseRoute databaseRoute;
   // ArrayList<HashMap<String, String>> dataServices;
    String getsourceLat = null, getsourceLongitude = null, strAddress = null,
            getdestinationLat = null, getdestinationLongitude = null, getShelterId = null,
            getShelterName = null, getdistance = null;
    String progressvalue;
    int progressChangedValue = 5;
   // ProgressDialog progressDialog;
    private static final LatLngBounds BOUNDS_RAIPUR = new LatLngBounds(new LatLng(21.2513844, 81.6296413), new LatLng(28.20453, 97.34466));
    //private static final int PLACE_PICKER_REQUEST = 3;
    final static int REQUEST_LOCATION = 199;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrda__near_bus_stop_on_map);

        tool_title          = (TextView) findViewById(R.id.appTitle);
        tool_back_icon      = (ImageView) findViewById(R.id.tool_back_icon);
        current_address     = (TextView) findViewById(R.id.et_current_address);
        address_clear       = (ImageView) findViewById(R.id.address_clear);
        btn_search          = (TextView) findViewById(R.id.btn_search);
        btnNavigate         = (ImageView) findViewById(R.id.btnNavigate);
        myLocationButton    = (Button) findViewById(R.id.myLocationButton);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.nearby_stop));
        //Check If Google Services Is Available
        if (getServicesAvailable()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
            Toast.makeText(this, getResources().getString(R.string.google_service_available), Toast.LENGTH_SHORT).show();
        }
        //Create The MapView Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        databaseRoute = new DatabaseRoute(getApplicationContext());

        current_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    //startActivityForResult(builder.build(Nrda_NearBusStopOnMap.this), PLACE_PICKER_REQUEST);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(Nrda_NearBusStopOnMap.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
       // progressDialog = ProgressDialog.show(Nrda_NearBusStopOnMap.this, "Getting Your Location...", "Please Wait...", true);
        address_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_address.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseTypeDialog();
            }
        });

        btnNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                               try {

                    //https://route.cit.api.here.com/routing/7.2/calculateroute.json?app_id=3HD3IuU4gH4DRpjm5S7s&app_code=wuZszRdHlWwSYRZF_YPYYg&waypoint0=geo!52.5,13.4&waypoint1=geo!52.5,13.45&mode=fastest;car;traffic:disabled

                    String google_url = "http://maps.google.com/maps?saddr="+getsourceLat+","+getsourceLongitude+"&daddr="+getdestinationLat+","+getdestinationLongitude;
                    String uri = String.format(Locale.ENGLISH, google_url, "Where the party is at");

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
                catch (Exception ev){
                    System.out.print(ev.getMessage());
                }
            }
        });

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Location loc = mMap.getMyLocation();
                    if (loc != null){
                        mMap.clear();
                       // LatLng ltlng = new LatLng(loc.getLatitude(), loc.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 18));
                        addOverlay(new LatLng(loc.getLatitude(),loc.getLongitude()));
                        addMarker(mMap,loc.getLatitude(),loc.getLongitude());
                    }
                }
                catch (Exception ev){
                    System.out.print(ev.getMessage());
                }
            }
        });

        //drawUpRoutePolyline();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //Uncomment To Show Google Location Blue Pointer
         mMap.setMyLocationEnabled(true);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.2513844, 81.6296413), 10));

       // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(BOUNDS_INDIA,zoomWidth, zoomHeight,zoomPadding));

        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition camPos) {
                if (currentZoom == -1) {
                    currentZoom = camPos.zoom;

                } else if (camPos.zoom != currentZoom) {
                    currentZoom = camPos.zoom;
                    return;
                }

                if (!isMapTouched) {
                    curretLatLng = camPos.target;
                    getAddressFromLocation(camPos.target, current_address);

                }
                isMapTouched = false;

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                View v = null;
                try {
                    String[] infoArr = marker.getTitle().split("`");
                    // Getting view from the layout file info_window_layout
                    v = getLayoutInflater().inflate(R.layout.custom_marker, null);

                    // Getting reference to the TextView to set latitude
                    TextView topTitle = (TextView)v.findViewById(R.id.topTitle);
                    TextView labelStop = (TextView) v.findViewById(R.id.labelStop);
                    TextView stopText = (TextView) v.findViewById(R.id.txt_stopName);
                    ImageView markerIcon = (ImageView)v.findViewById(R.id.markerIcon);
                    View viewLine = (View)v.findViewById(R.id.viewLine);

                    // Getting reference to the TextView to set longitude
                    TextView distanceText = (TextView) v.findViewById(R.id.txt_distance);
                    TextView labelDestination = (TextView) v.findViewById(R.id.labelDestination);

                    // Setting the latitude
                    stopText.setText(infoArr[1]);

                    if(Integer.parseInt(infoArr[0]) == 1) {
                        labelStop.setVisibility(View.VISIBLE);
                        labelStop.setText(getResources().getString(R.string.stop_name));
                        labelDestination.setText(getResources().getString(R.string.distance));
                        labelDestination.setVisibility(View.VISIBLE);
                        distanceText.setVisibility(View.VISIBLE);
                        markerIcon.setVisibility(View.VISIBLE);
                        viewLine.setVisibility(View.VISIBLE);
                        topTitle.setText(getResources().getString(R.string.near_stop_from_your_location));
                        // markerIcon.setImageResource(R.drawable.bus_stop);

                        if (infoArr.length > 2) {
                            // Setting the longitude
                            distanceText.setText(infoArr[2] + "  \tKm");
                        } else {
                            distanceText.setText(getResources().getString(R.string.no_record));
                        }
                    }else{
                        topTitle.setText(getResources().getString(R.string.my_location));
                        markerIcon.setVisibility(View.GONE);
                        viewLine.setVisibility(View.GONE);
                        labelDestination.setVisibility(View.INVISIBLE);
                        distanceText.setVisibility(View.INVISIBLE);
                        labelStop.setVisibility(View.INVISIBLE);
                    }
                }catch (Exception ev){
                    System.out.print(ev.getMessage());
                }
                // Returning the view containing InfoWindow contents
                return v;

            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });
    }

    Marker mk = null;
    // Add A Map Pointer To The MAp
    public void addMarker(GoogleMap googleMap, double lat, double lon) {

            mMap = googleMap;

            LatLng latlong = new LatLng(lat, lon);
            mk= mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                    .title("2`" + getResources().getString(R.string.my_location))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 18));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;

        }
        //startLocationUpdates();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        getServicesAvailable();

        drawUpRoutePolyline();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //Method to display the location on UI
    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {


            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                //progressDialog.dismiss();

                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                //LatLng latLng = new LatLng(latitude, longitude);
                String loc = "" + latitude + " ," + longitude + " ";
               // Toast.makeText(this,loc, Toast.LENGTH_SHORT).show();

                //Add pointer to the map at location
                addMarker(mMap,latitude,longitude);
                addOverlay(new LatLng(latitude, longitude));
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                stopLocationUpdates();

            } else {

                Toast.makeText(this, getResources().getString(R.string.gps_enable_msg),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Creating google api client object
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    //Creating location request object
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Nrda_Url_Conts.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Nrda_Url_Conts.FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(Nrda_Url_Conts.DISPLACEMENT);
    }

    //Starting the location updates
    protected void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest,  this);
        }
    }

    //Stopping location updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Once connected with google api, get the location
       // mMap.clear();
       displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        curretLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        displayLocation();
    }

    @Override
    public void onInfoWindowClick(Marker arg0) {

    }

    public boolean getServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {

            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cannot Connect To Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void getAddressFromLocation(final LatLng latlng, final TextView et) {
        et.setText(getResources().getString(R.string.wating_address));
        et.setTextColor(Color.BLACK);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Geocoder gCoder = new Geocoder(getApplication());
                    final List<Address> list = gCoder.getFromLocation(latlng.latitude, latlng.longitude, 1);
                    if (list != null && list.size() > 0) {
                        address = list.get(0);
                        StringBuilder sb = new StringBuilder();
                        if (address.getAddressLine(0) != null) {
                            if (address.getMaxAddressLineIndex() > 0) {
                                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                    sb.append(address.getAddressLine(i)).append("\n");
                                }
                                sb.append(",");
                                sb.append(address.getCountryName());
                            } else {
                                sb.append(address.getAddressLine(0));
                            }
                        }

                        strAddress = sb.toString();
                        strAddress = strAddress.replace(",null", "");
                        strAddress = strAddress.replace("null", "");
                        strAddress = strAddress.replace("\n", " ");
                        strAddress = strAddress.replace("Unnamed", "");
                    }
                    if (getApplication() == null)
                        return;

                    Nrda_NearBusStopOnMap.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(strAddress)) {
                                et.setText(strAddress);
                            } else {
                                et.setText("");
                                et.setTextColor(getResources().getColor(android.R.color.black));
                            }
                        }
                    });
                }
                catch (Exception exc) {
                    exc.printStackTrace();
                }
            }
        }).start();
    }

    public String getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        Barcode.GeoPoint p1 = null;
        String latitudeLongitude = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }

            try {
                Address location = address.get(0);

                double a1 = location.getLatitude();
                double a2 = location.getLongitude();

           /* p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));*/

                latitudeLongitude = a1 + "`" + a2;
            }catch (Exception ev){
                System.out.print(ev.getMessage());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latitudeLongitude;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());
                String msg = place.getName() + ",\n" + place.getAddress() + "\n" + place.getPhoneNumber();

                current_address.setText("" + place.getAddress());
                mMap.clear();

                LatLng lat_lng = place.getLatLng();
                double lat = lat_lng.latitude;
                double lng = lat_lng.longitude;
                addMarker(mMap,lat,lng);

                mMap.addCircle(new CircleOptions()
                        .center(place.getLatLng())
                        .radius(25)
                        .strokeColor(Color.RED)
                        .strokeWidth(1)
                        .fillColor(0x400000FF));

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());
                Toast.makeText(Nrda_NearBusStopOnMap.this, "GPS not working..", Toast.LENGTH_LONG).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Toast.makeText(Nrda_NearBusStopOnMap.this, "The user canceled the operation.", Toast.LENGTH_LONG).show();
            }
        }

       /* if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                String addressText = place.getName().toString();
                addressText += "\n" + place.getAddress().toString();

                current_address.setText("" + place.getAddress());
                mMap.clear();
                LatLng lat_lng = place.getLatLng();
                double lat = lat_lng.latitude;
                double lng = lat_lng.longitude;
                addMarker(mMap,lat,lng);
            }
        }*/
    }

    // -------- Bubble Animation on current location start----------------------------
    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void addOverlay(LatLng place) {

        GroundOverlay groundOverlay = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            groundOverlay = mMap.addGroundOverlay(new
                    GroundOverlayOptions()
                    .position(place, 100)
                    .transparency(0.5f)
                    .zIndex(3)
                    .image(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(getDrawable(R.drawable.map_overlay)))));
        }

        startOverlayAnimation(groundOverlay);
    }


    private void startOverlayAnimation(final GroundOverlay groundOverlay) {

        AnimatorSet animatorSet = new AnimatorSet();

        ValueAnimator vAnimator = ValueAnimator.ofInt(0, 100);
        vAnimator.setRepeatCount(ValueAnimator.INFINITE);
        vAnimator.setRepeatMode(ValueAnimator.RESTART);
        vAnimator.setInterpolator(new LinearInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                final Integer val = (Integer) valueAnimator.getAnimatedValue();
                groundOverlay.setDimensions(val);

            }
        });

        ValueAnimator tAnimator = ValueAnimator.ofFloat(0, 1);
        tAnimator.setRepeatCount(ValueAnimator.INFINITE);
        tAnimator.setRepeatMode(ValueAnimator.RESTART);
        tAnimator.setInterpolator(new LinearInterpolator());
        tAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Float val = (Float) valueAnimator.getAnimatedValue();
                groundOverlay.setTransparency(val);
            }
        });

        animatorSet.setDuration(3000);
        animatorSet.playTogether(vAnimator, tAnimator);
        animatorSet.start();
    }
    // -------- Bubble Animation on current location end ----------------------------------



    class getNearBusStop extends AsyncTask<String, String, String> {
        String getResponse = null, getAddress = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_NearBusStopOnMap.this, "Loading...", "Please Wait...", true);
            getAddress = current_address.getText().toString();

            if(getAddress.contains("+")){
                String parts[] = getAddress.split("\\+");
                getAddress = parts[0];
            }

            if(getLocationFromAddress(getAddress) != null) {
                String[] latLongiArr = (getLocationFromAddress(getAddress)).split("`");
                getsourceLat = latLongiArr[0];
                getsourceLongitude = latLongiArr[1];
            }
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("lat", getsourceLat));
            nameValuePairs.add(new BasicNameValuePair("lon", getsourceLongitude));

            try {
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETNEARBUS_STOP;

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httppost);
                getResponse = EntityUtils.toString(httpResponse.getEntity());

            } catch (Exception ev) {
                ev.getStackTrace();
            }
            return getResponse;
        }

        @Override
        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {

                        JSONObject jsonObject = new JSONObject(getResponse);
                        String success = jsonObject.getString("Success");
                        if (success.equalsIgnoreCase("true")) {
                            JSONArray jarray = jsonObject.getJSONArray("NearestShelter");

                            for (int i = 0; i < jarray.length(); i++) {

                                JSONObject object = jarray.getJSONObject(i);
                                 getdestinationLat = object.optString("latitude");
                                 getdestinationLongitude = object.optString("longitude");
                                 getShelterId = object.optString("shelterid");
                                 getShelterName = object.optString("sheltername");
                                 getdistance =  object.optString("distance");

                                mMap.clear();

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(getdestinationLat), Double.parseDouble(getdestinationLongitude)))
                                        .title("1`"+getShelterName+"`"+getdistance)
                                        .icon(BitmapDescriptorFactory.fromResource((R.drawable.ic_m))));

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)))
                                        .title("2`"+getAddress) // diwash
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                                mMap.addCircle(new CircleOptions()
                                        .center(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)))
                                        .radius(25)
                                        .strokeColor(Color.RED)
                                        .strokeWidth(1)
                                        .fillColor(0x400000FF));

                                builder.include(new LatLng(Double.parseDouble(getdestinationLat), Double.parseDouble(getdestinationLongitude)));
                                builder.include(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)));
                            }
                            btnNavigate.setVisibility(View.VISIBLE);

                        } else {
                            btnNavigate.setVisibility(View.INVISIBLE);
                            String msg = jsonObject.getString("Message");
                            Toast.makeText(Nrda_NearBusStopOnMap.this, getResources().getString(R.string.sorry_we_dont_serve_location), Toast.LENGTH_LONG).show();
                        }

                        LatLngBounds bounds = builder.build();
                        int width = (getResources().getDisplayMetrics().widthPixels)/2;
                        int height = (getResources().getDisplayMetrics().heightPixels)/2;
                        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 40);
                        mMap.animateCamera(cu);

                        int zoomlevel = 0 ;
                        if(Double.parseDouble(getdistance) > 0 && Double.parseDouble(getdistance) < 5){
                            zoomlevel = 14 ;
                        }else if(Double.parseDouble(getdistance) > 6 && Double.parseDouble(getdistance) < 11){
                            zoomlevel = 12 ;
                        }else if(Double.parseDouble(getdistance) > 10 && Double.parseDouble(getdistance) < 31){
                            zoomlevel = 11 ;
                        }else if(Double.parseDouble(getdistance) > 30 && Double.parseDouble(getdistance) < 51){
                            zoomlevel = 10 ;
                        }else if(Double.parseDouble(getdistance) > 50 && Double.parseDouble(getdistance) < 81){
                            zoomlevel = 9 ;
                        }else if(Double.parseDouble(getdistance) > 80 && Double.parseDouble(getdistance) < 101){
                            zoomlevel = 8 ;
                        }else if(Double.parseDouble(getdistance) > 100 && Double.parseDouble(getdistance) < 201){
                            zoomlevel = 6 ;
                        }else if(Double.parseDouble(getdistance) > 200 && Double.parseDouble(getdistance) < 501){
                            zoomlevel = 5 ;
                        }else if(Double.parseDouble(getdistance) > 500 && Double.parseDouble(getdistance) < 1001){
                            zoomlevel = 4 ;
                        }else if(Double.parseDouble(getdistance) > 1000 && Double.parseDouble(getdistance) < 2000){
                            zoomlevel = 3 ;
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)), zoomlevel));
                        drawUpRoutePolyline();
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }

                    progressDialog.dismiss();
                }
            });
        }
    }

    private void drawUpRoutePolyline(){
        ArrayList<LatLng> upRouteArr = new ArrayList<>();

        try {
            int totalCount = databaseRoute.Get_UPRouteCount();
            if(totalCount > 0) {
                ArrayList<String> getRouteIdArr = databaseRoute.Get_RouteId();

                for (int aind = 0; aind < getRouteIdArr.size(); aind++) {
                    String getRoueid = getRouteIdArr.get(aind);
                    upRouteArr = new ArrayList<>();
                    ///////////////// diwash ////////////
                    ArrayList<HashMap<String,String>> getcoordinateArr = databaseRoute.GetRouteby_Id(getRoueid);
                    for (int pqr = 0; pqr < getcoordinateArr.size(); pqr++) {
                        HashMap<String,String> routeModel = getcoordinateArr.get(pqr);
                        double latitude = Double.parseDouble(routeModel.get("latitude"));
                        double longitude = Double.parseDouble(routeModel.get("longitude"));

                        upRouteArr.add(new LatLng(latitude, longitude));
                    }
                    PolylineOptions polylineOptions = new PolylineOptions();
                    if(aind == 0){
                        polylineOptions.color(Color.parseColor("#51a8ff"));
                        polylineOptions.width(10);
                        polylineOptions.addAll(upRouteArr);
                        mMap.addPolyline(polylineOptions);

                    }else{
                        polylineOptions.color(Color.parseColor("#ef9a42"));
                        polylineOptions.width(4);
                        polylineOptions.addAll(upRouteArr);
                        mMap.addPolyline(polylineOptions);

                    }
                }
            }

        }catch (Exception ev){
            System.out.print(ev.getMessage());
        }
    }

    public void showRadiusDialog(){
        final Dialog dialog = new Dialog(this, R.style.mAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.search_dialog);

        TextView btn_search_radius  = (TextView) dialog.findViewById(R.id.btn_search_radius);
        TextView cancleButton       = (TextView) dialog.findViewById(R.id.btn_cancel);
        SeekBar simpleSeekBar       = (SeekBar) dialog.findViewById(R.id.simpleSeekBar);
       // TextView txt_address       = (TextView) dialog.findViewById(R.id.txt_address);
        final EditText et_radius          = (EditText) dialog.findViewById(R.id.et_radius);
        //txt_address.setText(current_address.getText().toString());

        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                /*Toast.makeText(Nrda_NearBusStopOnMap.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();*/
            }
        });


        btn_search_radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressChangedValue == 0){
                    Toast.makeText(Nrda_NearBusStopOnMap.this, getResources().getString(R.string.select_radius), Toast.LENGTH_LONG).show();
                }else {
                    progressvalue = String.valueOf(progressChangedValue);
                    new GetRadiusBasedShelter().execute(progressvalue);
                    dialog.dismiss();
                }
               /* if (TextUtils.isEmpty(et_radius.getText())){
                        Toast.makeText(Nrda_NearBusStopOnMap.this, "Enter radius in Km", Toast.LENGTH_LONG).show();
                }else {
                    new GetRadiusBasedShelter().execute(progressvalue);
                    dialog.dismiss();
                }*/
            }
        });

        cancleButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
                dialog.dismiss();
           }
       });

        dialog.show();
    }

    public void showChooseTypeDialog(){
        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.search_type);

        TextView btn_search_radius  = (TextView) dialog.findViewById(R.id.btn_search_radius);
        TextView btn_search_location       = (TextView) dialog.findViewById(R.id.btn_search_location);


        btn_search_radius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showRadiusDialog();
            }
        });

        btn_search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new getNearBusStop().execute();
            }
        });

        dialog.show();
    }

    class GetRadiusBasedShelter extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String getResponse = null, getAddress = null,url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Nrda_NearBusStopOnMap.this, "", "Please Wait...", true);
            getAddress = current_address.getText().toString();

            if(getAddress.contains("+")){
                String parts[] = getAddress.split("\\+");
                getAddress = parts[0];
            }

            if(getLocationFromAddress(getAddress) != null) {
                String[] latLongiArr = (getLocationFromAddress(getAddress)).split("`");
                getsourceLat = latLongiArr[0];
                getsourceLongitude = latLongiArr[1];
            }
        }

        protected String doInBackground(String... args) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("lat", getsourceLat));
            nameValuePairs.add(new BasicNameValuePair("lon", getsourceLongitude));
            nameValuePairs.add(new BasicNameValuePair("range", args[0]));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETSHELTERBYRANGE;
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        mMap.clear();
                        JSONObject jsonObject = new JSONObject(getResponse);
                        String success = jsonObject.getString("Success");
                        if (success.equalsIgnoreCase("true")) {
                            JSONArray jarray = jsonObject.getJSONArray("NearestShelter");

                            for (int i = 0; i < jarray.length(); i++) {

                                JSONObject object = jarray.getJSONObject(i);
                                getdestinationLat = object.optString("latitude");
                                getdestinationLongitude = object.optString("longitude");
                                getShelterId = object.optString("shelterid");
                                getShelterName = object.optString("sheltername");
                                getdistance = object.optString("distance");

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(getdestinationLat), Double.parseDouble(getdestinationLongitude)))
                                        .title("1`" + getShelterName + "`" + getdistance)
                                        .icon(BitmapDescriptorFactory.fromResource((R.drawable.ic_m))));

                                builder.include(new LatLng(Double.parseDouble(getdestinationLat), Double.parseDouble(getdestinationLongitude)));
                                builder.include(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)));
                            }

                            mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)))
                                    .title("2`" + getAddress) // Current Location
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            mMap.addCircle(new CircleOptions()
                                    .center(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)))
                                    .radius(25)
                                    .strokeColor(Color.RED)
                                    .strokeWidth(1)
                                    .fillColor(0x400000FF));

                            btnNavigate.setVisibility(View.VISIBLE);

                        } else {
                            btnNavigate.setVisibility(View.INVISIBLE);
                            String msg = jsonObject.getString("Message");
                            Toast.makeText(Nrda_NearBusStopOnMap.this, getResources().getString(R.string.sorry_we_dont_serve_location), Toast.LENGTH_LONG).show();
                        }

                        LatLngBounds bounds = builder.build();
                        int width = (getResources().getDisplayMetrics().widthPixels) / 2;
                        int height = (getResources().getDisplayMetrics().heightPixels) / 2;
                        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 40);
                        mMap.animateCamera(cu);

                        int zoomlevel = 0;
                        if (Double.parseDouble(getdistance) > 0 && Double.parseDouble(getdistance) < 5) {
                            zoomlevel = 14;
                        } else if (Double.parseDouble(getdistance) > 6 && Double.parseDouble(getdistance) < 11) {
                            zoomlevel = 12;
                        } else if (Double.parseDouble(getdistance) > 10 && Double.parseDouble(getdistance) < 31) {
                            zoomlevel = 10;
                        }
                       /* else if (Double.parseDouble(getdistance) > 30 && Double.parseDouble(getdistance) < 51) {
                            zoomlevel = 10;
                        } else if (Double.parseDouble(getdistance) > 50 && Double.parseDouble(getdistance) < 81) {
                            zoomlevel = 9;
                        } else if (Double.parseDouble(getdistance) > 80 && Double.parseDouble(getdistance) < 101) {
                            zoomlevel = 8;
                        } else if (Double.parseDouble(getdistance) > 100 && Double.parseDouble(getdistance) < 201) {
                            zoomlevel = 6;
                        } else if (Double.parseDouble(getdistance) > 200 && Double.parseDouble(getdistance) < 501) {
                            zoomlevel = 5;
                        } else if (Double.parseDouble(getdistance) > 500 && Double.parseDouble(getdistance) < 1001) {
                            zoomlevel = 4;
                        } else if (Double.parseDouble(getdistance) > 1000 && Double.parseDouble(getdistance) < 2000) {
                            zoomlevel = 3;
                        }*/
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)), zoomlevel));
                        drawUpRoutePolyline();
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }

                    progressDialog.dismiss();

                }

            });
        }
    }

}
