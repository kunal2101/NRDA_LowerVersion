package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nrda.utils.Nrda_Url_Conts;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class NRDA_Route_info extends AppCompatActivity {
    private RecyclerView routelist;
    private ImageView tool_back_icon;
    private TextView tool_title, get_route;
    static ArrayList<HashMap<String,String>> sourcedataList;
    private EditText spinner_source, spinner_destination;
    String source_id, destination_id;
    //static ArrayList<String> sourcelist;
    //ArrayList<HashMap<String, String>> ETData;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrda__new_route_info);

        tool_title          =(TextView)findViewById(R.id.appTitle);
        routelist           = (RecyclerView) findViewById(R.id.routelist);
        tool_back_icon      =(ImageView)findViewById(R.id.tool_back_icon);
        spinner_source      = (EditText)findViewById(R.id.spinner_source);
        spinner_destination = (EditText) findViewById(R.id.spinner_destination);
        get_route           = (TextView) findViewById(R.id.get_route);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.route_information));

        routelist.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        routelist.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplication(), R.dimen.item_offset);
        routelist.addItemDecoration(itemDecoration);

        if (isConnection()) {
            new GetAllShelterDetail().execute();
        }

        get_route.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(spinner_source.getText().toString())){
                Toast.makeText(NRDA_Route_info.this, getResources().getString(R.string.select_start_station), Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(spinner_destination.getText().toString())){
                Toast.makeText(NRDA_Route_info.this, getResources().getString(R.string.select_end_station), Toast.LENGTH_SHORT).show();
            } else if(spinner_destination.getText().toString().equals(spinner_source.getText().toString())){
                Toast.makeText(NRDA_Route_info.this, getResources().getString(R.string.source_destination_same), Toast.LENGTH_SHORT).show();

            }else {
                String source = spinner_source.getText().toString();
                String destination = spinner_destination.getText().toString();

                Intent in = new Intent(NRDA_Route_info.this, NRDA_RouteInfoDetail.class);
                in.putExtra("KEY_SOURCE", source);
                in.putExtra("KEY_DESTINATION", destination);
                in.putExtra("KEY_SOURCR_ID", source_id);
                in.putExtra("KEY_DESTINATION_ID", destination_id);
                startActivity(in);
                overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                finish();
            }
        }
    });

    }

    class GetAllShelterDetail extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NRDA_Route_info.this, "Loading...", "Please Wait...", true);
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
              //  sourcelist = new ArrayList<>();

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
                        //sourcelist.add(object.optString("ShelterName"));
                    }

                    Myadapter mAdapter = new Myadapter(sourcedataList);
                    routelist.setAdapter(mAdapter);
                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
            progressDialog.dismiss();
        }
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) NRDA_Route_info.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(NRDA_Route_info.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {
        HashMap<String, String> map = new HashMap<String, String>();
        ArrayList<HashMap<String,String>> dataList;

        public Myadapter(ArrayList<HashMap<String,String>> map) {
            this.dataList = map;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shelter_items, null);
            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // HashMap<String, String> map = new HashMap<String, String>();
            map = dataList.get(position);

            holder.txt_shltrname.setText( map.get("KEY_NAME")+" / "+ map.get("KEY_NAME_HI"));
            /*holder.from_to.setText(map.get("KEY_ROUTENAME"));
            routeId = map.get("KEY_ROUTEID");*/
            final String source = map.get("KEY_NAME");
            final String destination = map.get("KEY_NAME");
            final String s_shelterid = map.get("KEY_ID");
            final String d_shelterid = map.get("KEY_ID");

            holder.lin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (count % 2 ==0){
                        ArrayList<HashMap<String,String>> tempList = new ArrayList<>();

                        if (position <= 1){
                            for (int ind =0; ind< sourcedataList.size(); ind++){
                                String getSource = sourcedataList.get(ind).get("KEY_NAME");
                                if(!getSource.toLowerCase().contains("UP".toLowerCase())){
                                    tempList.add(sourcedataList.get(ind));
                                    //sourcedataList.remove(ind);
                                }
                            }

                            Myadapter mAdapter = new Myadapter(tempList);
                            routelist.setAdapter(mAdapter);
                        }else if(position >= 8){
                            for (int ind =0; ind< sourcedataList.size(); ind++){
                                String getSource = sourcedataList.get(ind).get("KEY_NAME");
                                if(!getSource.toLowerCase().contains("Down".toLowerCase())){
                                    tempList.add(sourcedataList.get(ind));
                                    //sourcedataList.remove(ind);
                                }
                            }

                            Myadapter mAdapter = new Myadapter(tempList);
                            routelist.setAdapter(mAdapter);
                        }else{
                            Myadapter mAdapter = new Myadapter(sourcedataList);
                            routelist.setAdapter(mAdapter);
                        }
                        //spinner_source.setFocusable(true);
                        spinner_source.setText(source);
                        source_id = s_shelterid;
                    }else {
                        //spinner_destination.setFocusable(true);
                        spinner_destination.setText(destination);
                        destination_id = d_shelterid;
                    }
                    count++;
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected TextView txt_shltrname;
            protected LinearLayout lin;


            public ViewHolder(final View itemLayputView) {
                super(itemLayputView);
                txt_shltrname = (TextView) itemLayputView.findViewById(R.id.txt_shltrname);
                lin = (LinearLayout) itemLayputView.findViewById(R.id.lin);
                }
        }
    }


}
