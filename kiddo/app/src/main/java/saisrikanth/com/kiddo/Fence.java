package saisrikanth.com.kiddo;

import android.location.Location;

/**
 * Created by Sai Srikanth on 13-03-2015.
 */
public class Fence {
    public Location location;
    private int radius;
    private String security;
    private String title;

    public Fence() {
        this.location = new Location("");
        this.radius = 0;
        this.title = "";
    }

    public Fence(Location location, int radius, String title) {
        this.location = location;
        this.radius = radius;
        this.title = title;
    }

    public Fence(String latitude, String longitude, int radius, String title) {
        this.location.setLatitude(Double.parseDouble(latitude));
        this.location.setLongitude(Double.parseDouble(longitude));
        this.radius = radius;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setLat(Double lat) {
        this.location.setLatitude(lat);
    }

    public void setLong(Double lat) {
        this.location.setLongitude(lat);
    }

    public double getLatitude() {
        return this.location.getLatitude();
    }

    public double getLongitude() {
        return this.location.getLongitude();
    }

}
