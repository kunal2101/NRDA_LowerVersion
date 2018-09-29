package com.nrda;
/**
 * Created by Diwash Choudhary on 05-011-2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class Nrda_AboutUs extends AppCompatActivity {
    private TextView tool_title, aboutUs;
    private ImageView tool_back_icon;
    private WebView abtUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrda__about_us);

        tool_title=(TextView)findViewById(R.id.appTitle);
        //aboutUs = (TextView) findViewById(R.id.aboutUs);
        abtUs = (WebView) findViewById(R.id.abtUs);

        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onbackClick();
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.about_us));

        abtUs.loadData(getResources().getString(R.string.about_uss),"text/html", "UTF-8");
    }

    protected void onbackClick()
    {
        Intent myIntent = new Intent();
        setResult(RESULT_OK,myIntent);
        super.finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
