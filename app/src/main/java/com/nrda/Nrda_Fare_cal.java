package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nrda.utils.Nrda_Url_Conts;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Diwash Choudhary on 3/3/2017.
 */

public class Nrda_Fare_cal extends AppCompatActivity {
    //EditText sour_edit;
    TextView get_fare, tool_title, btn_ticket;
    TextView amt_total,tot_dist;
    LinearLayout ly_fare, ly_dist, output;
    private ImageView tool_back_icon;
    AQuery aQuery;
    SearchableSpinner spinner_destination, spinner_source;
    static ArrayList<HashMap<String,String>> sourcedataList;
    static ArrayList<String> sourcelist, destinationList;
    static String sourceId, destinationId,sucess;
    String getSValue,getdValue, fare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nrda_fare_calcu);

        aQuery = new AQuery(Nrda_Fare_cal.this);

        //sour_edit               = (EditText) findViewById(R.id.sour_edit);
        get_fare                = (TextView) findViewById(R.id.get_fare);
        ly_dist                 = (LinearLayout) findViewById(R.id.ly_dist);
        ly_fare                 = (LinearLayout) findViewById(R.id.ly_fare);
        output                  = (LinearLayout) findViewById(R.id.output);
        amt_total               = (TextView) findViewById(R.id.amt_total);
        tot_dist                = (TextView) findViewById(R.id.tot_dist);
        btn_ticket              = (TextView) findViewById(R.id.btn_ticket);
        spinner_destination     = (SearchableSpinner) findViewById(R.id.spinner_destination);
        spinner_source          = (SearchableSpinner) findViewById(R.id.spinner_source);

        tool_title              = (TextView)findViewById(R.id.appTitle);
        tool_back_icon          = (ImageView) findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inty = new Intent(Nrda_Fare_cal.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText(getResources().getString(R.string.fare_estimate));

        get_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amt_total.setVisibility(View.INVISIBLE);
                tot_dist.setVisibility(View.INVISIBLE);
                output.setVisibility(View.INVISIBLE);
                btn_ticket.setVisibility(View.INVISIBLE);

                if (sourceId == null || sourceId.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.please_select_source,Toast.LENGTH_LONG).show();
                }else if (destinationId == null || destinationId.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.please_select_destination,Toast.LENGTH_LONG).show();
                }else if(sourceId.equalsIgnoreCase(destinationId)){
                    Toast.makeText(getApplicationContext(), R.string.source_destination_same,Toast.LENGTH_LONG).show();
                }else if(isConnection()) {

                        fare_info();
                }
            }
        });

        spinner_destination.setTitle(getString(R.string.select_destination));
        spinner_source.setTitle(getString(R.string.select_source));

        if (isConnection()) {
            new GetAllShelterDetail().execute();
        }
        /*sour_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner_destination.performClick();
            }
        });*/

        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);

                getSValue = getsource.getText().toString();

                for(int pqr=0; pqr < sourcedataList.size(); pqr++){
                    HashMap<String,String> map = sourcedataList.get(pqr);
                    String ss = map.get("KEY_NAME")+ " / "+ map.get("KEY_NAME_HI");
                    if(ss.equalsIgnoreCase(getSValue)){
                        sourceId = map.get("KEY_ID");
                        //Toast.makeText(Nrda_Fare_cal.this, "ID: " + sourceId, Toast.LENGTH_SHORT).show();
                    }
                }

                destinationList = new ArrayList<String>();
                for(int aind = 0 ; aind < sourcelist.size(); aind ++){
                    if(! getSValue.equalsIgnoreCase(sourcelist.get(aind))){
                        destinationList.add(sourcelist.get(aind));
                        Collections.reverse(destinationList);
                    }
                }

                ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_Fare_cal.this, R.layout.spinner_item, destinationList);
                spinner_destination.setAdapter(adapters);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);
                getdValue = getsource.getText().toString();

                for(int pqr=0; pqr < sourcedataList.size(); pqr++){

                    HashMap<String,String> map = sourcedataList.get(pqr);
                    String dd = map.get("KEY_NAME")+ " / "+ map.get("KEY_NAME_HI");
                    if(dd.equalsIgnoreCase(getdValue)){
                        destinationId = map.get("KEY_ID");
                        //Toast.makeText(Nrda_Fare_cal.this, "ID: " + destinationId, Toast.LENGTH_SHORT).show();
                    }
                }
                //  Toast.makeText(Nrda_Route_Eta.this,"source :"+sourceId+"\nDestination :"+destinationId,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Nrda_Fare_cal.this, TicketBooking.class);
                in.putExtra("source",getSValue);
                in.putExtra("destination",getdValue);
                in.putExtra("fare", fare);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        });
    }

    private void fare_info() {
        String url = Nrda_Url_Conts.FARE_CAL;
        Map map = new HashMap();
        map.put("sourceid", sourceId);
        map.put("destinationid", destinationId);
        ProgressDialog progressDialog = new ProgressDialog(Nrda_Fare_cal.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);
        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                    try {
                         sucess = object.getString("Success");
                        if (sucess.equalsIgnoreCase("true")) {
                            JSONArray jarray = object.getJSONArray("FareEstimationDetails");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject objects = jarray.getJSONObject(0);
                                 fare = objects.getString("fare");
                               // String directions = objects.getString("direction");
                                String tot_distance = objects.getString("distance");
                                amt_total.setVisibility(View.VISIBLE);
                                //tot_dist.setVisibility(View.VISIBLE);
                                output.setVisibility(View.VISIBLE);
                                btn_ticket.setVisibility(View.INVISIBLE);
                                amt_total.setText("₹\t" + fare);
                               // tot_dist.setText(tot_distance + "\tKm" );
                            }
                        } else {
                        //Toast.makeText(getApplicationContext(),"No Fare Available for this Route ",Toast.LENGTH_LONG).show();
                            Snackbar.make(getWindow().getDecorView().getRootView(), getResources().getString(R.string.no_fare_available), Snackbar.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_Fare_cal.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_Fare_cal.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    class GetAllShelterDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Fare_cal.this, "Loading...", "Please Wait...", true);
        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.ALLSHELTERDETAIL;
                HttpPost httppost = new HttpPost(url);

                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {
            progressDialog.dismiss();
            try {
                JSONObject jsonObj = new JSONObject(getResponse);
                String success = jsonObj.getString("Success");

                sourcedataList = new ArrayList<>();
                sourcelist = new ArrayList<>();

                if (success.equalsIgnoreCase("true")) {
                    JSONArray jarray = jsonObj.getJSONArray("ShelterDetails");
                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject object = jarray.getJSONObject(i);
                        map.put("KEY_ID", object.optString("shelterid"));
                        map.put("KEY_NAME", object.optString("ShelterName"));
                        map.put("KEY_SEQNUCE",object.optString("SequenceNo"));
                        map.put("KEY_NAME_HI",object.optString("ShelterName_hi"));

                        sourcedataList.add(map);
                        sourcelist.add(object.optString("ShelterName")+" / "+object.optString("ShelterName_hi"));
                    }
                    ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_Fare_cal.this, R.layout.spinner_item , sourcelist);
                    spinner_source.setAdapter(adapters);
                }

            } catch (Exception ev) {
                Log.e("response issue", ev.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}

