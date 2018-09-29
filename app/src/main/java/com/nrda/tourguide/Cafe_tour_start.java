package com.nrda.tourguide;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nrda.R;

public class Cafe_tour_start extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    CardView card_view_one,card_view_two,card_view_three,card_view_four,card_view_five;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe_tour_start);
        tool_title = (TextView) findViewById(R.id.appTitle);
        tool_title.setText(getResources().getString(R.string.tour_guide));
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        card_view_one = (CardView)findViewById(R.id.card_view_one) ;
        card_view_two = (CardView)findViewById(R.id.card_view_two) ;
        card_view_three = (CardView)findViewById(R.id.card_view_three) ;
        card_view_four = (CardView)findViewById(R.id.card_view_four) ;
        card_view_five = (CardView)findViewById(R.id.card_view_five) ;

        card_view_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Cafe_tour_start.this, Cafe_tour_detail.class);
                startActivity(inty);
                //finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                Toast.makeText(Cafe_tour_start.this, getResources().getString(R.string.no_info), Toast.LENGTH_SHORT).show();
            }
        });
        card_view_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Cafe_tour_start.this, Cafe_tour_detail.class);
                startActivity(inty);
                //finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                Toast.makeText(Cafe_tour_start.this, getResources().getString(R.string.no_info), Toast.LENGTH_SHORT).show();
            }
        });
        card_view_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Cafe_tour_start.this, Cafe_tour_detail.class);
                startActivity(inty);
                //finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                Toast.makeText(Cafe_tour_start.this, getResources().getString(R.string.no_info), Toast.LENGTH_SHORT).show();
            }
        });
        card_view_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Cafe_tour_start.this, Cafe_tour_detail.class);
                startActivity(inty);
                //finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                Toast.makeText(Cafe_tour_start.this, getResources().getString(R.string.no_info), Toast.LENGTH_SHORT).show();
            }
        });
        card_view_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Cafe_tour_start.this, Cafe_tour_detail.class);
                startActivity(inty);
                //finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);*/
                Toast.makeText(Cafe_tour_start.this, getResources().getString(R.string.no_info), Toast.LENGTH_SHORT).show();
            }
        });
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent inty = new Intent(Tour_Sec_deatil.this, MainActivity.class);
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
