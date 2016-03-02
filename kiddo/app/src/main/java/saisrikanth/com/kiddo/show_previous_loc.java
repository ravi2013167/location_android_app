package saisrikanth.com.kiddo;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class show_previous_loc extends ActionBarActivity {
    GoogleMap googleMap;
    DBHelper mydb;
    int date, month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_previous_loc);
        date = getIntent().getExtras().getInt("date");
        month = getIntent().getExtras().getInt("month");
        year = getIntent().getExtras().getInt("year");

        mydb = new DBHelper(this);
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, date);
        Log.d("date through gregorian", "" + gregorianCalendar.getTime());
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        format.setCalendar(gregorianCalendar);
        String check_date = format.format(gregorianCalendar.getTime());
        ArrayList<Location> locations = mydb.PreviousLoconDate(date, month, year);
        Log.d("date and time from intent pasing", "date" + date + " month" + month + " year" + year + locations.size() + "check date" + check_date);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        //status
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dailog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dailog.show();
        }
        //got connection from gplay
        else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
            //get googlemap object from fragment
            googleMap = fm.getMap();
            //my location layer
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date testdate = new Date();
            String temp;
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(Color.RED);
            polylineOptions.width(5);


            for (int i = 0; i < locations.size(); i++) {
                Location loc = new Location("");
                loc.setLatitude(locations.get(i).getLatitude());
                loc.setLongitude(locations.get(i).getLongitude());

                testdate.setTime(locations.get(i).getTime());
                temp = dateFormat.format(testdate);
                Marker tp = googleMap.addMarker((new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())).title(temp)));
                polylineOptions.add(new LatLng(loc.getLatitude(), loc.getLongitude()));
            }
            googleMap.addPolyline(polylineOptions);
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude()));
            googleMap.moveCamera(center);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_previous_loc, menu);
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
}
