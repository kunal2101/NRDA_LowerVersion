package com.nrda.smart_card;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nrda.R;

public class SmartCardDashBoard extends AppCompatActivity {

    AppCompatButton btn_addcard, btn_viewcard,btn_viewcard_validity;
    private TextView tool_title;
    private ImageView tool_back_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_card_dash_board);

        btn_addcard             = (AppCompatButton)findViewById(R.id.btn_addcard);
        btn_viewcard            = (AppCompatButton)findViewById(R.id.btn_viewcard);
        btn_viewcard_validity   = (AppCompatButton)findViewById(R.id.btn_viewcard_validity);
        tool_title              = (TextView) findViewById(R.id.appTitle);

        tool_back_icon          = (ImageView) findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_ImageGallery.this,MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.smart_card_detail));

        btn_addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(SmartCardDashBoard.this,SmartCardSearch.class);
                inty.putExtra("card","1");
                startActivity(inty);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        btn_viewcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(SmartCardDashBoard.this,Smart_View_Card.class);
                startActivity(inty);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        btn_viewcard_validity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inty = new Intent(SmartCardDashBoard.this,SmartCardSearch.class);
                inty.putExtra("card","2");
                startActivity(inty);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
