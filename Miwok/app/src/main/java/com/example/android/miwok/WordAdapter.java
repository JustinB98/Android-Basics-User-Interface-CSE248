package com.example.android.miwok;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Justin on 9/21/2018.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    public WordAdapter(Activity context, ArrayList<Word> words) {
        // we don't need a resource since we'll be creating it by ourselfs
        super(context, 0, words);
    }

    /**
     *
     * @param position  position requested
     * @param convertView   scrap view that can be null if not enough scrap views were made
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // if scrap view is null, then create a view that is based on the list view xml file
        if (convertView == null) {
            // LayoutInflater creates a view from an xml file
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // grab all the fields from the view and set their values
        TextView miwokView = (TextView) convertView.findViewById(R.id.miwok_text_view);
        Word currentWord = getItem(position);
        miwokView.setText(currentWord.getMiwokTranslation());
        TextView defaultView = (TextView) convertView.findViewById(R.id.default_text_view);
        defaultView.setText(currentWord.getDefaultTranslation());
        return convertView;
    }

}
