package behrman.justin.financialmanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;

public class TransactionForMonthAdapter extends ArrayAdapter<Transaction> {

    private final static String LOG_TAG = TransactionForMonthAdapter.class.getSimpleName() + "debug";

    public TransactionForMonthAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_transactions, parent, false);
        }
        Transaction t = getItem(position);
        setFields(convertView, t);
        // initLongClick(convertView);
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
        dateView.setText(ProjectUtils.getFullDate(t.getDate()));
    }

    private void initLongClick(View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i("long_click_test", "v: " + v + ", long click");
                return true;
            }
        });
    }

}
