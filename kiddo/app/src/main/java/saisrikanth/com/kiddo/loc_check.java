package saisrikanth.com.kiddo;

/**
 * Created by Sai Srikanth on 11-02-2015.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;

public class loc_check extends Activity {

    TextView tv;
    TextView tv1;
    SharedPreferences log;
    TextView logc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loc_check);
        logc = (TextView) findViewById(R.id.textView5);

        /*location loc= (location) getIntent().getSerializableExtra("loc");
        location loc1=new location();
        loc1.setLatitude("159874");
        loc1.setLongitude("741258963");
        String logname;
        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DATE);
        int month=c.get(Calendar.MONTH);
        logname="log"+month+"-"+date;
        SharedPreferences  mPrefs = getSharedPreferences(logname, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("loc1",loc.tostring());
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
}