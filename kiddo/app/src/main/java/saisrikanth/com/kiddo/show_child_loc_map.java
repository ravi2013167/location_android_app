package saisrikanth.com.kiddo;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class show_child_loc_map extends ActionBarActivity {
    GoogleMap googleMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Location loc = new Location("");
        double lat = getIntent().getExtras().getDouble("latitude");
        double lng = getIntent().getExtras().getDouble("longitude");
        Log.d("Latitude in show child", "" + lat);
        Log.d("Longitude in show child", "" + lng);
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        TextView tv = (TextView) findViewById(R.id.textView15);
        //status
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dailog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dailog.show();
        }
        //got connection from gplay
        else {
            tv.setText("" + loc.getLatitude());
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            //get googlemap object from fragment
            googleMap = fm.getMap();
            //my location layer
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude())));

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
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
