package com.nrda.smart_card;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nrda.Nrda_Fare_cal;
import com.nrda.R;
import com.nrda.utils.Nrda_Url_Conts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Smart_mobile_veri extends AppCompatActivity {
    ImageView btnNext;
    EditText mob_num;
    String tadid,sucess;
    AQuery aQuery;
    private TextView tool_title;
    private ImageView tool_back_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_mobile_veri);

        btnNext = (ImageView) findViewById(R.id.btnNext);
        mob_num = (EditText)findViewById(R.id.mob_num);
        tool_title          = (TextView) findViewById(R.id.appTitle);
        tool_back_icon      = (ImageView) findViewById(R.id.tool_back_icon);

        aQuery = new AQuery(Smart_mobile_veri.this);

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

        tool_title.setText(R.string.mobile_verify);

        try {
            tadid = getIntent().getStringExtra("tagid");
           // Toast.makeText(this, ""+tadid, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getMessage();
        }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mob_num.getText().toString().length()==10){
                    if (isConnection()) {
                        mob_info();
                    }
                }else {
                    Toast.makeText(Smart_mobile_veri.this, "Invalid Mobile Number ..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void mob_info() {
        String url = Nrda_Url_Conts.BASE_URL_two+Nrda_Url_Conts.GETOTP;
        Map map = new HashMap();
        map.put("TagId", tadid);
        map.put("MobileNo", mob_num.getText().toString());
        ProgressDialog progressDialog = new ProgressDialog(Smart_mobile_veri.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);
        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    sucess = object.getString("Success");
                    if (sucess.equalsIgnoreCase("true")) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Otp Sent Successfully", Snackbar.LENGTH_LONG).show();
                        Intent inty = new Intent(Smart_mobile_veri.this, SmartCard_Otp.class);
                        inty.putExtra("mob_no",mob_num.getText().toString());
                        inty.putExtra("tagid",tadid);
                        startActivity(inty);
                        finish();

                    } else {
                        //Toast.makeText(getApplicationContext(),"No Fare Available for this Route ",Toast.LENGTH_LONG).show();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Invalid Input", Snackbar.LENGTH_LONG).show();
                       // Toast.makeText(Smart_mobile_veri.this, "Invalid Input", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Smart_mobile_veri.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Smart_mobile_veri.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
