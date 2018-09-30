package com.example.android.quakereport;

/**
 * Created by Justin on 9/30/2018.
 */

public class Earthquake {

    private String mMagnitude;
    private String mLocation;
    private String mDate;

    public Earthquake(String magnitude, String location, String date) {
        mMagnitude = magnitude;
        mLocation = location;
        mDate = date;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getMagnitude() {
        return mMagnitude;
    }

    public String getDate() {
        return mDate;
    }
}
