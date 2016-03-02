package saisrikanth.com.kiddo;

/**
 * Created by Sai Srikanth on 08-03-2015.
 */

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.text.format.DateUtils;
import android.util.Log;

import static java.util.Calendar.DATE;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "kiddo.db";
    public static final String STOREDLOCATIONS_TABLE_NAME = "StoredLocations";
    public static final String LOG_TABLE_NAME = "LocationsLog";
    public static final String STOREDLOCATIONS_COLUMN_DATETIME = "date_time";
    public static final String STOREDLOCATIONS_COLUMN_LATITUDE = "latitude";
    public static final String STOREDLOCATIONS_COLUMN_LONGITUDE = "longitude";
    public static final String STOREDLOCATIONS_COLUMN_RADIUS = "radius";
    public static final String STOREDLOCATIONS_COLUMN_STREET = "street";
    public static final String STOREDLOCATIONS_COLUMN_CITY = "place";
    public static final String STOREDLOCATIONS_COLUMN_PHONE = "phone";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table  IF NOT EXISTS StoredLocations" +
                        "(id integer primary key autoincrement, latitude text,longitude text,radius integer,title text,security text)"
        );
        db.execSQL("create table  IF NOT EXISTS LocationsLog" +
                        "(id integer primary key autoincrement, latitude text,longitude text,date_time TIMESTAMP)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS StoredLocations");
        onCreate(db);
    }

    public boolean insertintoStored(String latitude, String longitude, int radius, String title, String secure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("latitude", latitude);
        contentValues.put("title", title);
        contentValues.put("longitude", longitude);
        contentValues.put("radius", radius);
        contentValues.put("security", secure);
        db.insert("StoredLocations", null, contentValues);
        return true;
    }

    public Cursor getStoredData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from StoredLocations ", null);
        return res;
    }

    public int numberOfRowsStored() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, STOREDLOCATIONS_TABLE_NAME);
        return numRows;
    }

    public boolean insertintoLogs(String latitude, String longitude, long timestamp) {
        //String s=MillitoDateTime(timestamp);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        contentValues.put("date_time", String.valueOf(new Timestamp(timestamp)));
        db.insert("LocationsLog", null, contentValues);
        return true;
    }

    public Integer deleteStoredLoc(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("In delete", "In delete");
        return db.delete("StoredLocations",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<Fence> getAllStoredLocs() {
        ArrayList<Fence> array_list = new ArrayList<Fence>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from StoredLocations", null);
        res.moveToFirst();

        int i = 0;
        while (!res.isAfterLast()) {
            Fence loc = new Fence();
            loc.setLat(Double.parseDouble(res.getString(res.getColumnIndex(STOREDLOCATIONS_COLUMN_LATITUDE))));
            loc.setLong(Double.parseDouble(res.getString(res.getColumnIndex(STOREDLOCATIONS_COLUMN_LONGITUDE))));
            loc.setRadius(res.getInt(res.getColumnIndex(STOREDLOCATIONS_COLUMN_RADIUS)));
            Log.d("indbcheck", "" + loc.getLongitude());
            array_list.add(loc);
            Log.d("check2", "" + array_list.get(i).getRadius());
            i++;
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<Location> PreviousLoconDate(int date, int month, int year) {
        ArrayList<Location> array_list = new ArrayList<Location>();
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        GregorianCalendar gregorianCalendar = new GregorianCalendar(year, month, date);
        GregorianCalendar gregorianCalendar1 = new GregorianCalendar(year, month, date);
        gregorianCalendar1.add(DATE, 1);
        Log.d("calender1", "" + gregorianCalendar.getTime());
        Log.d("calender2", "" + gregorianCalendar1.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String timestamp = dateFormat.format(gregorianCalendar.getTime());
        String timestamp1 = dateFormat.format(gregorianCalendar1.getTime());
        Log.d("formatted time", timestamp);
        Log.d("formatted time2", timestamp1);
        //String timestamp1=year+"-"+month+"-"+date+1;
        Cursor res = db.rawQuery("select * from LocationsLog where date_time between '" + timestamp + "' and '" + timestamp1 + "'", null);
        res.moveToFirst();

        int i = 0;
        while (!res.isAfterLast()) {
            Location loc = new Location("");
            loc.setLatitude(Double.parseDouble(res.getString(res.getColumnIndex(STOREDLOCATIONS_COLUMN_LATITUDE))));
            loc.setLongitude(Double.parseDouble(res.getString(res.getColumnIndex(STOREDLOCATIONS_COLUMN_LONGITUDE))));
            String input = res.getString(res.getColumnIndex(STOREDLOCATIONS_COLUMN_DATETIME));

            java.util.Date test = null;
            try {
                test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH).parse(input);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("parse excetption", "check parsing");
            }
            loc.setTime(test.getTime());

            Log.d("previous loc check", "" + loc.getLongitude());
            array_list.add(loc);
            Log.d("previous loc", "" + array_list.get(i).getTime());
            i++;
            res.moveToNext();
        }
        return array_list;
    }

    public String isSafe(Location loc) {
        ArrayList<Fence> storedLocs = new ArrayList<Fence>();
        storedLocs = getAllStoredLocs();
        Location temp = new Location("");
        float radius;
        String check = "DANGER";
        for (int i = 0; i < storedLocs.size(); i++) {
            temp.setLatitude(storedLocs.get(i).getLatitude());
            temp.setLongitude(storedLocs.get(i).getLongitude());
            radius = storedLocs.get(i).getRadius();
            float distance = temp.distanceTo(loc);
            Log.d("Check in DBhepler", "point " + i + "radius" + radius + "distance" + distance);
            if (distance < radius) {

                if (storedLocs.get(i).getSecurity().equals("SAFE")) {
                    check = "SAFE";
                    Log.d("check condition","SAFE");
                    //return check;
                } else if (storedLocs.get(i).getSecurity().equals("UNSAFE")) {
                    check = "UNSAFE";
                    //return check;
                }
            }

        }
        return check;
    }

    private String MillitoDateTime(long currentDateTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(currentDateTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(dateFormat.format(cal.getTime()));
    }

    private long DateTimetoMilli(String datetime) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date TimeinMilli = (Date) dateFormat.parse(datetime);
        return TimeinMilli.getTime();
    }

}