package com.caribelabs.whereismycellphone.controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {
	
	// 
	//Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
    	
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        try {
             
            if (bundle != null) {
                 
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                 
                for (int i = 0; i < pdusObj.length; i++) {
                     
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                     
                    String senderNum = phoneNumber;
                    String sendedMessage = currentMessage.getDisplayMessageBody();
                    
                    String activationMsg = "142011";
                    if(prefs.contains("activationMsg")){
                    	activationMsg = prefs.getString("activationMsg", "5");
                    }
                    
                    if(activationMsg.equals(sendedMessage)){
                    	Toast.makeText(context, "Cellphone Found", Toast.LENGTH_LONG).show();
    					
    					Intent intent1 = new Intent();
    					intent1.setClassName("com.caribelabs.whereismycellphone", "com.caribelabs.whereismycellphone.controllers.FindingReceiverActivity");
    					intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    			        context.startActivity(intent1);
                    }
                    
                    
                    Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + sendedMessage);
                     
 
                   // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    Toast toast = Toast.makeText(context, "senderNum: "+ senderNum + ", message: " + sendedMessage, duration);
                    toast.show();
                     
                } // end for loop
              } // bundle is null
 
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);
             
        }
    	
    }
	
}
