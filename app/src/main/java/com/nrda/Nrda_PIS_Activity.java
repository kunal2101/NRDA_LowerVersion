package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nrda.utils.Nrda_Url_Conts;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Nrda_PIS_Activity extends AppCompatActivity {
    SearchableSpinner spinner_source;

    private TextView tool_title, getTiming, txt_stopag, txt_stopage;
    private TextView txt_up_route1, txt_up_routename1, txt_up_time1, txt_up_route2, txt_up_routename2, txt_up_time2, txt_down_routeno1;
    private TextView txt_down_routename1, txt_down_time1, txt_down_routeno2, txt_down_routename2, txt_down_time2;
    private ImageView tool_back_icon;
    static ArrayList<HashMap<String,String>> sourcedataList;
    static ArrayList<String> sourcelist, destinationList;
    static String sourceId, destinationId;
    LinearLayout lin_one, lin_two;
    CardView ln_source, ln_destination;
    String getValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pis__new);
        spinner_source  = (SearchableSpinner) findViewById(R.id.spinner_source);
        txt_stopag      = (TextView) findViewById(R.id.txt_stopag);
        txt_stopage     = (TextView) findViewById(R.id.txt_stopage);
        tool_title      = (TextView) findViewById(R.id.appTitle);
        tool_back_icon  = (ImageView) findViewById(R.id.tool_back_icon);

        txt_up_route1           = (TextView) findViewById(R.id.txt_up_route1);
        txt_up_routename1       = (TextView) findViewById(R.id.txt_up_routename1);
        txt_up_route2           = (TextView) findViewById(R.id.txt_up_route2);
        txt_up_routename2       = (TextView) findViewById(R.id.txt_up_routename2);
        txt_up_time2            = (TextView) findViewById(R.id.txt_up_time2);
        txt_down_routeno1       = (TextView) findViewById(R.id.txt_down_routeno1);
        txt_down_routename1     = (TextView) findViewById(R.id.txt_down_routename1);
        txt_down_time1          = (TextView) findViewById(R.id.txt_down_time1);
        txt_down_routeno2       = (TextView) findViewById(R.id.txt_down_routeno2);
        txt_down_routename2     = (TextView) findViewById(R.id.txt_down_routename2);
        txt_down_time2          = (TextView) findViewById(R.id.txt_down_time2);
        txt_up_time1            = (TextView) findViewById(R.id.txt_up_time1);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_Bus_Timin.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText(getResources().getString(R.string.pis_informtion));
        lin_one = (LinearLayout) findViewById(R.id.lin_one);
        lin_two = (LinearLayout) findViewById(R.id.lin_two);
        ln_source = (CardView) findViewById(R.id.ln_source);
        ln_destination = (CardView) findViewById(R.id.ln_destination);
        getTiming = (TextView) findViewById(R.id.getTiming);

        spinner_source.setTitle(getResources().getString(R.string.select_bus_stop));
        if (isConnection()) {
            new GetAllShelterDetail().execute();
        }

        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);
                 getValue = getsource.getText().toString();
               /* txt_stopage.setText(getValue);
                txt_stopag.setText(getValue);*/

                for(int pqr=0; pqr < sourcedataList.size(); pqr++){
                    HashMap<String,String> map = sourcedataList.get(pqr);
                    String ss = map.get("KEY_NAME")+ " / "+ map.get("KEY_NAME_HI");
                    if(ss.equalsIgnoreCase(getValue)){
                        sourceId = map.get("KEY_ID");
                    }
                }

                /*destinationList = new ArrayList<String>();
                for(int aind = 0 ; aind < sourcelist.size(); aind ++){
                    if(! getValue.equalsIgnoreCase(sourcelist.get(aind))){
                        destinationList.add(sourcelist.get(aind));
                        Collections.reverse(destinationList);
                    }
                }*/

               /* ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_Bus_Timin.this, R.layout.spinner_item, destinationList);
                spinner_destination.setAdapter(adapters);*/
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_one.setVisibility(View.GONE);
                lin_two.setVisibility(View.GONE);
                ln_source.setVisibility(View.GONE);
                ln_destination.setVisibility(View.GONE);

                if (sourceId == null || sourceId.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.please_select_source,Toast.LENGTH_LONG).show();
                }else if (isConnection()){
                    new GetBusTiming().execute();
                }
            }
        });
    }
    class GetAllShelterDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_PIS_Activity.this, "Loading...", "Please Wait...", true);

        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL+ Nrda_Url_Conts.ALLSHELTERDETAIL;
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
            try{
                JSONObject jsonObj = new JSONObject(getResponse);
                String success = jsonObj.getString("Success");
                sourcedataList = new ArrayList<>();
                sourcelist = new ArrayList<>();

                sourcelist.clear();
                sourcedataList.clear();

                if(success.equalsIgnoreCase("true")){

                    JSONArray jarray = jsonObj.getJSONArray("ShelterDetails");

                    for (int i = 0; i < jarray.length(); i++) {

                        HashMap<String, String> map = new HashMap<>();
                        JSONObject object = jarray.getJSONObject(i);
                        map.put("KEY_ID",object.optString("shelterid"));
                        map.put("KEY_NAME",object.optString("ShelterName"));
                        map.put("KEY_SEQNUCE",object.optString("SequenceNo"));
                        map.put("KEY_NAME_HI",object.optString("ShelterName_hi"));

                        sourcedataList.add(map);
                        sourcelist.add(object.optString("ShelterName")+" / "+object.optString("ShelterName_hi"));

                    }
                    ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_PIS_Activity.this,  R.layout.spinner_item, sourcelist);
                    spinner_source.setAdapter(adapters);
                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
        }
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_PIS_Activity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_PIS_Activity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    class GetBusTiming extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String getResponse = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_PIS_Activity.this, "Loading...", "Please Wait...", true);
        }

        protected String doInBackground(String... args) {


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("shelterid", sourceId));

            try {

                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL+ Nrda_Url_Conts.GETREALTIMEPISNEW;
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {
            progressDialog.dismiss();
            try{
                JSONObject jsonObj = new JSONObject(getResponse);
                String success = jsonObj.getString("Success");

                if(success.equalsIgnoreCase("true")){

                    JSONArray jarray = jsonObj.getJSONArray("pisdetails");
                    for (int ind = 0; ind < jarray.length(); ind++) {

                        JSONObject objPis = jarray.getJSONObject(ind);

                        if(ind == 0){
                            String sheltername = objPis.getString("sheltername");

                            JSONArray jsArr = objPis.getJSONArray("vehicle_info");
                            String uptime1 = jsArr.getJSONObject(0).getString("time");
                            String uptime2 = jsArr.getJSONObject(1).getString("time");
                            String uproutenum1 = jsArr.getJSONObject(0).getString("routename");
                            String uproutenum2 = jsArr.getJSONObject(1).getString("routename");

                            txt_up_route1.setText(uproutenum1);
                            txt_up_route2.setText(uproutenum2);
                            txt_up_time1.setText(uptime1);
                            txt_up_time2.setText(uptime2);
                            txt_stopage.setText(sheltername);

                        }else if(ind == 1){
                            String sheltername = objPis.getString("sheltername");

                            JSONArray jsArr1 = objPis.getJSONArray("vehicle_info");
                            String downtime1 = jsArr1.getJSONObject(0).getString("time");
                            String downtime2 = jsArr1.getJSONObject(1).getString("time");
                            String downroutenum1 = jsArr1.getJSONObject(0).getString("routename");
                            String downroutenum2 = jsArr1.getJSONObject(1).getString("routename");

                            txt_down_time1.setText(downtime1);
                            txt_down_time2.setText(downtime2);
                            txt_down_routeno1.setText(downroutenum1);
                            txt_down_routeno2.setText(downroutenum2);
                            txt_stopag.setText(sheltername);

                        }
                    }
                    lin_one.setVisibility(View.VISIBLE);
                    lin_two.setVisibility(View.VISIBLE);
                    ln_source.setVisibility(View.VISIBLE);
                    ln_destination.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(Nrda_PIS_Activity.this, "Sorry! no information is available", Toast.LENGTH_SHORT).show();
                    lin_one.setVisibility(View.GONE);
                    lin_two.setVisibility(View.GONE);
                    ln_source.setVisibility(View.GONE);
                    ln_destination.setVisibility(View.GONE);
                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
        }
    }

}
