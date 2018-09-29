package com.nrda.smart_card;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nrda.R;
import com.nrda.database.DatabaseRoute;
import com.nrda.database.DatabaseSmartCardDetailModel;
import com.nrda.utils.Nrda_Url_Conts;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmartCardDetail_View extends AppCompatActivity {

    private TextView tool_title,name,mob_no,type_of_card,card_num,rec_amt,rec_type, txtvalid;
    private ImageView tool_back_icon;
    String tag_id,sucess;
    AQuery aQuery;
    String Tagid,Name,Mobile,RechargeType,RechargeAmount,CardType;
    private DatabaseRoute dbroute;
    ArrayList<DatabaseSmartCardDetailModel> getDatalist ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_card_detail);

        tool_title = (TextView) findViewById(R.id.appTitle);
        name = (TextView) findViewById(R.id.name);
        mob_no = (TextView) findViewById(R.id.mob_no);
        type_of_card = (TextView) findViewById(R.id.type_of_card);
        card_num = (TextView) findViewById(R.id.card_num);
        rec_amt = (TextView) findViewById(R.id.rec_amt);
        rec_type = (TextView) findViewById(R.id.rec_type);
        tool_title = (TextView) findViewById(R.id.appTitle);
        txtvalid = (TextView) findViewById(R.id.txtvalid);

        try {
            tag_id = getIntent().getStringExtra("tagid");
        }catch (Exception ev){
            ev.getMessage();
        }

            dbroute = new DatabaseRoute(this);

        aQuery = new AQuery(SmartCardDetail_View.this);

       /* if (isConnection()) {
            otp_info();
        }*/

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

        tool_title.setText(getResources().getString(R.string.smart_card));

        getDatalist = dbroute.Get_SelectedRecord(tag_id);

        for (int i = 0; i< getDatalist.size(); i++) {

            DatabaseSmartCardDetailModel dbCard = getDatalist.get(i);

            name.setText(dbCard.getName());
            mob_no.setText(dbCard.getMobile());
            type_of_card.setText(dbCard.getCard_type());
            card_num.setText(dbCard.getTag_id());
            rec_amt.setText("₹ " +dbCard.getRecharge_amount());
            rec_type.setText(dbCard.getRecharge_type());
            txtvalid.setText(dbCard.getValidity());
        }
    }

   /* private void otp_info() {
        String url = Nrda_Url_Conts.BASE_URL_two+Nrda_Url_Conts.GETCARDDETAIL;
        Map map = new HashMap();
        map.put("TagId", tag_id);


        ProgressDialog progressDialog = new ProgressDialog(SmartCardDetail_View.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);
        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    sucess = object.getString("Success");
                    if (sucess.equalsIgnoreCase("true")) {

                        JSONArray jarray = object.getJSONArray("CardDetails");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject objects = jarray.getJSONObject(0);
                            Tagid = objects.getString("Tagid");
                            Name = objects.getString("Name");
                            Mobile = objects.getString("Mobile");
                            RechargeType = objects.getString("RechargeType");
                            RechargeAmount = objects.getString("RechargeAmount");
                            CardType = objects.getString("CardType");
                            name.setText(Name);
                            mob_no.setText(Mobile);
                            type_of_card.setText(RechargeType);
                            card_num.setText(Tagid);
                            rec_amt.setText("₹ "+RechargeAmount);
                            rec_type.setText(CardType);


                            // tot_dist.setText(tot_distance + "\tKm" );
                        }
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Otp Verify Successfully", Snackbar.LENGTH_LONG).show();


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
    }*/
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) SmartCardDetail_View.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(SmartCardDetail_View.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
