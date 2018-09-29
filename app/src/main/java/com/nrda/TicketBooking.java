package com.nrda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TicketBooking extends AppCompatActivity {
    int minteger = 1;
    int fares = 0;
    private TextView txt_sorce,txt_destination, txt_date, txt_fare, displayInteger, tool_title,txt_fares;
    private ImageView tool_back_icon;
    private EditText et_ph, et_name, et_email;
    String order_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_booking);

        tool_title=(TextView)findViewById(R.id.appTitle);
        tool_back_icon = (ImageView) findViewById(R.id.tool_back_icon);

        tool_back_icon.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        Intent inty = new Intent(TicketBooking.this, Nrda_Fare_cal.class);
        startActivity(inty);
        finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }
        });
        tool_back_icon.setVisibility(View.VISIBLE);
        tool_title.setText(getResources().getString(R.string.mobile_registration));

        Intent inty = getIntent();
        String source = inty.getStringExtra("source");
        String destination = inty.getStringExtra("destination");
        String fare = inty.getStringExtra("fare");

                txt_sorce       = (TextView) findViewById(R.id.txt_sorce);
                txt_destination = (TextView) findViewById(R.id.txt_destination);
                txt_date        = (TextView) findViewById(R.id.txt_date);
                txt_fare        = (TextView) findViewById(R.id.txt_fare);
                txt_fares       = (TextView) findViewById(R.id.txt_fares);
                //payNow = (TextView) findViewById(R.id.payNow);
                displayInteger  = (TextView) findViewById(R.id.integer_number);
                et_email        = (EditText) findViewById(R.id.et_email);
                et_name         = (EditText) findViewById(R.id.et_name);
                et_ph           = (EditText) findViewById(R.id.et_ph);

        txt_sorce.setText(source);
        txt_destination.setText(destination);
        txt_fare.setText("₹\t" + fare);

            fares = Integer.parseInt(fare);
            txt_fares.setText("₹\t" + String.valueOf(1*fares));

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dates = df.format(Calendar.getInstance().getTime());

        txt_date.setText(dates);
        }

    public void increaseInteger(View view) {
            minteger = minteger + 1;
            displayInteger.setText("" + minteger);
            txt_fares.setText("₹\t" + String.valueOf(minteger*fares));
            }

    public void decreaseInteger(View view) {
            if(minteger <= 1){
            minteger = 1;
            Toast.makeText(this, "Number of passenger can't be less than 1", Toast.LENGTH_SHORT).show();
            displayInteger.setText("" + minteger);
                    txt_fares.setText("₹\t" + String.valueOf(minteger*fares));
                    }
            if(minteger > 1){
            minteger = minteger - 1;
            displayInteger.setText("" + minteger);
                    txt_fares.setText("₹\t" + String.valueOf(minteger*fares));
            }
    }

@Override
public void onBackPressed(){
        super.onBackPressed();
        // do something here and don't write super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        }


    public void payNow(View view) {

    }

}
