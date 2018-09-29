package com.nrda.smart_card;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.nrda.R;
import com.nrda.utils.Nrda_Url_Conts;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SmartCard_Otp extends AppCompatActivity {
    //OTPView otpView;
    TextView timeText;
    private TextView tool_title;
    private ImageView tool_back_icon;
    String sucess,tag_id,mob_no,otp_st;
    AQuery aQuery;
    EditText otpView;
    String otpValue;
    Button txt_verify, resendotp;

    private BroadcastReceiver mOTPBroadcastReceiver;
    public static final String SMSOTP_READER = "tatpar_otp_reader";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_card__otp);

        otpView             = (EditText) findViewById(R.id.otpView);
        txt_verify          = (Button)findViewById(R.id.txt_verify);
        resendotp           = (Button)findViewById(R.id.resendotp);
        timeText            = (TextView) findViewById(R.id.timeText);
        tool_title          = (TextView) findViewById(R.id.appTitle);
        tool_back_icon      = (ImageView) findViewById(R.id.tool_back_icon);
        tool_title          = (TextView) findViewById(R.id.appTitle);

        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);
        tool_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent inty = new Intent(Nrda_ImageGallery.this,MainActivity.class);
                startActivity(inty);*/
                finish();
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });
        tool_back_icon.setVisibility(View.VISIBLE);

        tool_title.setText(R.string.otp_verify);

        aQuery = new AQuery(SmartCard_Otp.this);

        try {
            tag_id = getIntent().getStringExtra("tagid");
            mob_no = getIntent().getStringExtra("mob_no");
            //Toast.makeText(this, ""+tag_id+mob_no, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

            e.getMessage();
        }
        resendotp.setBackgroundResource(R.drawable.bg_disabled_button);
        resendotp.setEnabled(false);
        txt_verify.setEnabled(false);
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                timeText.setText("Time remaining: " + millisUntilFinished / 1000);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timeText.setText("Click on Resend button to re-generate the OTP !");
                resendotp.setBackgroundResource(R.drawable.bg_primary_button);

                resendotp.setEnabled(true);
            }

        }.start();

        final TextWatcher mTextEditorWatcher = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 6) {
                    txt_verify.setBackgroundResource(R.drawable.bg_disabled_button);
                    txt_verify.setEnabled(false);


                } else if (s.length() == 6) {
                    txt_verify.setBackgroundResource(R.drawable.bg_primary_button);
                    txt_verify.setEnabled(true);
                }
            }

            public void afterTextChanged(Editable s) {
            }
        };

        otpView.addTextChangedListener(mTextEditorWatcher);

        /*otpView.setTextColor(R.color.colorAccent)
                .setHintColor(R.color.colorAccent)
                .setCount(6)
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setViewsPadding(16)
                .setListener(new OTPListener() {
                    @Override
                    public void otpFinished(String otp) {
                        Toast.makeText(SmartCard_Otp.this, "OTP finished, the otp is " + otp, Toast.LENGTH_SHORT).show();
                        otp_st = otp;
                        txt_verify.setTextColor(getResources().getColor(R.color.resend));
                        resendotp.setTextColor(getResources().getColor(R.color.gray));
                    }
                })
                .fillLayout();*/


        txt_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isConnection()) {
                    otp_st = otpView.getText().toString().trim();
                    otp_info(otp_st);

                }
            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isConnection()){
                    mob_info();
                }
            }
        });

        mOTPBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equalsIgnoreCase(SMSOTP_READER)){
                    //Toast.makeText(SmartCard_Otp.this,"broadcasr Receiver",Toast.LENGTH_LONG).show();
                    otpValue = intent.getStringExtra("otpValue");
                    otpView.setText(otpValue);

                    if (isConnection()) {
                        otp_st = otpView.getText().toString().trim();
                        otp_info(otp_st);

                    }
                }

            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mOTPBroadcastReceiver, new IntentFilter(SMSOTP_READER));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mOTPBroadcastReceiver);
        super.onPause();
    }


    private void otp_info(String code) {
        String url = Nrda_Url_Conts.BASE_URL_two+Nrda_Url_Conts.GETREADOTP;
        Map map = new HashMap();
        map.put("TagId", tag_id);
        map.put("MobileNo", mob_no);
        map.put("otpcode",code);

        ProgressDialog progressDialog = new ProgressDialog(SmartCard_Otp.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);
        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    sucess = object.getString("Success");
                    if (sucess.equalsIgnoreCase("true")) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Otp Verify Successfully", Snackbar.LENGTH_LONG).show();
                        Intent inty = new Intent(SmartCard_Otp.this,SmartCardDetail.class);
                        inty.putExtra("tagid",tag_id);
                        startActivity(inty);
                        finish();

                    } else {
                        //Toast.makeText(getApplicationContext(),"No Fare Available for this Route ",Toast.LENGTH_LONG).show();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Invalid Input", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(Smart_mobile_veri.this, "Invalid Input", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void mob_info() {
        String url = Nrda_Url_Conts.BASE_URL_two+Nrda_Url_Conts.GETOTP;
        Map map = new HashMap();
        map.put("TagId", tag_id);
        map.put("MobileNo", mob_no);
        ProgressDialog progressDialog = new ProgressDialog(SmartCard_Otp.this);
        progressDialog.setMessage("Loding.....");
        progressDialog.setCancelable(false);
        aQuery.progress(progressDialog).ajax(url, map, JSONObject.class, new AjaxCallback<JSONObject>() {

            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {

                try {
                    sucess = object.getString("Success");
                    if (sucess.equalsIgnoreCase("true")) {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Otp Sent Successfully", Snackbar.LENGTH_LONG).show();

                        resendotp.setBackgroundResource(R.drawable.bg_disabled_button);
                        resendotp.setEnabled(false);
                        txt_verify.setEnabled(false);
                        new CountDownTimer(60000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                timeText.setText("Time remaining: " + millisUntilFinished / 1000);
                                //here you can have your logic to set text to edittext
                            }

                            public void onFinish() {
                                timeText.setText("Click on Resend button to re-generate the OTP !");
                                resendotp.setBackgroundResource(R.drawable.bg_primary_button);

                                resendotp.setEnabled(true);
                            }

                        }.start();



                    } else {
                        //Toast.makeText(getApplicationContext(),"No Fare Available for this Route ",Toast.LENGTH_LONG).show();
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Invalid Input", Snackbar.LENGTH_LONG).show();
                        // Toast.makeText(Smart_mobile_veri.this, "Invalid Input", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    protected boolean isConnection() {
        ConnectivityManager manage = (ConnectivityManager) SmartCard_Otp.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manage.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {

            return true;
        } else {
            Toast.makeText(SmartCard_Otp.this, getResources().getString(R.string.check_internet), Toast.LENGTH_LONG).show();
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
