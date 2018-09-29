package com.nrda.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.nrda.smart_card.SmartCard_Otp;
import com.nrda.smart_card.Smart_mobile_veri;

public class ReadSms extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {

            if (bundle != null){

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++){

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    boolean b = phoneNumber.endsWith("TATPAR");  //Just to fetch otp sent from WNRCRP
                    String senderNum = phoneNumber ;
                    String message = currentMessage .getDisplayMessageBody();

                    String messageBody = currentMessage.getMessageBody();
                    String otpValue = messageBody.replaceAll("[^0-9]","");   // here abcd contains otp
                    //which is in number format
                    //Pass on the text to our listener.

                    try{

                        if (phoneNumber.endsWith("TATPAR")){

                            /*Otp Sms = new Otp();
                            Sms.recivedSms(message );*/

                            //Toast.makeText(context,"ReadSMS: "+otpValue,Toast.LENGTH_LONG).show();
                            // Notify UI that registration has completed, so the progress indicator can be hidden.
                            Intent otpReceivedIntent = new Intent(SmartCard_Otp.SMSOTP_READER);
                            otpReceivedIntent.putExtra("otpValue",otpValue);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(otpReceivedIntent);
                        }
                    }catch(Exception e){}
                }
            }

        } catch (Exception e) {}
    }

}
