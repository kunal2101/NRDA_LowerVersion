package com.nrda.tourguide;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nrda.R;
import com.nrda.TourDetail;
import com.nrda.Tour_Sec_deatil;

public class Other_tour_guide_Start extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    CardView card_view_one;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_tour_guide__start);
        tool_title = (TextView) findViewById(R.id.appTitle);
        tool_title.setText(getResources().getString(R.string.tour_guide));
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        card_view_one = (CardView)findViewById(R.id.card_view_one) ;
        card_view_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(Other_tour_guide_Start.this, Other_tour_guide.class);
                startActivity(inty);
                //finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
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

