
package com.nrda.smart_card;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Map;

public class SmartCardDetail extends AppCompatActivity {
    private TextView tool_title,name,mob_no,type_of_card,card_num,rec_amt,rec_type;
    private ImageView tool_back_icon;
    String tag_id,sucess;
    AQuery aQuery;
    String Tagid,Name,Mobile,RechargeType,RechargeAmount,CardType, color, card_code;
    private DatabaseRoute dbRoute;

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

        dbRoute = new DatabaseRoute(this);

        tag_id = getIntent().getStringExtra("tagid");
        aQuery = new AQuery(SmartCardDetail.this);
        otp_info();
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
    }

    private void otp_info() {
        String url = Nrda_Url_Conts.BASE_URL_two+Nrda_Url_Conts.GETCARDDETAIL;
        Map map = new HashMap();
        map.put("TagId", tag_id);


        ProgressDialog progressDialog = new ProgressDialog(SmartCardDetail.this);
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
                            color = objects.optString("Color");
                            card_code=objects.optString("Smartcardcode");

                            name.setText(Name);
                            mob_no.setText(Mobile);
                            type_of_card.setText(CardType);
                            card_num.setText(Tagid);
                            rec_amt.setText( " ₹  "+RechargeAmount);
                            rec_type.setText(RechargeType);

                            if (!dbRoute.searchTagId(Tagid)){
                                dbRoute.insertSmartCardDetail(new DatabaseSmartCardDetailModel(Tagid,Name,Mobile,RechargeType,RechargeAmount,CardType,color,card_code, "28-Aug-2022"));
                            }
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
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
