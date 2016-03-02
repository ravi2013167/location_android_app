package saisrikanth.com.kiddo;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.util.Calendar;

/**
 * Created by Sai Srikanth on 12-02-2015.
 */
public class incomingsms extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    Activity recvmsg = null;
    check_location chkloc = null;

    public void onReceive(Context context, Intent intent) {

        DBHelper mydb;
        SharedPreferences num;
        num = context.getSharedPreferences("options", 0);
        String phnumber = num.getString("phno", "");
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    long timeStamp = currentMessage.getTimestampMillis();
                    Log.d("SmsReceiver", "senderNum: " + senderNum + "; message: " + message + " time stamp" + timeStamp);


                    if (senderNum.equals(phnumber)) {
                        this.abortBroadcast();
                        Location loc = new Location("");
                        loc.setLatitude(Double.parseDouble(message.substring(0, message.indexOf(' '))));
                        loc.setLongitude(Double.parseDouble(message.substring(message.indexOf(' '), message.length())));
                        mydb = new DBHelper(context);
                        mydb.insertintoLogs("" + loc.getLatitude(), "" + loc.getLongitude(), timeStamp);
                        Log.d("SAI", "Before");
                        Log.d("SAI", "After");
                        String check = mydb.isSafe(loc);
                        Log.d("Check value", check);
                        // Show Alert
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context,
                                "senderNum: " + senderNum + ",Latitude: " + loc.getLatitude() + " Longitude: " + loc.getLongitude(), duration);
                        toast.show();


                        if (check.equals("SAFE")) {
                            int duration1 = Toast.LENGTH_LONG;
                            Toast toast1 = Toast.makeText(context, "Safe", duration1);
                            toast1.show();
                        } else {

                            Intent intent1 = new Intent(context, sos.class);
                            // intent1.setClassName("saisrikanth.com.kiddo","saisrikanth.com.kiddo.sos");
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            int duration1 = Toast.LENGTH_LONG;
                            Toast toast1 = Toast.makeText(context, "Danger", duration1);
                            toast1.show();
                            intent1.putExtra("security", check);
                            intent1.putExtra("latitude", loc.getLatitude());
                            intent1.putExtra("longitude", loc.getLongitude());
                            context.startActivity(intent1);
                        }
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }

    public boolean deleteSms(String smsId) {
        boolean isSmsDeleted = false;
        try {
            recvmsg.getContentResolver().delete(
                    Uri.parse("content://sms/" + smsId), null, null);
            isSmsDeleted = true;

        } catch (Exception ex) {
            isSmsDeleted = false;
        }
        return isSmsDeleted;
    }

}
