package behrman.justin.financialmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;

// https://www.youtube.com/watch?v=jZxZIFnJ9jE
public class CardsAndTransactionsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Card> listDataHeader;
    private HashMap<Card, List<Transaction>> listHashMap;
    private ArrayList<Double> totals;

    public CardsAndTransactionsAdapter(Context context, List<Card> listDataHeader, HashMap<Card, List<Transaction>> listHashMap, ArrayList<Double> totals) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.totals = totals;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Card groupCard = listDataHeader.get(groupPosition);
        List<Transaction> transactions = listHashMap.get(groupCard);
        return transactions.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Card groupCard = listDataHeader.get(groupPosition);
        List<Transaction> transactions = listHashMap.get(groupCard);
        return transactions.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Card header = (Card)getGroup(groupPosition);
        double total = totals.get(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.header_list_item, parent, false);
        }
        TextView cardView = convertView.findViewById(R.id.card_view);
        cardView.setText(header.getCardName() + " - " + header.getCardType());
        TextView totalView = convertView.findViewById(R.id.total_view);
        totalView.setText(ProjectUtils.formatNumber(total).replace("$", ""));
        /*
        TextView cardNameView = convertView.findViewById(R.id.card_name_view);
        cardNameView.setText(header.getCardName());
        TextView cardTypeView = convertView.findViewById(R.id.card_type_view);
        cardTypeView.setText(header.getCardType().toString());
        */
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Transaction transaction = (Transaction) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_transactions, parent, false);
        }
        setFields(convertView, transaction);
        return convertView;
    }

    private void setFields(View convertView, Transaction t) {
        TextView placeView = convertView.findViewById(R.id.place_view);
        placeView.setText(t.getPlace());
        TextView amountView = convertView.findViewById(R.id.amount_view);
        amountView.setText(ProjectUtils.formatNumber(t.getAmount()).replace("$", "")); // want to get rid of $
        TextView currencyCodeView = convertView.findViewById(R.id.currency_code_view);
        currencyCodeView.setText(t.getCurrencyCode());
        TextView dateView = convertView.findViewById(R.id.date_view);
        dateView.setText(ProjectUtils.getFullDate(context, t.getDate()));
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
