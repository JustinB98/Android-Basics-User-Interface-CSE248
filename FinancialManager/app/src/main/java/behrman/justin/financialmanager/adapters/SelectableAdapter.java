package behrman.justin.financialmanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;

public class SelectableAdapter extends ArrayAdapter<Card> {

    public final static String LOG_TAG = SelectableAdapter.class.getSimpleName() + "debug";

    private ListView listView;

    public SelectableAdapter(Context context, List<Card> cards, ListView listView) {
        super(context, 0, cards);
        this.listView = listView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_monthly_calculations, parent, false);
        }
        // you have to remove the listeners so they don't get called again
        removeListeners(convertView);
        putCardIntoViews(position, convertView);
        // after putting the data in, then put back the listeners
        initCheckBoxListener(convertView, position);
        initOnClick(convertView);
        return convertView;
    }

    // https://stackoverflow.com/questions/3149414/how-to-receive-a-event-on-android-checkbox-check-change
    // @Tatarize
    private void removeListeners(View convertView) {
        convertView.setOnClickListener(null);
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(null);
    }

    private void putCardIntoViews(int pos, View convertView) {
        Card currentCard = getItem(pos);
        // Log.i(LOG_TAG, "card: " + currentCard + ", item checked: " + listView.isItemChecked(pos));
        TextView cardNameView = (TextView) convertView.findViewById(R.id.card_name_view);
        cardNameView.setText(currentCard.getCardName());
        TextView cardTypeView = (TextView) convertView.findViewById(R.id.card_type_view);
        cardTypeView.setText(currentCard.getCardType().toString());
        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
        checkBox.setChecked(listView.isItemChecked(pos));
    }

    private void initOnClick(View convertView) {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = v.findViewById(R.id.checkbox);
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
    }

    private void initCheckBoxListener(View convertView, final int position) {
        CheckBox checkBox = convertView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // View view = (View) buttonView.getParent();
                // view.setSelected(isChecked);
                // turns out that you get can the list view by getting the parent of the parent
                // ListView listView = (ListView) buttonView.getParent().getParent();
                listView.setItemChecked(position, isChecked);
            }
        });
    }

}
