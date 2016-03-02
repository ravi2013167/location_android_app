package saisrikanth.com.kiddo;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class findChild extends ActionBarActivity {
     static ProgressDialog p;
     static boolean progressDialogFlag=false;
     static ProgressDialog progressDialog;
     BroadcastReceiver smsReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_child);
        IntentFilter intentFilter = new IntentFilter("SmsReceiver");
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        smsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SharedPreferences num;
                num = context.getSharedPreferences("options", 0);
                String phnumber = num.getString("phno", "");

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
                                progressDialogFlag = false;
                                progressDialog.dismiss();
                                Location loc = new Location("");
                                loc.setLatitude(Double.parseDouble(message.substring(0, message.indexOf(' '))));
                                loc.setLongitude(Double.parseDouble(message.substring(message.indexOf(' '), message.length())));
                                Intent intent1 = new Intent(context, sos.class);
                                intent1.setClassName("saisrikanth.com.kiddo","saisrikanth.com.kiddo.show_child_loc_map");
                                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.putExtra("latitude", loc.getLatitude());
                                intent1.putExtra("longitude", loc.getLongitude());
                                context.startActivity(intent1);

                                Log.d("Sms received", "Sms received");
                                finish();
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("SmsReceiver", "Exception smsReceiver" + e);

                }
            }
        };
        this.registerReceiver(smsReceiver, intentFilter);
        sendSMS();
        progressDialog=new ProgressDialog(this);
        SMSReceiver test=new SMSReceiver();
        test.execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_child, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // Method to send SMS.
    private void sendSMS() {
        SharedPreferences num;
        num = getSharedPreferences("options", 0);
        String mobNo= num.getString("phno", "");
        String message="EMERGENCY";
        String smsSent = "SMS_SENT";
        String smsDelivered = "SMS_DELIVERED";
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(smsSent), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(smsDelivered), 0);

        // Receiver for Sent SMS.
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(smsSent));

        // Receiver for Delivered SMS.
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(smsDelivered));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(mobNo, null, message, sentPI, deliveredPI);


    }

    public class SMSReceiver extends AsyncTask<Void, Integer, Void> {


        protected void onPreExecute() {
            // start a progressdialog box
            super.onPreExecute();
            progressDialogFlag = true;
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Waiting to Receive SMS...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            if (progressDialogFlag == true) {
                progressDialog.setProgress(progress[0]);
            }
            if (progress[0] == 100 || progressDialogFlag == false) {
                progressDialog.dismiss();
            }
        }

        protected void onPostExecute(Void result) {

        }

        @Override
        protected Void doInBackground(Void... params) {
            int interval = 2;
            int totalDuration = 5 * 60 * 1000; // 5 min
            int sleepPeriod = (totalDuration / 100) * interval;
            for (int i = 2; i <= 100; i += 2) {
                publishProgress(i);
                try {
                    Thread.sleep(sleepPeriod);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }


}
