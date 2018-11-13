package behrman.justin.financialmanager.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.ItemGetter;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.model.TransactionCommunicator;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.TransactionPopUpUtils;

public class TransactionForMonthAdapter extends ArrayAdapter<Transaction> {

    private final static String LOG_TAG = TransactionForMonthAdapter.class.getSimpleName() + "debug";
    private boolean isManual;

    private TransactionPopUpUtils popUpUtils;

    public TransactionForMonthAdapter(Context context, List<Transaction> transactions, boolean isManual, final TransactionCommunicator communicator) {
        super(context, 0, transactions);
        this.isManual = isManual;
        popUpUtils = new TransactionPopUpUtils(context, new ItemGetter<Transaction>() {
            @Override
            public Transaction getItem(int position) {
                return TransactionForMonthAdapter.this.getItem(position);
            }

            @Override
            public void removeItem(Transaction object) {
                TransactionForMonthAdapter.this.remove(object);
            }

        });
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_transactions, parent, false);
            initLongClick(convertView, position);
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
        dateView.setText(ProjectUtils.getFullDate(getContext(), t.getDate()));
    }

    private void initLongClick(View view, final int position) {
        if (isManual) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popUpUtils.displayPopUpMenu(v, position);
                    return true;
                }
            });
        }
    }

}
