package saisrikanth.com.kiddo;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import saisrikanth.com.kiddo.R;

public class sos extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        Location loc = new Location("");
        Log.d("Security", "false values" + getIntent().getExtras().getString("Security"));
        double lat = getIntent().getExtras().getDouble("latitude");
        double lng = getIntent().getExtras().getDouble("longitude");
        Log.d("Latitude in sos", "" + lat);
        Log.d("Longitude in sos", "" + lng);
        loc.setLatitude(lat);
        loc.setLongitude(lng);
        TextView tv1 = (TextView) findViewById(R.id.textView13);
        TextView tv2 = (TextView) findViewById(R.id.textView14);
        tv1.setText("" + loc.getLatitude());
        tv2.setText("" + loc.getLongitude());
        // String test=getIntent().getExtras().getString("test");
        //Log.d("Test in sos",test);
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.siren);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        Button cancel = (Button) findViewById(R.id.cancel);
        ImageButton show_loc = (ImageButton) findViewById(R.id.imageButton);
        Button showloc= (Button) findViewById(R.id.showlocation);
        final MediaPlayer finalMMediaPlayer = mMediaPlayer;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalMMediaPlayer.stop();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });

        final Location finalLoc = loc;
        showloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalMMediaPlayer.stop();
                Intent intent = new Intent(sos.this, show_child_loc_map.class);
                intent.putExtra("latitude", finalLoc.getLatitude());
                intent.putExtra("longitude", finalLoc.getLongitude());
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sos, menu);
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
