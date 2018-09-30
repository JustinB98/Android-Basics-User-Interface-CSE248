package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Justin on 9/30/2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPARATOR = " of ";

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // get a layout inflater instance using the current context of the app.
            // then we inflate the actual layout into a view object
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake earthquake = getItem(position);

        TextView magnitudeView = (TextView) convertView.findViewById(R.id.magnitude);
        double mag = earthquake.getMagnitude();
        String formattedMag = formatMagnitude(mag);
        magnitudeView.setText(formattedMag);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        TextView locationView = (TextView) convertView.findViewById(R.id.primary_location);
        TextView offsetView = (TextView) convertView.findViewById(R.id.location_offset);

        String originalLocation = earthquake.getLocation();
        String primaryLocation, offsetLocation;

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            offsetLocation = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            offsetLocation = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        locationView.setText(primaryLocation);
        offsetView.setText(offsetLocation);

        long time = earthquake.getTimeInTilliseconds();
        Date date = new Date(time);
        String formattedDate = formatDate(date);
        String formattedTime = formatTime(date);

        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        TextView timeView = (TextView) convertView.findViewById(R.id.time);
        dateView.setText(formattedDate);
        timeView.setText(formattedTime);

        return convertView;
    }

    private int getMagnitudeColor(double magnitude) {
        int colorRes;
        int flooredMag = (int) Math.floor(magnitude);
        switch (flooredMag) {
            case 0:
            case 1:
                colorRes = R.color.magnitude1;
                break;
            case 2:
                colorRes = R.color.magnitude2;
                break;
            case 3:
                colorRes = R.color.magnitude3;
                break;
            case 4:
                colorRes = R.color.magnitude4;
                break;
            case 5:
                colorRes = R.color.magnitude5;
                break;
            case 6:
                colorRes = R.color.magnitude6;
                break;
            case 7:
                colorRes = R.color.magnitude7;
                break;
            case 8:
                colorRes = R.color.magnitude8;
                break;
            case 9:
                colorRes = R.color.magnitude9;
                break;
            default:
                colorRes = R.color.magnitude10plus;
        }

        return ContextCompat.getColor(getContext(), colorRes);

    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

}
