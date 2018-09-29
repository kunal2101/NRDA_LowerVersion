package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nrda.utils.Nrda_Url_Conts;
import com.nrda.utils.TouchImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class RouteMap extends AppCompatActivity {
    private TextView tool_title;
    private ImageView tool_back_icon;
    TouchImageView img_map;
    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        tool_title=(TextView)findViewById(R.id.appTitle);
        tool_back_icon=(ImageView)findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inty = new Intent(Nrda_Route_Info.this,MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(getResources().getString(R.string.route_map));

        img_map = (TouchImageView) findViewById(R.id.img_map);

        if (isConnection()) {
           new FetchImageTask().execute();
        }

    }

    private class FetchImageTask extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(RouteMap.this, "Loading...", "Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... urls) {
            String getResponse = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETROUTEMAPIMAGE;
                HttpGet httpget = new HttpGet(url);

                HttpResponse response = httpclient.execute(httpget);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject jsonObject = new JSONObject(result);
                boolean Success = Boolean.parseBoolean(jsonObject.getString("Success"));
                String Msg = jsonObject.getString("Message");
                if(Success){
                    JSONArray jsonArray = jsonObject.getJSONArray("getRouteMap");
                    JSONObject jsObj = jsonArray.getJSONObject(0);

                     imgPath = jsObj.getString("image");

                    try{

                        Picasso.with(RouteMap.this)
                                .load(imgPath)
                                //.placeholder(R.drawable.naya_raipur_image)
                                // .resize(200,0)
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .priority(Picasso.Priority.HIGH)
                                .into(img_map);

                    }catch (Exception ev){
                        System.out.print(ev.getMessage());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_LONG).show();

                }
            }catch (Exception ev){

            }
            progressDialog.dismiss();
        }
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) RouteMap.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(RouteMap.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
