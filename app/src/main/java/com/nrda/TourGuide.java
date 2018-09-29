package com.nrda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nrda.tourguide.Cafe_tour_start;
import com.nrda.tourguide.EntertentmantZone;
import com.nrda.tourguide.Market_tour_strat;
import com.nrda.tourguide.Other_tour_guide_Start;

/**
 * Created by kunalkumar on 04/04/18.
 */

public class TourGuide extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    LinearLayout llwhattosee, entertentment, other , cafeteria, market ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_guide);
        llwhattosee =(LinearLayout)findViewById(R.id.llwhattosee);
        entertentment =(LinearLayout)findViewById(R.id.entertentment);
        other =(LinearLayout)findViewById(R.id.other);
        cafeteria = (LinearLayout)findViewById(R.id.cafeteria);
        market = (LinearLayout)findViewById(R.id.market);

        llwhattosee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(TourGuide.this, Tour_Sec_deatil.class);
                startActivity(inty);
              //  finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        entertentment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(TourGuide.this, EntertentmantZone.class);
                startActivity(inty);
                //  finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(TourGuide.this, Other_tour_guide_Start.class);
                startActivity(inty);
                //  finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        cafeteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(TourGuide.this, Cafe_tour_start.class);
                startActivity(inty);
                //  finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(TourGuide.this, Market_tour_strat.class);
                startActivity(inty);
                //  finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });


        tool_title = (TextView) findViewById(R.id.appTitle);
        tool_title.setText(getResources().getString(R.string.tour_guide));
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inty = new Intent(TourGuide.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}


