package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Diwash Choudhary on 3/4/2017.
 */
public class Nrda_Route_Eta extends AppCompatActivity {

    SearchableSpinner spinner_source, spinner_destination;
    static ArrayList<HashMap<String,String>> sourcedataList;
    static ArrayList<String> sourcelist, destinationList;
    ArrayList<HashMap<String, String>> ETData;
    static String sourceId, destinationId;
    private ImageView tool_back_icon;
    private TextView tool_title, get_fare;
    private RecyclerView recyclerView;
    String getSource, getDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nrda_route_eta);

        tool_title=(TextView)findViewById(R.id.appTitle);

        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inty = new Intent(Nrda_Route_Eta.this,MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.estimated_time));
        get_fare=(TextView)findViewById(R.id.get_fare);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.INVISIBLE);

        get_fare.setText(getResources().getString(R.string.search));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        spinner_source = (SearchableSpinner)findViewById(R.id.spinner_source);
        spinner_destination = (SearchableSpinner)findViewById(R.id.spinner_destination);
        spinner_destination.setTitle(getResources().getString(R.string.please_select_destination));
        spinner_source.setTitle(getResources().getString(R.string.please_select_source));

        if (isConnection()) {
            new GetAllShelterDetail().execute();
        }
        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);
                getSource = getsource.getText().toString();

                for(int pqr=0; pqr < sourcedataList.size(); pqr++){
                    HashMap<String,String> map = sourcedataList.get(pqr);
                    String ss = map.get("KEY_NAME")+ " / "+ map.get("KEY_NAME_HI");
                    if(ss.equalsIgnoreCase(getSource)){
                        sourceId = map.get("KEY_ID");
                    }
                }

                destinationList = new ArrayList<String>();
                for(int aind = 0 ; aind < sourcelist.size(); aind ++){
                    if(! getSource.equalsIgnoreCase(sourcelist.get(aind))){
                        destinationList.add(sourcelist.get(aind));
                        Collections.reverse(destinationList);
                    }
                }

                ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_Route_Eta.this, R.layout.spinner_item, destinationList);
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
                getDestination = getsource.getText().toString();
                for(int pqr=0; pqr < sourcedataList.size(); pqr++){
                    HashMap<String,String> map = sourcedataList.get(pqr);
                    String dd = map.get("KEY_NAME")+ " / "+ map.get("KEY_NAME_HI");
                    if(dd.equalsIgnoreCase(getDestination)){
                        destinationId = map.get("KEY_ID");
                    }
                }

              //  Toast.makeText(Nrda_Route_Eta.this,"source :"+sourceId+"\nDestination :"+destinationId,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        get_fare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sourceId == null || sourceId.isEmpty()){
                    Toast.makeText(Nrda_Route_Eta.this, getResources().getString(R.string.select_start_station), Toast.LENGTH_SHORT).show();
                }else if (destinationId == null || destinationId.isEmpty()){
                    Toast.makeText(Nrda_Route_Eta.this, getResources().getString(R.string.select_end_station), Toast.LENGTH_SHORT).show();
                }else if(sourceId.equalsIgnoreCase(destinationId)){
                    Toast.makeText(Nrda_Route_Eta.this, getResources().getString(R.string.source_destination_same), Toast.LENGTH_SHORT).show();
                }else if (isConnection())  {
                    new getEtaRouteDetaait().execute();
                }
            }
        });
    }


    class GetAllShelterDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Route_Eta.this, "Loading...", "Please Wait...", true);
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

            try{
                JSONObject jsonObj = new JSONObject(getResponse);
                String success = jsonObj.getString("Success");
                sourcedataList = new ArrayList<>();
                sourcelist = new ArrayList<>();

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
                    ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_Route_Eta.this, R.layout.spinner_item, sourcelist);
                    spinner_source.setAdapter(adapters);

                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
            progressDialog.dismiss();
        }
    }

    class getEtaRouteDetaait extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Route_Eta.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("sourceid", sourceId));
            nameValuePairs.add(new BasicNameValuePair("destinationid", destinationId));

            try {
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETETAROUTEDETAIL;

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

            final ArrayList<String> routeIdsArr = new ArrayList<>();
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(getResponse);
                        String success = jsonObject.getString("Success");
                        if (success.equalsIgnoreCase("true")) {
                            JSONArray jarray = jsonObject.getJSONArray("ETADetails");

                            ETData = new ArrayList<>();
                            for (int i = 0; i < jarray.length(); i++) {
                                HashMap<String, String> map = new HashMap<>();
                                JSONObject object = jarray.getJSONObject(i);
                                map.put("KEY_ROUTEID", object.optString("routeid"));
                                map.put("KEY_ROUTENO", object.optString("routeNo"));
                                map.put("KEY_ROUTENAME", object.optString("routeName"));
                                map.put("KEY_ROUTETYPE", object.optString("routetype"));
                                routeIdsArr.add(object.optString("routeid"));
                                ETData.add(map);
                            }
                             /*Myadapter mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setVisibility(View.VISIBLE);*/

                        } else {
                            String msg = jsonObject.getString("Message");
                            Toast.makeText(Nrda_Route_Eta.this, "Sorry! no record found", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }

                }
            });
            progressDialog.dismiss();

            if(routeIdsArr != null && routeIdsArr.size() > 0){
                String joinedRouteIds = TextUtils.join(",", routeIdsArr);
                Intent inty = new Intent(Nrda_Route_Eta.this, Nrda_Eta_Second.class);
                inty.putExtra("routeid", joinedRouteIds);
                inty.putExtra("Source", getSource);
                inty.putExtra("destination", getDestination);
                startActivity(inty);
                finish();
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        }
    }
   /* public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        String routeId;
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nrda_eta_list, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            HashMap<String, String> map = new HashMap<String, String>();
            map = ETData.get(position);

            holder.route_name.setText("Route No. - " + map.get("KEY_ROUTENO"));
            holder.from_to.setText(map.get("KEY_ROUTENAME"));
            routeId = map.get("KEY_ROUTEID");

            holder.lin_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inty = new Intent(Nrda_Route_Eta.this, Nrda_Eta_Second.class);
                    inty.putExtra("routeid", routeId);
                    startActivity(inty);
                    finish();
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            });
        }


        @Override
        public int getItemCount() {
            return ETData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView route_name;
            protected TextView from_to;
            protected CardView lin_main;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                route_name = (TextView) itemLayputView.findViewById(R.id.route_name);
                from_to = (TextView) itemLayputView.findViewById(R.id.from_to);
                lin_main = (CardView) itemLayputView.findViewById(R.id.lin_main);
            }
        }
    }*/
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_Route_Eta.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_Route_Eta.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
