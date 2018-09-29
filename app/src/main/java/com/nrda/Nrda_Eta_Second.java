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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nrda.database.DatabaseRoutemodel;
import com.nrda.utils.Nrda_Url_Conts;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Diwash Choudhary on 3/6/2017.
 */
public class Nrda_Eta_Second extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tool_title;
    private ImageView tool_back_icon;
    private Myadapter mAdapter;
    ArrayList<HashMap<String, String>> ETData;
    String route,routeid;
    private CardView noPosts;
    ArrayList<String> routeIdsArr;
    String getSource, getDestination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nrda_eta_recycle_second);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        tool_title=(TextView)findViewById(R.id.appTitle);
        noPosts= (CardView)findViewById(R.id.noPosts);

        tool_title.setText(getResources().getString(R.string.route_info));
        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inty = new Intent(Nrda_Eta_Second.this,Nrda_Route_Eta.class);
                startActivity(inty);
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        routeid=intent.getStringExtra("routeid");
        getSource = intent.getStringExtra("Source");
        getDestination = intent.getStringExtra("destination");

        if (isConnection()) {
            routeIdsArr = new ArrayList<>();
            ETData = new ArrayList<>();
            routeIdsArr = new ArrayList<String>(Arrays.asList(routeid.split(",")));

            if(routeIdsArr!= null && routeIdsArr.size()>0){
                for(int aind =0; aind < routeIdsArr.size(); aind++) {
                    new getBusTimingList().execute(routeIdsArr.get(aind));
                }
            }
        }
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

    }

    class getBusTimingList extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Eta_Second.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("routeid", params[0]));

            try {
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETROUTETRIPDETAILBYRID;

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
                            JSONArray jarray = jsonObject.getJSONArray("TripDetailsbyRID");
                            ArrayList<String> vehno = new ArrayList<>();
                            //ETData = new ArrayList<>();
                            for (int i = 0; i < jarray.length(); i++) {

                                HashMap<String, String> map = new HashMap<>();
                                JSONObject object = jarray.getJSONObject(i);
                                String vechWithRoute = object.optString("tripbegintime")+""+object.optString("vehicleno");

                                map.put("KEY_ROUTENAME", object.optString("routename"));
                                map.put("KEY_VEHICLENO", object.optString("vehicleno"));
                                map.put("KEY_SHELTER_ORIGIN_NAME", object.optString("triporiginname"));
                                map.put("KEY_SHELTERID", object.optString("tripid"));
                                map.put("KEY_SHELTERVEHICLEID", object.optString("vehicleid"));
                                map.put("KEY_SHELTER_DESTINATON_NAME", object.optString("tripdestinationname"));
                               // map.put("KEY_ETATIME", object.optString("tripbegintime"));
                                try {
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(object.optString("tripbegintime"));
                                    System.out.println(_24HourDt);
                                    System.out.println(_12HourSDF.format(_24HourDt));

                                    map.put("KEY_ETATIME", _12HourSDF.format(_24HourDt));
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }

                                if (!vehno.contains(vechWithRoute)) {
                                    ETData.add(map);
                                    vehno.add(vechWithRoute);
                                }
                            }

                            mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setVisibility(View.VISIBLE);

                        }else {
                            String msg = jsonObject.getString("Message");
                            //Toast.makeText(Nrda_Eta_Second.this, msg, Toast.LENGTH_LONG).show();
                            noPosts.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }
                }
            });
            progressDialog.dismiss();
        }
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_Eta_Second.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_Eta_Second.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent inty = new Intent(Nrda_Eta_Second.this,Nrda_Route_Eta.class);
        startActivity(inty);
        finish();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.nrda_eta_list_two_item, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Collections.sort(ETData, new Comparator<HashMap<String, String>>() {

                @Override
                public int compare(HashMap<String, String> t1, HashMap<String, String> t2) {
                    int returnVal = 0;
                    try{
                        returnVal = new SimpleDateFormat("hh:mm a").parse(t1.get("KEY_ETATIME")).compareTo(new SimpleDateFormat("hh:mm a").parse(t2.get("KEY_ETATIME")));
                    }catch (Exception ev){

                    }
                    return  returnVal;
                }
            });


            HashMap<String, String> map = new HashMap<String, String>();
            map = ETData.get(position);

            //holder.source.setText(source);
            holder.time.setText(map.get("KEY_ETATIME"));
            // holder.destination.setText(map.get("KEY_SHELTERNAME"));
            holder.destination.setText(map.get("KEY_SHELTER_ORIGIN_NAME") + " - " + map.get("KEY_SHELTER_DESTINATON_NAME") );

            holder.routeno_data.setText(map.get("KEY_ROUTENAME"));

            holder.name.setText(map.get("KEY_VEHICLENO"));

            final String tripId = map.get("KEY_SHELTERID");
            final String vehicleid = map.get("KEY_SHELTERVEHICLEID");

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inty = new Intent(Nrda_Eta_Second.this,Nrda_Eta_third.class);
                    inty.putExtra("tripid",tripId);
                    inty.putExtra("vehicleid",vehicleid);
                    inty.putExtra("Source", getSource);
                    inty.putExtra("destination", getDestination);
//                    finish();
                    startActivity(inty);
                    overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                }
            });
            /*if(position==101){
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inty = new Intent(Nrda_Eta_Second.this,Nrda_Eta_third.class);
                        inty.putExtra("tripid",tripId);
                        inty.putExtra("vehicleid",vehicleid);
                        inty.putExtra("Source", getSource);
                        inty.putExtra("destination", getDestination);
                        startActivity(inty);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                });


            }else {
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent inty = new Intent(Nrda_Eta_Second.this,Nrda_Eta_third.class);
                        inty.putExtra("tripid",tripId);
                        inty.putExtra("vehicleid",vehicleid);
                        inty.putExtra("Source", getSource);
                        inty.putExtra("destination", getDestination);
                        finish();
                        startActivity(inty);
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                    }
                });
            }*/
        }

        @Override
        public int getItemCount() {
            return ETData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            //protected MyTextView source;
            protected TextView time;
            protected TextView name;
            protected TextView destination;
            protected TextView routeno_data;
            protected LinearLayout linearLayout;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                //source = (MyTextView) itemLayputView.findViewById(R.id.source);
                time = (TextView) itemLayputView.findViewById(R.id.time);
                linearLayout = (LinearLayout)itemLayputView.findViewById(R.id.line_main);
                name = (TextView) itemLayputView.findViewById(R.id.name);
                routeno_data = (TextView) itemLayputView.findViewById(R.id.routeno_data);
                destination = (TextView) itemLayputView.findViewById(R.id.destination);

            }
        }
    }
}
