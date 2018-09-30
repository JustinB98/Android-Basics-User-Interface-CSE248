package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Justin on 9/30/2018.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private Context mContext;

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> list) {
        super(context, 0, list);
        mContext = context;
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

        TextView magnitudeView = (TextView) convertView.findViewById(R.id.magnitude_view);
        magnitudeView.setText(earthquake.getMagnitude());

        TextView locationView = (TextView) convertView.findViewById(R.id.location_view);
        locationView.setText(earthquake.getLocation());

        TextView dateView = (TextView) convertView.findViewById(R.id.date_view);
        dateView.setText(earthquake.getDate());
        return convertView;
    }
}
