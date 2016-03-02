package saisrikanth.com.kiddo;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.io.Serializable;


public class showchildLocation extends FragmentActivity {


    GoogleMap googleMap;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showchild_location);
        Location loc = (Location) getIntent().getExtras().getSerializable("loc");
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        //status
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dailog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dailog.show();
        }
        //got connection from gplay
        else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            //get googlemap object from fragment
            googleMap = fm.getMap();
            //my location layer
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            Marker tp = googleMap.addMarker((new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude()))));
            Location myloc = googleMap.getMyLocation();
            Location zoomloc = new Location("");
            Marker tp2 = googleMap.addMarker((new MarkerOptions().position(new LatLng(myloc.getLatitude(), myloc.getLongitude()))));

            zoomloc.setLongitude(Math.abs(myloc.getLongitude() - loc.getLongitude()));
            zoomloc.setLatitude(Math.abs(myloc.getLatitude() - loc.getLongitude()));
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(zoomloc.getLatitude(), zoomloc.getLongitude()));
            googleMap.moveCamera(center);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_showchild_location, menu);
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
