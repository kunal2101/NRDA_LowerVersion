package com.nrda;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nrda.database.DatabaseRoute;
import com.nrda.model.MapBean;
import com.nrda.utils.Nrda_Url_Conts;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nrda_Routewise_liveTracking extends AppCompatActivity implements GoogleMap.OnMapClickListener {

    private SearchableSpinner route_no;
    //private FloatingActionButton routeList;
    private ArrayAdapter<String> adapters;
    ArrayList<String> mylist;
    ArrayList<HashMap<String, String>> dataServices;
    String get_route_id, get_route_name, get_route_no, s_name;
    private TextView tool_title;
    private ImageView tool_back_icon;
    SupportMapFragment fm;
    GoogleMap googleMap;
    List<Marker> markerList = new ArrayList<Marker>();
    List<Marker> mMarkerList = new ArrayList<Marker>();
    List<String> vehicleImei = new ArrayList<String>();
    LatLngBounds.Builder bld;
    ArrayList<MapBean> mapData = new ArrayList<MapBean>();
    ArrayList<MapBean> newmapData = new ArrayList<MapBean>();
    CountDownTimer mHandler;
    JSONArray jarray_shelter;
    JSONObject object_shelter;
    DatabaseRoute databaseRoute;
   LinearLayout layoutDrawer;
   DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrda__routewise_live_tracking);
        bld = new LatLngBounds.Builder();
        tool_title = (TextView) findViewById(R.id.appTitle);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(LiveTrackingActivity_OnMap.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText(getResources().getString(R.string.live_tracking));

       layoutDrawer = (LinearLayout) findViewById(R.id.layoutDrawer);
        drawer       = (DrawerLayout) findViewById(R.id.drawer_layout);

        databaseRoute = new DatabaseRoute(getApplicationContext());
        // drawUpRoutePolyline();

        if (isConnection()) {
            new GetRoute_Info().execute();
        }

        route_no = (SearchableSpinner) findViewById(R.id.spinner_route);
        // routeList = (FloatingActionButton) findViewById(R.id.routeList);

        route_no.setTitle(getResources().getString(R.string.select_route_name));

        route_no.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_name = route_no.getItemAtPosition(position).toString();

                //String getSelectedVal = mylist.get(position);
                if (position != -1) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map = dataServices.get(position);

                    get_route_id = map.get("KEY_ID");
                    get_route_name = map.get("KEY_NAME");
                    get_route_no = map.get("KEY_NO");

                    if (isConnection()) {
                        googleMap.clear();

                        // mHandler.cancel();
                        new maptracking().execute();
                        new getAllShelterList().execute();
                        drawUpRoutePolyline();
                        // mHandler.start();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) {
            // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.setPadding(0, 0, 0, 100);

            // Setting a custom info window adapter for the google map
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    View v = null;
                    try {
                        String[] infoArr = arg0.getTitle().split("`");
                        // Getting view from the layout file info_window_layout
                        v = getLayoutInflater().inflate(R.layout.live_custom_marker, null);

                        // Getting reference to the TextView to set latitude
                        TextView txtRouteno = (TextView) v.findViewById(R.id.txt_routeno);
                        TextView txtVhno = (TextView) v.findViewById(R.id.txt_vno);
                        TextView txtDate = (TextView) v.findViewById(R.id.txt_date);
                        TextView txtSpeed = (TextView) v.findViewById(R.id.txt_speed);
                       // Button markerIcon = (Button) v.findViewById(R.id.btnClose);
                        TextView txt_msg = (TextView) v.findViewById(R.id.txt_msg);
                        TextView txt_shelter = (TextView) v.findViewById(R.id.txt_shelter);
                        TextView txt_type = (TextView) v.findViewById(R.id.txt_type);
                        RelativeLayout topLay = (RelativeLayout) v.findViewById(R.id.topLay);

                        if (infoArr.length < 2) {
                            topLay.setVisibility(View.GONE);
                            txt_msg.setVisibility(View.VISIBLE);
                           // markerIcon.setVisibility(View.GONE);
                            txt_shelter.setVisibility(View.GONE);
                        } else {

                            txtRouteno.setText(infoArr[1]);
                            txtVhno.setText(infoArr[2]);
                            txtDate.setText(infoArr[3]);
                            txtSpeed.setText(infoArr[4] + "\tKm");
                            txt_type.setText(infoArr[5]);

                            if(txtRouteno.getText().toString().matches("")){

                           // if (TextUtils.isEmpty(txtRouteno.getText().toString())) {
                                txt_shelter.setText(infoArr[6]);

                                txt_shelter.setVisibility(View.VISIBLE);
                                topLay.setVisibility(View.GONE);
                               // markerIcon.setVisibility(View.GONE);
                                txt_msg.setVisibility(View.GONE);
                            } else {
                                topLay.setVisibility(View.VISIBLE);
                              //  markerIcon.setVisibility(View.GONE);
                                txt_msg.setVisibility(View.GONE);
                                txt_shelter.setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                    return v;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(Marker arg0) {

                    // Returning the view containing InfoWindow contents
                    return null;
                }
            });
        }


       /* routeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnection()) {
                    googleMap.clear();
                    mHandler.cancel(); // cancel
                    new maptracking().execute();
                   mHandler.start();
                }
            }
        });*/

        if (isConnection()) {

            new getAllShelterList().execute();
        }

        mHandler = new CountDownTimer(10000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                Log.w("Live Track counter", "Start");
              //  Toast.makeText(Nrda_Routewise_liveTracking.this, "Bg service start", Toast.LENGTH_LONG).show();
                new maptrackingBG().execute();
                start();
            }
        };
        mHandler.start();

       layoutDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END); /*Opens the Right Drawer*/
            }
        });

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    class GetRoute_Info extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(Nrda_Routewise_liveTracking.this, "", "Please Wait...", true);

        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETROUTEINFO;
                HttpPost httppost = new HttpPost(url);

                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {
           // progressDialog.dismiss();

            try {
                JSONObject jsonObj = new JSONObject(getResponse);
                String success = jsonObj.getString("Success");
                dataServices = new ArrayList<>();
                HashMap<String, String> mapO = new HashMap<>();
                mapO.put("KEY_NO", "0");
                mapO.put("KEY_ID", "all");
                mapO.put("KEY_NAME", "All");
                dataServices.add(mapO);
                mylist = new ArrayList<>();
                mylist.add(0, "All Routes");
                if (success.equalsIgnoreCase("true")) {

                    JSONArray jarray = jsonObj.getJSONArray("RouteDetails");

                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject object = jarray.getJSONObject(i);
                        map.put("KEY_NO", object.optString("routeNo"));
                        map.put("KEY_ID", object.optString("routeid"));
                        map.put("KEY_NAME", object.optString("routeName"));
                        dataServices.add(map);
                        mylist.add("Route " + object.optString("routeNo"));
                    }

                    adapters = new ArrayAdapter<String>(Nrda_Routewise_liveTracking.this, android.R.layout.simple_dropdown_item_1line, mylist);
                    route_no.setAdapter(adapters);
                    //route_no.setSelection(1);
                }

            } catch (Exception ev) {
                Log.e("response issue", ev.getMessage());
            }
        }
    }

    //on load screen
    class maptracking extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String jsonStr = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog = ProgressDialog.show(Nrda_Routewise_liveTracking.this, "", "Loading. Please wait...", true, false);
        }

        protected String doInBackground(String... args) {

            String parameter = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETLIVETRACKBYROUTE;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("routeid", get_route_id));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(parameter);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httppost);
                jsonStr = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {

                System.out.print(e.getMessage());
            }
            return jsonStr;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONObject obj = new JSONObject(getResponse);
                        mMarkerList.clear();// = new ArrayList<Marker>();
                        vehicleImei.clear();// = new ArrayList<String>();
                        if (obj.getString("Success").equalsIgnoreCase("true")) {
                            JSONArray jsonAry = obj.getJSONArray("VehicleTracking");
                            for (int i = 0; i < jsonAry.length(); i++) {
                                JSONObject jObj = jsonAry.getJSONObject(i);

                                MapBean data = new MapBean();

                                data.setLatitude(Double.valueOf(jObj.getString("latitude")));
                                data.setLongitude(Double.valueOf(jObj.getString("longitude")));
                                data.setRoutetype(jObj.getString("routetype"));
                                data.setName(jObj.getString("employeename"));
                                data.setVehicleRegNo(jObj.getString("vehicleregno"));
                                data.setDeviceSpeed(jObj.getString("devicespeed"));
                                data.setRouteID(jObj.getString("routeid"));
                                data.setRouteName(jObj.getString("routename"));
                                data.setRoutetype(jObj.getString("routetype"));
                                data.setTripdateTime(jObj.getString("packettime"));
                                mapData.add(data);

                                vehicleImei.add(jObj.getString("imeino"));

                                double newLatitude = Double.parseDouble(jObj.getString("latitude"));
                                double newLongitude = Double.parseDouble(jObj.getString("longitude"));

                                if (jObj.getString("routetype").equalsIgnoreCase("UP")) {
                                    Marker markerRed = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(newLatitude, newLongitude))
                                            .title("1`" + jObj.getString("routename") + "`" + jObj.getString("vehicleregno") + "`" + jObj.getString("packettime") + "`" + jObj.getString("devicespeed") + "`" + jObj.getString("routetype"))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.r))
                                    );
                                    mMarkerList.add(markerRed);

                                } else if (jObj.getString("routetype").equalsIgnoreCase("DOWN")){
                                    Marker markerGreen = googleMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(newLatitude, newLongitude))
                                            .title("1`" + jObj.getString("routename") + "`" + jObj.getString("vehicleregno") + "`" + jObj.getString("packettime") + "`" + jObj.getString("devicespeed")+ "`" + jObj.getString("routetype"))
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.g))
                                    );

                                    mMarkerList.add(markerGreen);
                                }
                                bld.include(new LatLng(newLatitude, newLongitude));
                            }
                        }

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bld.build(), 13));

                        progressDialog.dismiss();
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }
    }

    //every time interval
    class maptrackingBG extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String jsonStr = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {

            String parameter = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETLIVETRACKBYROUTE;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("routeid", get_route_id));

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(parameter);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httppost);
                jsonStr = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {

                System.out.print(e.getMessage());
            }
            return jsonStr;
        }

        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        Log.w("Live Track counter", getResponse);

                        //Toast.makeText(Nrda_Routewise_liveTracking.this, "Counter Start " + get_route_id, Toast.LENGTH_SHORT).show();
                        JSONObject obj = new JSONObject(getResponse);

                        if (obj.getString("Success").equalsIgnoreCase("true")) {
                            JSONArray jsonAry = obj.getJSONArray("VehicleTracking");
                            for (int i = 0; i < jsonAry.length(); i++) {
                                JSONObject jObj = jsonAry.getJSONObject(i);

                                MapBean data = new MapBean();

                                data.setLatitude(Double.valueOf(jObj.getString("latitude")));
                                data.setLongitude(Double.valueOf(jObj.getString("longitude")));
                                data.setRoutetype(jObj.getString("routetype"));
                                data.setName(jObj.getString("employeename"));
                                data.setVehicleRegNo(jObj.getString("vehicleregno"));
                                data.setDeviceSpeed(jObj.getString("devicespeed"));
                                data.setRouteID(jObj.getString("routeid"));
                                data.setRouteName(jObj.getString("routename"));
                                data.setRoutetype(jObj.getString("routetype"));
                                data.setTripdateTime(jObj.getString("packettime"));
                                mapData.add(data);

                                int getIndex = vehicleImei.indexOf(jObj.getString("imeino"));
                                if (getIndex > -1) {
                                    double newLatitude = Double.parseDouble(jObj.getString("latitude"));
                                    double newLongitude = Double.parseDouble(jObj.getString("longitude"));
                                    Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
                                    targetLocation.setLatitude(newLatitude);
                                    targetLocation.setLongitude(newLongitude);

                                    Marker getMarker = mMarkerList.get(getIndex);
                                    animateMarkerNew(targetLocation, getMarker);

                                }
                            }
                        }
                        try {
                            for (Marker marker : markerList) {
                                marker.remove();
                            }
                        } catch (Exception ev) {
                            System.out.print(ev.getMessage());
                        }

                        markerList.clear();

                        for (int j = 0; j < mapData.size(); j++) {
                            bld.include(new LatLng(mapData.get(j).getLatitude(), mapData.get(j).getLongitude()));
                        }
                        newmapData.clear();
                        newmapData = mapData;
                    } catch (Exception ev) {
                        System.out.print(ev.getMessage());
                    }
                }
            });
        }

    }


    class getAllShelterList extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Routewise_liveTracking.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETALLSHELTERLIST;

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);

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
                            jarray_shelter = new JSONArray();

                            jarray_shelter = jsonObject.getJSONArray("ShelterDetails");

                            for (int i = 0; i < jarray_shelter.length(); i++) {
                                object_shelter = jarray_shelter.getJSONObject(i);

                                View markerView = ((LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_shelter_layout, null);

                                Marker shetletMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(Double.parseDouble(object_shelter.optString("Lat")), Double.parseDouble(object_shelter.optString("lon"))))
                                        .title("1`" + "`" + "`" + "`" + "`" +"`" + object_shelter.optString("ShelterName"))
                                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(Nrda_Routewise_liveTracking.this, markerView))));
                            }
                        } else {
                            String msg = jsonObject.getString("Message");
                            Toast.makeText(Nrda_Routewise_liveTracking.this, msg, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }
                }
            });
            progressDialog.dismiss();
        }
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_Routewise_liveTracking.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_Routewise_liveTracking.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()

        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new DrawerLayout.LayoutParams(DrawerLayout.LayoutParams.WRAP_CONTENT, DrawerLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    private void drawUpRoutePolyline() {
        ArrayList<LatLng> upRouteArr = new ArrayList<>();

        try {
            int totalCount = databaseRoute.Get_UPRouteCount();
            if (totalCount > 0) {
                ArrayList<String> getRouteIdArr = databaseRoute.Get_RouteId();

                for (int aind = 0; aind < getRouteIdArr.size(); aind++) {
                    String getRoueid = getRouteIdArr.get(aind);
                    upRouteArr = new ArrayList<>();
                    ///////////////// diwash ////////////
                    ArrayList<HashMap<String, String>> getcoordinateArr = databaseRoute.GetRouteby_Id(getRoueid);
                    for (int pqr = 0; pqr < getcoordinateArr.size(); pqr++) {
                        HashMap<String, String> routeModel = getcoordinateArr.get(pqr);
                        double latitude = Double.parseDouble(routeModel.get("latitude"));
                        double longitude = Double.parseDouble(routeModel.get("longitude"));

                        upRouteArr.add(new LatLng(latitude, longitude));
                    }
                    PolylineOptions polylineOptions = new PolylineOptions();
                    if (aind == 0) {
                        polylineOptions.color(Color.parseColor("#51a8ff"));
                        polylineOptions.width(9);
                        polylineOptions.addAll(upRouteArr);
                        googleMap.addPolyline(polylineOptions);

                    } else {
                        polylineOptions.color(Color.parseColor("#ef9a42"));
                        polylineOptions.width(4);
                        polylineOptions.addAll(upRouteArr);
                        googleMap.addPolyline(polylineOptions);

                    }
                }
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(upRouteArr.get(2), 11));
            }

        } catch (Exception ev) {
            System.out.print(ev.getMessage());
        }
    }
    private void animateMarkerNew(final Location destination, final Marker marker) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(1000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {

                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        marker.setAnchor(0.5f, 0.5f);
                        marker.setAlpha(1.0f);
                        marker.setRotation(getBearing(startPosition, new LatLng(destination.getLatitude(), destination.getLongitude())));

                    } catch (Exception ex) {

                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                }
            });
            valueAnimator.start();
        }
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    //Method for finding bearing between two points
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);
        float bearing = 0;

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            bearing = (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            bearing = (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            bearing = (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            bearing = (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
       /* else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            bearing = (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 360);*/

        if (begin.latitude == end.latitude) {
            bearing = 0;
        }
        //Toast.makeText(getApplicationContext(), String.valueOf(bearing),Toast.LENGTH_LONG).show();
        return bearing;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.w("Live Track on destroy", "Done");
        // Toast.makeText(Nrda_Routewise_liveTracking.this, "Live Track Stop", Toast.LENGTH_LONG).show();
        mHandler.cancel();
    }

}
