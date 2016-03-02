package saisrikanth.com.kiddo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class Add_boundaries extends FragmentActivity {
    DBHelper mydb;
    SharedPreferences boundaries;
    GoogleMap googleMap;
    LocationManager locationManager;
    PendingIntent pendingIntent;
    int locationCount = 0;
    EditText etPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_boundaries);
        //Connecting to db
        mydb = new DBHelper(this);
        TextView tv = (TextView) findViewById(R.id.textView);

        //google play services
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

            //info window changing view
            // Setting a custom info window adapter for the google map
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                // Use default InfoWindow frame
                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                // Defines the contents of the InfoWindow
                @Override
                public View getInfoContents(final Marker arg0) {

                    // Getting view from the layout file info_window_layout
                    View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);

                    // Getting the position from the marker
                    LatLng latLng = arg0.getPosition();

                    // Getting reference to the TextView to set latitude
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView title = (TextView) v.findViewById(R.id.tv_title);
                    // Getting reference to the TextView to set longitude
                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                    tvLat.setText("Latitude:" + latLng.latitude);
                    title.setText("point " + arg0.getTitle());
                    // Setting the longitude
                    tvLng.setText("Longitude:" + latLng.longitude);


                    // Returning the view containing InfoWindow contents
                    return v;

                }
            });
            final ImageView deleteMarker = (ImageView) findViewById(R.id.deleteMarker);
            googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    deleteMarker.setVisibility(View.VISIBLE);
                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    LatLng markerLocation = marker.getPosition();
                    LatLng mapCenter = googleMap.getCameraPosition().target;

                    float[] results = new float[1];
                    Location.distanceBetween(markerLocation.latitude,
                            markerLocation.longitude, mapCenter.latitude,
                            mapCenter.longitude, results);
                    if (results[0] / 1000 < 5000) // the RADIUS depends on Camera Zoom
                    {
                        marker.remove();
                        // ... remove marker here
                    }
                    deleteMarker.setVisibility(View.GONE);
                }
            });

            //open sharedpref object
            boundaries = getSharedPreferences("location", 0);
            //location count
            locationCount = mydb.numberOfRowsStored();
            //tv.setText(""+locationCount);
            //locationCount = boundaries.getInt("locationCount", 0);
            // Getting stored zoom level if exists else return 0
            String zoom = boundaries.getString("zoom", "0");

            //if locations are already inbuilt
            if (locationCount != 0) {
                String lat = "";
                String lng = "";
                ArrayList<Fence> storedlocs = mydb.getAllStoredLocs();
                // Iterating through all the locations stored
                Fence temp = new Fence();
                tv.setText("" + storedlocs.size());
                Log.d("StoredLocCount", "" + mydb.numberOfRowsStored());
                for (int i = 0; i < storedlocs.size(); i++) {
                    //storing ith location in db in temp
                    Log.d("in for loop", "" + storedlocs.get(i).getLongitude());
                    // Drawing marker on the map
                    // drawMarker(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));

                    // Drawing circle on the map
                    drawCircle(storedlocs.get(i));
                    drawMarker(storedlocs.get(i), i);
                }
                // Moving CameraPosition to last clicked position
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(temp.getLatitude(), temp.getLongitude())));
            }
            // Setting the zoom level in the map on last position  is clicked
            //googleMap.animateCamera(CameraUpdateFactory.zoomTo(Float.parseFloat(zoom)));

            googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                TextView radiustv;
                Spinner spinner;

                @Override
                public void onMapLongClick(LatLng latLng) {
                    // Incrementing location count
                    locationCount++;
                    Log.d("onclickListener", "before adding");
                    final Double lat = latLng.latitude;
                    final Double lng = latLng.longitude;
                    Log.d("onclickListener", "after adding");
                    LayoutInflater layoutInflater = (LayoutInflater) Add_boundaries.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View promptView = layoutInflater.inflate(R.layout.prompt, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Add_boundaries.this);
                    alertDialogBuilder.setView(promptView);
                    radiustv = (TextView) promptView.findViewById(R.id.textView18);

                    final SeekBar seekBar = (SeekBar) promptView.findViewById(R.id.seekBar);
                    radiustv.setText("" + seekBar.getProgress() * 50);

                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        int progress = 0;

                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                            progress = progressValue;
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            int num = seekBar.getProgress() * 50;
                            radiustv.setText("" + num);
                        }
                    });
                    final EditText titleet = (EditText) promptView.findViewById(R.id.editText5);
                    spinner = (Spinner) promptView.findViewById(R.id.spinner);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int radius = Integer.parseInt(String.valueOf(seekBar.getProgress() * 50));
                                    String title = titleet.getText().toString();
                                    Log.d("Radius", "" + radius);
                                    Log.d("Security", "" + String.valueOf(spinner.getSelectedItem()));
                                    mydb.insertintoStored(lat.toString(), lng.toString(), radius, title, String.valueOf(spinner.getSelectedItem()));

                                    Fence temp = new Fence();
                                    temp.setLat(lat);
                                    temp.setLong(lng);
                                    temp.setTitle(title + radius);
                                    temp.setRadius(radius);
                                    drawCircle(temp);
                                    drawMarker(temp, mydb.numberOfRowsStored());
                                    Toast.makeText(getBaseContext(), "Fence Added" + "Title : " + title + " Radius " + radius, Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertD = alertDialogBuilder.create();
                    alertD.show();
                }

            });

            /*googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

                @Override
                public void onMapLongClick(LatLng point) {


                }
            });*/
        }


    }

    private void drawCircle(Fence temp) {
        LatLng point = new LatLng(temp.getLatitude(), temp.getLongitude());
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(temp.getRadius());

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        googleMap.addCircle(circleOptions);

    }

    private void drawMarker(Fence temp, int id) {
        // Creating an instance of MarkerOptions
        LatLng point = new LatLng(temp.getLatitude(), temp.getLongitude());
        Log.d("Count", "" + temp.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);
        markerOptions.title("" + temp.getTitle() + "\n ID : " + id);

        // Adding marker on the Google Map
        Marker marker = googleMap.addMarker(markerOptions);
        marker.showInfoWindow();
        //googleMap.addMarker(new MarkerOptions().
        //      position(new LatLng(point.latitude, point.longitude)).title(" " + temp.getTitle()+"ID"+id));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_boundaries, menu);
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
