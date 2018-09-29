package com.nrda.smart_card;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nrda.R;
import com.nrda.utils.Nrda_Url_Conts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SmartCardSearch extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    private Button search_btn_main;
    private EditText card_textview;
    String tag_id,sucess;
    AQuery aQuery;
    String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_card_search);
        search_btn_main= (Button)findViewById(R.id.search_btn_main);
        tool_title = (TextView) findViewById(R.id.appTitle);
        card_textview = (EditText)findViewById(R.id.card_textview);
        try {
            flag = getIntent().getStringExtra("card");
        }catch (Exception e){
            e.getMessage();
        }
        aQuery = new AQuery(SmartCardSearch.this);

        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
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

        tool_title.setText(getResources().getString(R.string.smart_card_search));
        search_btn_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (card_textview.getText().toString().length() == 1) {
                    Toast.makeText(SmartCardSearch.this, "Please Enter your Smart Card Number", Toast.LENGTH_SHORT).show();
                } else if (card_textview.getText().toString().length() < 7) {
                    Toast.makeText(SmartCardSearch.this, "Invalid Card Number..", Toast.LENGTH_SHORT).show();
                } else {
                    card_info();

                }
            }
        });
    }

    private void card_info() {
        String url = Nrda_Url_Conts.BASE_URL_two+Nrda_Url_Conts.GETCARDDETAIL;
        Map map = new HashMap();
        map.put("TagId", card_textview.getText().toString());


        ProgressDialog progressDialog = new ProgressDialog(SmartCardSearch.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);
        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    sucess = object.getString("Success");
                    if (sucess.equalsIgnoreCase("true")) {
                        if(flag.equalsIgnoreCase("1") ) {

                            if (card_textview.getText().toString().length() == 1) {
                                Toast.makeText(SmartCardSearch.this, "Please Enter your Smart Card Number", Toast.LENGTH_SHORT).show();
                            } else if (card_textview.getText().toString().length() < 7) {
                                Toast.makeText(SmartCardSearch.this, "Invalid Card Number..", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent inty = new Intent(SmartCardSearch.this, Smart_mobile_veri.class);
                                inty.putExtra("tagid", card_textview.getText().toString());
                                startActivity(inty);
                                finish();
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                            }
                        }else {
                            if (card_textview.getText().toString().length() == 2) {
                                Toast.makeText(SmartCardSearch.this, "Please Enter your Smart Card Number", Toast.LENGTH_SHORT).show();
                            } else if (card_textview.getText().toString().length() < 7) {
                                Toast.makeText(SmartCardSearch.this, "Invalid Card Number..", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent inty = new Intent(SmartCardSearch.this, DetailPage.class);
                                inty.putExtra("tagid", card_textview.getText().toString());
                                startActivity(inty);
                                finish();
                                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

                            }
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),"Invalid Card Number ",Toast.LENGTH_LONG).show();
                       // Snackbar.make(getWindow().getDecorView().getRootView(), "Invalid Input", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(Smart_mobile_veri.this, "Invalid Input", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
