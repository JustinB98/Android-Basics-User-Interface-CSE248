package behrman.justin.financialmanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.EditTransactionActivity;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class TransactionForMonthAdapter extends ArrayAdapter<Transaction> {

    private final static String LOG_TAG = TransactionForMonthAdapter.class.getSimpleName() + "debug";
    private boolean isManual;

    public TransactionForMonthAdapter(Context context, List<Transaction> transactions, boolean isManual) {
        super(context, 0, transactions);
        this.isManual = isManual;
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
        dateView.setText(ProjectUtils.getFullDate(t.getDate()));
    }

    private void initLongClick(View view, final int position) {
        if (isManual) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    displayPopUpMenu(v, position);
                    return true;
                }
            });
        }
    }

    private void displayPopUpMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuItem editItem = popupMenu.getMenu().add(R.string.edit_transaction_item);
        MenuItem deleteItem = popupMenu.getMenu().add(R.string.delete_transaction_item);
        initClicks(editItem, deleteItem, position);
        popupMenu.show();
    }

    private void initClicks(MenuItem editItem, MenuItem deleteItem, final int position) {
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getContext(), EditTransactionActivity.class);
                intent.putExtra(StringConstants.TRANSACTION_KEY, getItem(position));
                getContext().startActivity(intent);
                return false;
            }
        });
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this transaction!??!");
                builder.setPositiveButton(R.string.accept_transaction_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(LOG_TAG, "deleting transaction");
                    }
                });
                builder.setNegativeButton(R.string.cancel_transaction_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(LOG_TAG, "keeping transaction");
                    }
                });
                builder.create().show();
                return false;
            }
        });
    }

}
