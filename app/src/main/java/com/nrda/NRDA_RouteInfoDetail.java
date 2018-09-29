package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Random;

public class NRDA_RouteInfoDetail extends AppCompatActivity {
    private ImageView tool_back_icon;
    private TextView tool_title, tvto, tvfrom, txtTvkm, txtTvrupee, txtTvtime, txtTvstation, tvinterchange, tvnextbus;
    String source, destination, source_id, destination_id;
    private RecyclerView shelternameRecyclerView;
    ArrayList<HashMap<String, String>> routeInfoData;
    private CardView noPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrda__route_info_detail);

        tool_title              = (TextView) findViewById(R.id.appTitle);
        tool_back_icon          = (ImageView) findViewById(R.id.tool_back_icon);
        shelternameRecyclerView = (RecyclerView) findViewById(R.id.shelternameRecyclerView);
        tvfrom                  = (TextView) findViewById(R.id.tvfrom);
        tvto                    = (TextView) findViewById(R.id.tvto);
        txtTvkm                 = (TextView) findViewById(R.id.txtTvkm);
        txtTvrupee              = (TextView) findViewById(R.id.txtTvrupee);
        txtTvtime               = (TextView) findViewById(R.id.txtTvtime);
        txtTvstation            = (TextView) findViewById(R.id.txtTvstation);
        tvinterchange           = (TextView) findViewById(R.id.tvinterchange);
        noPosts                 = (CardView) findViewById(R.id.noPosts);
        tvnextbus               = (TextView) findViewById(R.id.tvnextbus);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.route_information));

        Intent in       = getIntent();
        source          = in.getStringExtra("KEY_SOURCE");
        destination     = in.getStringExtra("KEY_DESTINATION");
        source_id       = in.getStringExtra("KEY_SOURCR_ID");
        destination_id  = in.getStringExtra("KEY_DESTINATION_ID");

        tvfrom.setText(destination);
        tvto.setText(source);

        shelternameRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        shelternameRecyclerView.setLayoutManager(layoutManager);

            if (isConnection()){
                new GetRouteInformation().execute();
            }

    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            HashMap<String, String> map = new HashMap<String, String>();
            map = routeInfoData.get(position);

            final String dest = map.get("KEY_NAME");

           final String inHTML = map.get("KEY_NAME")+" / "+map.get("KEY_NAME_hi");
            holder.title.setText(Html.fromHtml(inHTML));

            if (dest.equals(destination)){
                holder.img_flg.setImageResource(R.drawable.ic_end);
            }
        }

        @Override
        public int getItemCount() {
            return routeInfoData.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ImageView img_flg;
            public MyViewHolder(View itemView) {
                super(itemView);

                title       = (TextView) itemView.findViewById(R.id.routename);
                img_flg     = (ImageView) itemView.findViewById(R.id.img_flg);

            }
        }
    }

    class GetRouteInformation extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String getResponse = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NRDA_RouteInfoDetail.this, "Loading...", "Please Wait...", true);
        }

        protected String doInBackground(String... args) {


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("source", source_id));
            nameValuePairs.add(new BasicNameValuePair("destination", destination_id));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL+ Nrda_Url_Conts.GETROUTEINFORMATION;
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
                    routeInfoData = new ArrayList<>();

                    String tot_distance = jsonObj.getString("tot_distance");
                    String tot_fare     = jsonObj.getString("tot_fare");
                    String tot_time     = jsonObj.getString("tot_time");
                    String tot_station  = jsonObj.getString("tot_station");
                    String interchange  = jsonObj.getString("interchange");

                    txtTvkm.setText(tot_distance);
                    txtTvrupee.setText(tot_fare);
                    txtTvtime.setText(tot_time);
                    txtTvstation.setText(tot_station);
                    tvinterchange.setText(interchange);

                    JSONArray jarray = jsonObj.getJSONArray("data");

                    for (int i = 0; i < jarray.length(); i++) {
                        HashMap<String, String> map = new HashMap<>();
                        JSONObject object = jarray.getJSONObject(i);

                        map.put("KEY_INTERCHANGE",object.optString("interchange1"));
                        map.put("KEY_NAME",object.optString("sheltername"));
                        map.put("KEY_NAME_hi",object.optString("sheltername_hi"));

                        routeInfoData.add(map);
                    }
                    MyAdapter myAdapter = new MyAdapter();

                    Random r = new Random();
                    int Low = 27;
                    int High = 30;
                    int Result = r.nextInt(High-Low) + Low;
                    tvnextbus.setText(""+Result+" Min");

                    shelternameRecyclerView.setAdapter(myAdapter);
                    shelternameRecyclerView.setVisibility(View.VISIBLE);
                    noPosts.setVisibility(View.GONE);
                }else {
                    //Toast.makeText(NRDA_RouteInfoDetail.this, getResources().getString(R.string.no_records_available), Toast.LENGTH_SHORT).show();
                    noPosts.setVisibility(View.VISIBLE);
                    shelternameRecyclerView.setVisibility(View.GONE);
                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
        }
    }

    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) NRDA_RouteInfoDetail.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(NRDA_RouteInfoDetail.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
