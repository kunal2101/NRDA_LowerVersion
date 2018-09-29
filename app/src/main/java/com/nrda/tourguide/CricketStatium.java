package com.nrda.tourguide;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.nrda.R;
import com.nrda.TourGuideNearestStop;

public class CricketStatium extends AppCompatActivity implements OnMapReadyCallback {
    private SupportMapFragment mapFragment;
    private TextView tool_title;
    private ImageView tool_back_icon, mapview;
    Button map_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cricket_statium);

        tool_title = (TextView) findViewById(R.id.appTitle);
        tool_title.setText(getResources().getString(R.string.tour_guide));
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        map_btn=(Button)findViewById(R.id.map_btn);
        mapview = (ImageView) findViewById(R.id.mapview);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inty = new Intent(TourDetail.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(CricketStatium.this);

        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CricketStatium.this, TourGuideNearestStop.class);
                in.putExtra("KEY_LAT", "21.203451");
                in.putExtra("KEY_LNG", "81.823921");
                startActivity(in);
            }
        });

        mapview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CricketStatium.this, TourGuideNearestStop.class);
                in.putExtra("KEY_LAT", "21.203451");
                in.putExtra("KEY_LNG", "81.823921");
                startActivity(in);
            }

        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(21.203451, 81.823921)).zoom(12).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //show current location
        googleMap.setMyLocationEnabled(false); // false to disable
        // Zooming Buttons
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        //Zooming Functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
        //Compass Functionality
        googleMap.getUiSettings().setCompassEnabled(false);
        //My Location Button
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        //Map Rotate Gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
    }

        @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
