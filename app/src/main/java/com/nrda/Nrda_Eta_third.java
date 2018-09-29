package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.nrda.utils.Nrda_Url_Conts;
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

/**
 * Created by Diwash Choudhary on 3/6/2017.
 */
public class Nrda_Eta_third extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tool_title;
   /* private List<EtaBean> array_admin;
    EtaBean etaBean;*/
    ImageView tool_back_icon;
    ArrayList<HashMap<String, String>> ETData;
    ArrayList<String> shelterList;
    String tripid,vehicleid;
    String getSource, getDestination;
    List<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nrda_eta_recycle_second);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tool_title=(TextView)findViewById(R.id.appTitle);

        tool_title.setText(getResources().getString(R.string.detail_stop_list));
        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_Eta_third.this,Nrda_Route_Eta.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        Intent inty = getIntent();
        tripid=inty.getStringExtra("tripid");
        vehicleid=inty.getStringExtra("vehicleid");
        getSource = inty.getStringExtra("Source");
        getDestination = inty.getStringExtra("destination");

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        if (isConnection()) {
            new getBusTimingList().execute();

        }

    }
    class getBusTimingList extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Eta_third.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("vehicleid", vehicleid));
            nameValuePairs.add(new BasicNameValuePair("tripid", tripid));

            try {
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETETATRIPWISEDETAILS;

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httppost);
                getResponse = EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception ev) {
                ev.getStackTrace();
            }
            return getResponse;
        }

        @Override
        protected void onPostExecute(final String getResponse) {

            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(getResponse);
                        String success = jsonObject.getString("Success");
                        if (success.equalsIgnoreCase("true")) {
                            JSONArray jarray = jsonObject.getJSONArray("ETATripWiseDetails");

                            ETData = new ArrayList<>();
                            shelterList = new ArrayList<>();
                            for (int i = 0; i < jarray.length(); i++) {
                                HashMap<String, String> map = new HashMap<>();
                                JSONObject object = jarray.getJSONObject(i);
                                // map.put("KEY_SHELTERID", object.optString("shelterid"));
                                map.put("KEY_SHELTERNAME", object.optString("shelterName"));
                                // map.put("KEY_SHELTERNAME", object.optString("ShelterName"));
                                map.put("KEY_SCHEDULETIME", object.optString("etatime"));


                                /*try {
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(object.optString("etatime"));
                                    System.out.println(_24HourDt);
                                    System.out.println(_12HourSDF.format(_24HourDt));

                                    map.put("KEY_SCHEDULETIME", _12HourSDF.format(_24HourDt));
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }*/

                                shelterList.add(object.optString("shelterName"));
                                ETData.add(map);
                            }

                            int start_pos = shelterList.indexOf(getSource);
                            int end_pos = shelterList.indexOf(getDestination);

                            map = ETData.subList(start_pos, end_pos+1);
                            Myadapter mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);

                        }else {
                            String msg = jsonObject.getString("Message");
                            Toast.makeText(Nrda_Eta_third.this, R.string.no_record_found, Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }
                }
            });
            progressDialog.dismiss();
        }
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nrda_third_item, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if((position % 2 == 0)){
                holder.cardView.setBackgroundColor(Color.parseColor("#87CEFA"));
            }else{
                holder.cardView.setBackgroundColor(Color.parseColor("#EEE8AA"));
            }

            HashMap<String, String> mMap = new HashMap<String, String>();
            mMap = map.get(position);

            holder.route_name.setText(mMap.get("KEY_SHELTERNAME"));

            String source_name = map.get(0).get("KEY_SHELTERNAME");

            if(source_name.equalsIgnoreCase(mMap.get("KEY_SHELTERNAME"))){
                holder.from_to.setText(getResources().getString(R.string.estimated_depart) + mMap.get("KEY_SCHEDULETIME"));
            }else {
                holder.from_to.setText(getResources().getString(R.string.estimate_arrival) + mMap.get("KEY_SCHEDULETIME"));
            }
        }

        @Override
        public int getItemCount() {
            return map.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView route_name;
            protected TextView from_to;
            protected LinearLayout lin_main;
            protected CardView cardView;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                route_name = (TextView) itemLayputView.findViewById(R.id.route_name);
                from_to = (TextView) itemLayputView.findViewById(R.id.from_to);
                lin_main = (LinearLayout) itemLayputView.findViewById(R.id.lin_main);
                cardView = (CardView) itemLayputView.findViewById(R.id.card_view);
            }
        }
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_Eta_third.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_Eta_third.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
