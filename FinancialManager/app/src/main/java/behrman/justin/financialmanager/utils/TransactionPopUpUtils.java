package behrman.justin.financialmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.EditTransactionActivity;
import behrman.justin.financialmanager.model.ItemGetter;
import behrman.justin.financialmanager.model.Transaction;

public class TransactionPopUpUtils {

    private final static String LOG_TAG = TransactionPopUpUtils.class.getSimpleName() + "debug";

    private Context context;
    private ItemGetter<Transaction> itemGetter;

    public TransactionPopUpUtils(Context context, ItemGetter<Transaction> itemGetter) {
        this.context = context;
        this.itemGetter = itemGetter;
    }

    public void displayPopUpMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        MenuItem editItem = popupMenu.getMenu().add(R.string.edit_transaction_item);
        MenuItem deleteItem = popupMenu.getMenu().add(R.string.delete_transaction_item);
        initClicks(editItem, deleteItem, position);
        popupMenu.show();
    }

    private void initClicks(MenuItem editItem, MenuItem deleteItem, final int position) {
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, EditTransactionActivity.class);
                intent.putExtra(StringConstants.TRANSACTION_KEY, itemGetter.getItem(position));
                context.startActivity(intent);
                return false;
            }
        });
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
