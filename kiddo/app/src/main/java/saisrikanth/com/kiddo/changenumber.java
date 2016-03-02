package saisrikanth.com.kiddo;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class changenumber extends ActionBarActivity {
    SharedPreferences number;
    Button save;
    Button clear;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changenumber);
        //save button
        save = (Button) findViewById(R.id.button7);
        //clear button
        clear = (Button) findViewById(R.id.button8);
        et = (EditText) findViewById(R.id.editText2);
        number = getSharedPreferences("options", 0);
        String phno = number.getString("phno", "");
        et.setText(phno);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtxt = String.valueOf(et.getText());
                SharedPreferences.Editor editor = number.edit();
                editor.putString("phno", edtxt);
                editor.commit();
                Toast.makeText(getApplicationContext(),
                        "Number changed", Toast.LENGTH_LONG).show();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et.setText("");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_changenumber, menu);
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
