package saisrikanth.com.kiddo;

/**
 * Created by Sai Srikanth on 03-03-2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationView extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        TextView tv = (TextView) findViewById(R.id.tv_notification);
        Bundle data = getIntent().getExtras();
        tv.setText(data.getString("content"));
    }
}