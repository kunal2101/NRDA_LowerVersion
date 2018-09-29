package com.nrda;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TourGuideNearestStop extends FragmentActivity implements OnMapReadyCallback {
    private TextView tool_title;
    private ImageView tool_back_icon, btnNavigate;
    LatLngBounds.Builder builder = new LatLngBounds.Builder();
    String getsourceLat = null, getsourceLongitude = null,
            getdestinationLat = null, getdestinationLongitude = null, getShelterId = null,
            getShelterName = null, getdistance = null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_guide_nearest_stop);

        tool_title          = (TextView) findViewById(R.id.appTitle);
        tool_back_icon      = (ImageView) findViewById(R.id.tool_back_icon);
        btnNavigate         = (ImageView) findViewById(R.id.btnNavigate);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.nearby_stop));

        if (getServicesAvailable()) {
            // Building the GoogleApi client
            //buildGoogleApiClient();
            //createLocationRequest();
            Toast.makeText(this, getResources().getString(R.string.google_service_available), Toast.LENGTH_SHORT).show();
        }
        //Create The MapView Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        try{
            Intent in = getIntent();
            getsourceLat = in.getStringExtra("KEY_LAT");
            getsourceLongitude = in.getStringExtra("KEY_LNG");

        }catch (Exception ev){
            System.out.print(ev.getMessage());
        }

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
        if (isConnection()){
            new getNearBusStop().execute();
        }

    }


    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) TourGuideNearestStop.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(TourGuideNearestStop.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
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
      //  mMap.setMyLocationEnabled(true);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(21.2513844, 81.6296413), 10));

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

   /* Marker mk = null;
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
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        getServicesAvailable();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    class getNearBusStop extends AsyncTask<String, String, String> {
        String getResponse = null, getAddress = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourGuideNearestStop.this, "Loading...", "Please Wait...", true);
           // getAddress = current_address.getText().toString();

           /* if(getAddress.contains("+")){
                String parts[] = getAddress.split("\\+");
                getAddress = parts[0];
            }*/

            /*if(getLocationFromAddress(getAddress) != null) {
                String[] latLongiArr = (getLocationFromAddress(getAddress)).split("`");
                getsourceLat = latLongiArr[0];
                getsourceLongitude = latLongiArr[1];
            }*/
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
                            Toast.makeText(TourGuideNearestStop.this, getResources().getString(R.string.sorry_we_dont_serve_location), Toast.LENGTH_LONG).show();
                        }

                        LatLngBounds bounds = builder.build();
                        int width = (getResources().getDisplayMetrics().widthPixels)/2;
                        int height = (getResources().getDisplayMetrics().heightPixels)/2;
                        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, 10);
                        mMap.animateCamera(cu);

                        int zoomlevel = 0 ;
                        if(Double.parseDouble(getdistance) > 0 && Double.parseDouble(getdistance) < 5){
                            zoomlevel = 15 ;
                        }else if(Double.parseDouble(getdistance) > 6 && Double.parseDouble(getdistance) < 11){
                            zoomlevel = 12 ;
                        }/*else if(Double.parseDouble(getdistance) > 10 && Double.parseDouble(getdistance) < 31){
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
                        }*/
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(getsourceLat), Double.parseDouble(getsourceLongitude)), zoomlevel));
                       // drawUpRoutePolyline();
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }

                    progressDialog.dismiss();
                }
            });
        }
    }
}
