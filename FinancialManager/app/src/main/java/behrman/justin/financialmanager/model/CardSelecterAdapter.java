package behrman.justin.financialmanager.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import behrman.justin.financialmanager.R;

public class CardSelecterAdapter extends ArrayAdapter<Card> {

    public CardSelecterAdapter(Context context, List<Card> cards) {
        super(context, 0, cards);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_layout, parent, false);
        }
        putCardIntoViews(position, convertView);
        return convertView;
    }

    private void putCardIntoViews(int pos, View convertView) {
        Card currentCard = getItem(pos);
        TextView cardNameView = (TextView) convertView.findViewById(R.id.card_name_view);
        cardNameView.setText(currentCard.getCardName());
        TextView cardTypeView = (TextView) convertView.findViewById(R.id.card_type_view);
        cardTypeView.setText(currentCard.getCardType().toString());
    }

}
