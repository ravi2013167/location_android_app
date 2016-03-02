package saisrikanth.com.kiddo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Sai Srikanth on 04-03-2015.
 */
public class check_location extends Activity {
    SharedPreferences locations;
    Location loc;
    float results[] = new float[2];

    int cnt = locations.getInt("locationCount", 0);
    Location chkloc = new Location("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations = getSharedPreferences("location", 0);
       /* location loc1=new location();
        loc1.setLatitude("159874");
        loc1.setLongitude("741258963");
        String logname;
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int month=c.get(Calendar.MONTH);
        logname="log"+month+"-"+date;
        SharedPreferences mPrefs = getSharedPreferences(logname, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();*/

        /*prefsEditor.putString("loc1",loc.tostring());
        prefsEditor.putString("loc2",loc1.tostring());
        prefsEditor.apply();


        location loc3 =new location(),loc4=new location();
        loc3.setLongitude(mPrefs.getString("loc1", "error"));
        loc4.setLongitude(mPrefs.getString("loc2", "error"));
        tv = (TextView) findViewById(R.id.textView3);
        tv1 = (TextView) findViewById(R.id.textView4);
        tv.setText( loc3.getLongitude());
        tv1.setText( loc4.getLongitude());*/
    }

    public void checkloc(Location recloc) {
        loc = recloc;
        boolean condition = false;
        for (int i = 0; i < cnt; i++) {
            chkloc.setLatitude(Double.parseDouble(locations.getString("lat" + i, "0")));
            chkloc.setLongitude(Double.parseDouble(locations.getString("lng" + i, "0")));
            chkloc.distanceTo(loc);
            if (results[0] < 100) {
                condition = true;
                break;
            }
        }
        if (condition == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Alert Child Out of Boundary");
            alertDialogBuilder.setPositiveButton("Show Location",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent positveActivity = new Intent(getApplicationContext(), saisrikanth.com.kiddo.showchildLocation.class);
                            positveActivity.putExtra("loc", loc);
                            startActivity(positveActivity);

                        }
                    });
            alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() { // define the 'Cancel' button
                public void onClick(DialogInterface dialog, int which) {
                    //Either of the following two lines should work.
                    //dialog.cancel();
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
