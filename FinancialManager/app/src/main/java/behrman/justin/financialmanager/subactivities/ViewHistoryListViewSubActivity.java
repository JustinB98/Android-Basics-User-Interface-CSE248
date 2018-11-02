package behrman.justin.financialmanager.subactivities;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.TransactionForMonthAdapter;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.model.TransactionCommunicator;

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
        ArrayList<Transaction> transactions = new ArrayList<>();
        HashMap<Date, ArrayList<Transaction>> monthTransactions = communicator.getTransactions();
        for (Date date : monthTransactions.keySet()) {
            transactions.addAll(monthTransactions.get(date));
        }
        Collections.sort(transactions);
        setListViewAdapter(transactions);
    }

    private void setListViewAdapter(List<Transaction> transactions) {
        if (transactions != null && transactions.size() > 0) {
            TransactionForMonthAdapter adapter = new TransactionForMonthAdapter(activity, transactions);
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
