package com.nrda.services;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import com.nrda.database.DatabaseRoute;
import com.nrda.database.DatabaseRoutemodel;
import com.nrda.utils.Nrda_Url_Conts;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class RouteCoordinate extends IntentService {

    public DatabaseRoute databaseRoute;
    private static final String TAG = "RouteCordinateService";

    public RouteCoordinate() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Route Co-ordinate", "Service created...............................");
        databaseRoute  = new DatabaseRoute(this);
        int upRoutecount = databaseRoute.Get_UPRouteCount();

        if(upRoutecount == 0) {
            new GetAllRoutePoints().execute();
        }
    }

    class GetAllRoutePoints extends AsyncTask<String, String, String> {
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Please Wait...", true);
            Log.e(TAG, "Service Called---------------------");
        }

        protected String doInBackground(String... args) {

            String getResponse = null;

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL + Nrda_Url_Conts.GETROUTEPOINT;
                HttpPost httppost = new HttpPost(url);

                HttpResponse response = httpclient.execute(httppost);
                getResponse = EntityUtils.toString(response.getEntity());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return getResponse;
        }

        protected void onPostExecute(final String getResponse) {

            try {
                JSONObject jsonObj = new JSONObject(getResponse);
                boolean success = jsonObj.getBoolean("Success");

                if (success) {
                    JSONArray jarray = jsonObj.getJSONArray("routepoints");
                    for (int i = 0; i < jarray.length(); i++) {

                        JSONObject object = jarray.getJSONObject(i);
                        String routeid = object.getString("routeid");
                        String routeName = object.getString("routeName");

                        JSONArray innerArray = object.getJSONArray("Details");
                        for (int j = 0; j < innerArray.length(); j++) {
                            JSONObject pontobj = innerArray.getJSONObject(j);

                            databaseRoute.insertUPRoute(new DatabaseRoutemodel(routeid, pontobj.getString("latitude"), pontobj.getString("longitude")));
                        }
                    }
                }

            } catch (Exception ev) {
                Log.e("response issue", ev.getMessage());
            }
            Log.e(TAG, "Service Finish---------------------");
            Log.e("response issue", getResponse);
            // progressDialog.dismiss();
        }
    }
}
