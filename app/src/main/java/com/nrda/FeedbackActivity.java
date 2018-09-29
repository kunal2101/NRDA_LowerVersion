package com.nrda;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends AppCompatActivity {
    private TextView tool_title;
    private EditText feedbackEdittext, txt_mob;
    private Button btndone;
    private ImageView bus_five, bus_four, bus_three, bus_two, bus_one, tool_back_icon;
    private ImageView sheal_five, sheal_four, sheal_three, sheal_two, sheal_one;
    private ImageView driver_five, driver_four, driver_three, driver_two, driver_one;
    String busRrtting, shelterRatting, serviceRatting, getMobile, getFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_feedback);

        tool_back_icon      = (ImageView) findViewById(R.id.tool_back_icon);
        tool_title          = (TextView) findViewById(R.id.appTitle);
        txt_mob             = (EditText) findViewById(R.id.txt_mob);
        feedbackEdittext    = (EditText) findViewById(R.id.feedbackEdittext);

        bus_five            = (ImageView) findViewById(R.id.bus_five);
        bus_four            = (ImageView) findViewById(R.id.bus_four);
        bus_three           = (ImageView) findViewById(R.id.bus_three);
        bus_two             = (ImageView) findViewById(R.id.bus_two);
        bus_one             = (ImageView) findViewById(R.id.bus_one);

        sheal_five          = (ImageView) findViewById(R.id.sheal_five);
        sheal_four          = (ImageView) findViewById(R.id.sheal_four);
        sheal_three         = (ImageView) findViewById(R.id.sheal_three);
        sheal_two           = (ImageView) findViewById(R.id.sheal_two);
        sheal_one           = (ImageView) findViewById(R.id.sheal_one);

        driver_five         = (ImageView) findViewById(R.id.driver_five);
        driver_four         = (ImageView) findViewById(R.id.driver_four);
        driver_three        = (ImageView) findViewById(R.id.driver_three);
        driver_two          = (ImageView) findViewById(R.id.driver_two);
        driver_one          = (ImageView) findViewById(R.id.driver_one);

        btndone             = (Button) findViewById(R.id.btndone);

        tool_title.setText(getResources().getString(R.string.feedback_new));

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent inty = new Intent(FeedbackActivity.this, MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

        tool_back_icon.setVisibility(View.VISIBLE);

        busRrtting = null;
        shelterRatting = null;
        serviceRatting = null;

        bus_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));
                bus_five.setImageResource(R.drawable.nervous_ch);
                bus_four.setImageResource(R.drawable.sad);
                bus_three.setImageResource(R.drawable.happysmile);
                bus_two.setImageResource(R.drawable.happytri);
                bus_one.setImageResource(R.drawable.happy);
                busRrtting = "1";

            }
        });

        bus_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));
                bus_five.setImageResource(R.drawable.nervous);
                bus_four.setImageResource(R.drawable.sad_ch);
                bus_three.setImageResource(R.drawable.happysmile);
                bus_two.setImageResource(R.drawable.happytri);
                bus_one.setImageResource(R.drawable.happy);
                busRrtting = "2";
            }
        });

        bus_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));
                bus_five.setImageResource(R.drawable.nervous);
                bus_four.setImageResource(R.drawable.sad);
                bus_three.setImageResource(R.drawable.happysmile_ch);
                bus_two.setImageResource(R.drawable.happytri);
                bus_one.setImageResource(R.drawable.happy);
                busRrtting = "3";
            }
        });

        bus_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));
                bus_five.setImageResource(R.drawable.nervous);
                bus_four.setImageResource(R.drawable.sad);
                bus_three.setImageResource(R.drawable.happysmile);
                bus_two.setImageResource(R.drawable.happytri_ch);
                bus_one.setImageResource(R.drawable.happy);
                busRrtting = "4";
            }
        });

        bus_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));
                bus_five.setImageResource(R.drawable.nervous);
                bus_four.setImageResource(R.drawable.sad);
                bus_three.setImageResource(R.drawable.happysmile);
                bus_two.setImageResource(R.drawable.happytri);
                bus_one.setImageResource(R.drawable.happy_ch);
                busRrtting = "5";
            }
        });

        sheal_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                sheal_five.setImageResource(R.drawable.nervous_ch);
                sheal_four.setImageResource(R.drawable.sad);
                sheal_three.setImageResource(R.drawable.happysmile);
                sheal_two.setImageResource(R.drawable.happytri);
                sheal_one.setImageResource(R.drawable.happy);
                shelterRatting = "1";
            }
        });

        sheal_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                sheal_five.setImageResource(R.drawable.nervous);
                sheal_four.setImageResource(R.drawable.sad_ch);
                sheal_three.setImageResource(R.drawable.happysmile);
                sheal_two.setImageResource(R.drawable.happytri);
                sheal_one.setImageResource(R.drawable.happy);
                shelterRatting = "2";
            }
        });

        sheal_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch))

                sheal_five.setImageResource(R.drawable.nervous);
                sheal_four.setImageResource(R.drawable.sad);
                sheal_three.setImageResource(R.drawable.happysmile_ch);
                sheal_two.setImageResource(R.drawable.happytri);
                sheal_one.setImageResource(R.drawable.happy);
                shelterRatting = "3";
            }
        });

        sheal_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                sheal_five.setImageResource(R.drawable.nervous);
                sheal_four.setImageResource(R.drawable.sad);
                sheal_three.setImageResource(R.drawable.happysmile);
                sheal_two.setImageResource(R.drawable.happytri_ch);
                sheal_one.setImageResource(R.drawable.happy);
                shelterRatting = "4";
            }
        });

        sheal_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                sheal_five.setImageResource(R.drawable.nervous);
                sheal_four.setImageResource(R.drawable.sad);
                sheal_three.setImageResource(R.drawable.happysmile);
                sheal_two.setImageResource(R.drawable.happytri);
                sheal_one.setImageResource(R.drawable.happy_ch);
                shelterRatting = "5";
            }
        });

        driver_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                driver_five.setImageResource(R.drawable.nervous_ch);
                driver_four.setImageResource(R.drawable.sad);
                driver_three.setImageResource(R.drawable.happysmile);
                driver_two.setImageResource(R.drawable.happytri);
                driver_one.setImageResource(R.drawable.happy);
                serviceRatting = "1";
            }
        });

        driver_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                driver_five.setImageResource(R.drawable.nervous);
                driver_four.setImageResource(R.drawable.sad_ch);
                driver_three.setImageResource(R.drawable.happysmile);
                driver_two.setImageResource(R.drawable.happytri);
                driver_one.setImageResource(R.drawable.happy);
                serviceRatting = "2";
            }
        });

        driver_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                driver_five.setImageResource(R.drawable.nervous);
                driver_four.setImageResource(R.drawable.sad);
                driver_three.setImageResource(R.drawable.happysmile_ch);
                driver_two.setImageResource(R.drawable.happytri);
                driver_one.setImageResource(R.drawable.happy);
                serviceRatting = "3";
            }
        });

        driver_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));

                driver_five.setImageResource(R.drawable.nervous);
                driver_four.setImageResource(R.drawable.sad);
                driver_three.setImageResource(R.drawable.happysmile);
                driver_two.setImageResource(R.drawable.happytri_ch);
                driver_one.setImageResource(R.drawable.happy);
                serviceRatting = "4";
            }
        });

        driver_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // bus_five.setBackground(getResources().getDrawable(R.drawable.happy_ch));
                driver_five.setImageResource(R.drawable.nervous);
                driver_four.setImageResource(R.drawable.sad);
                driver_three.setImageResource(R.drawable.happysmile);
                driver_two.setImageResource(R.drawable.happytri);
                driver_one.setImageResource(R.drawable.happy_ch);
                serviceRatting = "5";
            }
        });

        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    getMobile = txt_mob.getText().toString();
                    getFeedback = feedbackEdittext.getText().toString();
                    if (TextUtils.isEmpty(getMobile)){
                        Toast.makeText(FeedbackActivity.this, "Enter mobile number.", Toast.LENGTH_SHORT).show();
                    }else if (!mobNumberValidation(txt_mob.getText().toString())) {
                        Toast.makeText(FeedbackActivity.this, "Enter valid mobile number", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(getFeedback)){
                        Toast.makeText(FeedbackActivity.this, "Enter your feedback message", Toast.LENGTH_SHORT).show();
                    } else if (isConnection()){
                        new GetRouteInformation().execute();
                }
            }
        });
    }
    public static boolean mobNumberValidation(String phonestring){
        if (null == phonestring || phonestring.length() == 0) {
            return false;
        }
        Pattern phonePattern = Pattern.compile("[0-9]{10}");
        Matcher phoneMatcher = phonePattern.matcher(phonestring);
        return phoneMatcher.matches();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    class GetRouteInformation extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        String getResponse = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FeedbackActivity.this, "Loading...", "Please Wait...", true);

        }

        protected String doInBackground(String... args) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

            nameValuePairs.add(new BasicNameValuePair("mobile", getMobile));
            nameValuePairs.add(new BasicNameValuePair("feedback", getFeedback));
            nameValuePairs.add(new BasicNameValuePair("busrating", busRrtting));
            nameValuePairs.add(new BasicNameValuePair("shelterrating", shelterRatting));
            nameValuePairs.add(new BasicNameValuePair("driverrating", serviceRatting));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                String url = Nrda_Url_Conts.BASE_URL+ Nrda_Url_Conts.GETFEEDBACK;
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
                    txt_mob.setText("");
                    feedbackEdittext.setText("");
                    Toast.makeText(FeedbackActivity.this, jsonObj.getString("Message"), Toast.LENGTH_SHORT).show();
                }

            }catch (Exception ev){
                Log.e("response issue", ev.getMessage());
            }
        }
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) FeedbackActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(FeedbackActivity.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
