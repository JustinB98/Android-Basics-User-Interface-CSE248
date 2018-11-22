package behrman.justin.financialmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.activities.EditTransactionActivity;
import behrman.justin.financialmanager.interfaces.ItemGetter;
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
        initEditItem(editItem, position);
        initDeleteMenuItem(deleteItem, position);
        popupMenu.show();
    }

    private void initEditItem(MenuItem editItem, final int position) {
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(context, EditTransactionActivity.class);
                intent.putExtra(StringConstants.TRANSACTION_KEY, itemGetter.getItem(position));
                context.startActivity(intent);
                return false;
            }
        });

    }

    private void initDeleteMenuItem(final MenuItem deleteItem, final int position) {
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.delete_transaction_prompt);
                builder.setPositiveButton(R.string.accept_transaction_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(position);
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

    private void deleteItem(final int position) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put(StringConstants.OBJECT_ID_KEY, itemGetter.getItem(position).getObjectId());
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_DELETE_MANUAL_TRANSACTION, params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if (e == null) {
                    if (ProjectUtils.deepEquals(object, StringConstants.SUCCESS)) {
                        Toast.makeText(context, "Deleted...", Toast.LENGTH_SHORT).show();
                        itemGetter.removeItem(itemGetter.getItem(position));
                        ProjectUtils.setItemDeleted(true);
                    }
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e, context);
                }
            }
        });
    }
}
