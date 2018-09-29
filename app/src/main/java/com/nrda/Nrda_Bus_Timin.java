package com.nrda;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.nrda.utils.Nrda_Url_Conts;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Diwash Kumar Choudhary on 3/9/2017.
 */
public class Nrda_Bus_Timin extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView tool_title, getTiming;
    private ImageView tool_back_icon;
   // ArrayList<HashMap<String, String>> ETData ;
    SearchableSpinner spinner_source;
    int selectedRadio = 0 ; // 0-> noting, 1--> onwards, 2--> full days
     ArrayList<HashMap<String,String>> sourcedataList;
     ArrayList<String> sourcelist, upList, downList;
    String sourceId = null;
    LinearLayout ly_tit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nrda_eta_recycle);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setVisibility(View.INVISIBLE);
        getTiming = (TextView) findViewById(R.id.getTiming);
        //spinner_destination = (SearchableSpinner) findViewById(R.id.spinner_destination);
        spinner_source = (SearchableSpinner) findViewById(R.id.spinner_source);
        ly_tit  = (LinearLayout) findViewById(R.id.ly_tit);

        tool_title = (TextView) findViewById(R.id.appTitle);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText(getResources().getString(R.string.schedule_bus_timing));

        if (isConnection()) {
            new GetAllShelterDetail().execute();
        }
        //spinner_destination.setTitle("Select Destination Station");
        spinner_source.setTitle(getResources().getString(R.string.select_your_stop));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        getTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sourceId == null || sourceId.isEmpty()){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.select_your_stop),Toast.LENGTH_LONG).show();
                }
               /* else if (destinationId == null || destinationId.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Select Destination",Toast.LENGTH_LONG).show();
                }else if(sourceId.equalsIgnoreCase(destinationId)){
                    Toast.makeText(getApplicationContext(),"Source Stop and Destination Stop should not be same",Toast.LENGTH_LONG).show();
                }*/
                else if(isConnection()) {
                    //new getBusTimingList().execute();
                    showtimingdialog();
                }
            }
        });


       /* spinner_destination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);
                String getValue = getsource.getText().toString();
                for(int pqr=0; pqr < sourcedataList.size(); pqr++){
                    HashMap<String,String> map = sourcedataList.get(pqr);
                    if(map.get("KEY_NAME").equalsIgnoreCase(getValue)){
                        destinationId = map.get("KEY_ID");
                    }
                }
                //  Toast.makeText(Nrda_Route_Eta.this,"source :"+sourceId+"\nDestination :"+destinationId,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        spinner_source.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView getsource = (TextView)view.findViewById(R.id.sr_source);
                String getValue = getsource.getText().toString();

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
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_timing_list_item, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
           /* if((position % 2 == 0)){
                holder.lin_main.setBackgroundColor(Color.parseColor("#87CEFA"));
            }else{
                holder.lin_main.setBackgroundColor(Color.parseColor("#EEE8AA"));
            }*/
           /* List<String> l = new ArrayList<String>();
            l.add("8:00 am");
            l.add("8:32 am");
            l.add("8:10 am");
            l.add("1:00 pm");
            l.add("3:00 pm");
            l.add("2:00 pm");
            l.add("2:00 am");

            Collections.sort(l, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    try {
                        return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
                    } catch (ParseException e) {
                        return 0;
                    } catch (java.text.ParseException e) {
                        return 0;
                    }
                }
            });
            System.out.println(l);*/

            Collections.sort(upList, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    try {
                        return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
                    } catch (ParseException e) {
                        return 0;
                    } catch (java.text.ParseException e) {
                        return 0;
                    }
                }
            });

            Collections.sort(downList, new Comparator<String>() {

                @Override
                public int compare(String o1, String o2) {
                    try {
                        return new SimpleDateFormat("hh:mm a").parse(o1).compareTo(new SimpleDateFormat("hh:mm a").parse(o2));
                    } catch (ParseException e) {
                        return 0;
                    } catch (java.text.ParseException e) {
                        return 0;
                    }
                }
            });

            if(position < upList.size()){
                holder.txt_uptime.setText(upList.get(position));
            }else{
                holder.txt_uptime.setText("-");
            }

            if(position < downList.size()){
                holder.txt_downtime.setText(downList.get(position));
            }else{
                holder.txt_downtime.setText("-");
            }

            /*HashMap<String, String> map = new HashMap<String, String>();
            map = ETData.get(position);

            holder.route_type.setText(map.get("KEY_ROUTE_TYPE"));
            holder.route_timing.setText(map.get("KEY_SCHEDULETIME"));
            holder.txt_destination.setText(map.get("KEY_DISTINATION"));
            holder.txt_source.setText(map.get("KEY_ORIGIN"));
            holder.txt_vehicle.setText("Route No. : " +map.get("KEY_ROUTE_NO"));*/
        }

        @Override
        public int getItemCount() {
            return upList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView route_timing, txt_source, txt_destination, txt_vehicle, txt_uptime, txt_downtime;
            protected TextView route_type;
            protected CardView lin_main;

            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                /*route_timing = (TextView) itemLayputView.findViewById(R.id.route_time);
                txt_vehicle = (TextView) itemLayputView.findViewById(R.id.txt_vehicle);
                route_type = (TextView) itemLayputView.findViewById(R.id.txt_type);
                txt_source = (TextView) itemLayputView.findViewById(R.id.txt_source);
                txt_destination = (TextView) itemLayputView.findViewById(R.id.txt_destination);
                lin_main = (CardView) itemLayputView.findViewById(R.id.lin_main);*/

                txt_downtime = (TextView) itemLayputView.findViewById(R.id.txt_downtime);
                txt_uptime = (TextView) itemLayputView.findViewById(R.id.txt_uptime);
            }
        }
    }

    private void showtimingdialog() {

        final Dialog dialog = new Dialog(Nrda_Bus_Timin.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.nrda_bus_timingdialog);
        ImageView imgCancle = (ImageView) dialog.findViewById(R.id.cancleImg);
        final RadioGroup auto_RadioGroup    = (RadioGroup) dialog.findViewById(R.id.radiogrp);
        RadioButton radioButton_auto        = (RadioButton) dialog.findViewById(R.id.radioone);
        RadioButton radioButton_other       = (RadioButton) dialog.findViewById(R.id.radiotwo);

        imgCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        auto_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String auto_Value = ((RadioButton) dialog.findViewById(checkedId)).getTag().toString();
                if (auto_Value.equalsIgnoreCase("0")) {
                    selectedRadio = 2;
                    dialog.dismiss();
                    new getBusTimingList().execute();
                }
                if (auto_Value.equalsIgnoreCase("1")) {
                    selectedRadio = 1;
                    new getBusTimingList().execute();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    class GetAllShelterDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Bus_Timin.this, "Loading...", "Please Wait...", true);

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
                    ArrayAdapter adapters = new ArrayAdapter<String>(Nrda_Bus_Timin.this,  R.layout.spinner_item, sourcelist);
                    spinner_source.setAdapter(adapters);
                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
        }
    }


    class getBusTimingList extends AsyncTask<String, String, String> {
        String getResponse = null;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Nrda_Bus_Timin.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("sourceid", sourceId));
           // nameValuePairs.add(new BasicNameValuePair("destinationid", destinationId));

            try {
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETSHELTERETADETAILS;

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
                            JSONArray jarray = jsonObject.getJSONArray("ETAShelterWiseDetails");

                           // ETData = new ArrayList<>();
                            upList = new ArrayList<>();
                            downList = new ArrayList<>();
                            upList.clear();
                            downList.clear();

                            ArrayList<String> vehno = new ArrayList<>();

                            for (int i = 0; i < jarray.length(); i++) {
                              //  HashMap<String, String> map = new HashMap<>();

                                JSONObject object = jarray.getJSONObject(i);
                               /* map.put("KEY_ROUTENAME", object.optString("shelterName"));
                                map.put("KEY_SHELTERID", object.optString("shelterid"));
                                map.put("KEY_ROUTE_TYPE", object.optString("routetype"));
                                map.put("KEY_VEHICLE_NO", object.optString("vehicleno"));
                                map.put("KEY_ROUTE_ID", object.optString("routeid"));
                                map.put("KEY_SCHEDULETIME", object.optString("scheduletime"));
                                map.put("KEY_ESTIMATED_TIME", object.optString("etatime"));
                                map.put("KEY_ROUTE_NO", object.optString("routeno"));

                                map.put("KEY_ORIGIN", object.optString("triporiginname"));
                                map.put("KEY_DISTINATION", object.optString("tripdestinationname"));
                                map.put("KEY_ESTIMATED_TIME", object.optString("etatime"));*/

                               String vechWithRoute = object.optString("vehicleno")+""+object.optString("routetype");
                                String routeType = object.optString("routetype");


                                /*try {
                                   // dd/MM/yyyy hh:mm:ss a
                                    SimpleDateFormat _24HourSDF = new SimpleDateFormat("hh:mm a");
                                    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                                    Date _24HourDt = _24HourSDF.parse(object.optString("etatime"));
                                    System.out.println(_24HourDt);
                                    System.out.println(_12HourSDF.format(_24HourDt));

                                    map.put("KEY_ESTIMATED_TIME", _12HourSDF.format(_24HourDt));
                                } catch (final ParseException e) {
                                    e.printStackTrace();
                                }*/

                                if(selectedRadio == 1){

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                                    Date date1 = simpleDateFormat.parse(object.optString("etatime"));

                                    Calendar cal = Calendar.getInstance();
                                    Date currentLocalTime = cal.getTime();
                                    DateFormat date = new SimpleDateFormat("hh:mm a");

                                    String localTime = date.format(currentLocalTime);

                                    Date date2 = simpleDateFormat.parse(localTime);

                                    long difference = date1.getTime() - date2.getTime();

                                    if(difference > 0){
                                        if (routeType.equalsIgnoreCase("UP") && !vehno.contains(vechWithRoute)){
                                            upList.add(object.optString("etatime"));
                                            vehno.add(vechWithRoute);
                                        }else if (routeType.equalsIgnoreCase("DOWN") && !vehno.contains(vechWithRoute)){
                                            downList.add(object.optString("etatime"));
                                            vehno.add(vechWithRoute);
                                        }
                                       // ETData.add(map);
                                    }

                                }else{
                                    if (routeType.equalsIgnoreCase("UP") && !vehno.contains(vechWithRoute)){
                                        upList.add(object.optString("etatime"));
                                        vehno.add(vechWithRoute);
                                    }else if (routeType.equalsIgnoreCase("DOWN") && !vehno.contains(vechWithRoute)){
                                        downList.add(object.optString("etatime"));
                                        vehno.add(vechWithRoute);
                                    }
                                    //ETData.add(map);
                                }
                                //ETData.add(map);
                            }
                            Myadapter mAdapter = new Myadapter();
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setVisibility(View.VISIBLE);
                            ly_tit.setVisibility(View.VISIBLE);

                        }else {
                            String msg = jsonObject.getString("Message");
                            Toast.makeText(Nrda_Bus_Timin.this, getResources().getString(R.string.no_records_available), Toast.LENGTH_LONG).show();
                            recyclerView.setVisibility(View.GONE);
                            ly_tit.setVisibility(View.GONE);
                        }
                    } catch (Exception ev) {
                        ev.getStackTrace();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }


    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) Nrda_Bus_Timin.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(Nrda_Bus_Timin.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
