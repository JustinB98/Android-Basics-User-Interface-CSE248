package behrman.justin.financialmanager.subactivities;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.TransactionForMonthAdapter;
import behrman.justin.financialmanager.model.BooleanProperty;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.model.TransactionCommunicator;
import behrman.justin.financialmanager.model.ViewHistoryActivity;

public class ViewHistoryListViewSubActivity {

    private ListView listView;

    private TextView noTransactionsView;
    private AppCompatActivity activity;
    private View root;
    private TransactionCommunicator communicator;

    public ViewHistoryListViewSubActivity(AppCompatActivity activity, TransactionCommunicator communicator) {
        root = LayoutInflater.from(activity).inflate(R.layout.view_all_transactions_for_month, null);
        this.activity = activity;
        this.communicator = communicator;
        extractViews();
    }

    public void setToView() {
        activity.setContentView(root);
    }

    private void extractViews() {
        listView = root.findViewById(R.id.list_view);
        noTransactionsView = root.findViewById(R.id.no_transactions_view);
    }

    public void setListView() {
        Log.i(ViewHistoryActivity.LOG_TAG, "setting list view; loading: " + communicator.loadingProperty().getValue());
        if (!communicator.loadingProperty().getValue()) {
            Log.i(ViewHistoryActivity.LOG_TAG, "transactions: " + communicator.getTransactions());
            setListViewAdapter();
        } else {
            communicator.loadingProperty().addListener(new BooleanProperty.BooleanListener() {
                @Override
                public void onChange(boolean newValue) {
                    setToView();
                    setListView();
                    communicator.loadingProperty().removeListener();
                }
            });
            communicator.setToLoadingScreen();
        }
    }

    private void setListViewAdapter() {
        ArrayList<Transaction> transactions = communicator.getTransactionAsList();
        Log.i(ViewHistoryActivity.LOG_TAG, "transactions: " + transactions);
        if (transactions != null && transactions.size() > 0) {
            TransactionForMonthAdapter adapter = new TransactionForMonthAdapter(activity, transactions, communicator.isManualCard());
            listView.setAdapter(adapter);
            setToListView();
        } else {
            setToTextView();
        }
    }

    private void setToTextView() {
        listView.setVisibility(View.GONE);
        noTransactionsView.setVisibility(View.VISIBLE);
    }

    private void setToListView() {
        listView.setVisibility(View.VISIBLE);
        noTransactionsView.setVisibility(View.GONE);
    }
}
