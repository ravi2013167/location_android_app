package saisrikanth.com.kiddo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toggle on/off button
        final ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);

        final SharedPreferences prefs = getSharedPreferences("options", 0);
        final SharedPreferences.Editor editor = prefs.edit();

        boolean status = prefs.getBoolean("status", false);
        tb.setChecked(prefs.getBoolean("status", true));


        final String tbstatus = (String) tb.getText();

        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dont think of it this is correct ;)
                if (tb.isChecked()) {

                    editor.putBoolean("status", true);


                } else {

                    editor.putBoolean("status", false);

                }

                editor.commit();
                Boolean check = prefs.getBoolean("status", false);
                Toast.makeText(MainActivity.this, "" + check,
                        Toast.LENGTH_SHORT).show();
            }
        });


        //Add Boundaries
        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Add_boundaries.class);
                startActivity(in);
            }
        });
        //Set Child Number
        Button b6 = (Button) findViewById(R.id.button6);
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, changenumber.class);
                startActivity(in);
            }
        });
        Button b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//wheres my child on click listener
            }
        });
        //show maps example
        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, maps.class);
                startActivity(in);
            }
        });
        //previous locs
        Button previoslocs = (Button) findViewById(R.id.button11);
        previoslocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, Previous_Logs.class);
                startActivity(in);
            }
        });
        //where is my child
        Button find_child=(Button)findViewById(R.id.button3);
        find_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, findChild.class);
                startActivity(in);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
