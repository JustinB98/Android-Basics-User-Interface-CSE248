package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.TransactionAdapter;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewTransactionsForDateActivity extends AppCompatActivity {

    private ListView listView;
    private List<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions_for_date);
        listView = findViewById(R.id.transactions_list_view);
        transactions = (List<Transaction>) getIntent().getSerializableExtra(StringConstants.TRANSACTIONS_KEY);
        setUp();
    }

    private void setUp() {
        TransactionAdapter adapter = new TransactionAdapter(this, transactions);
        listView.setAdapter(adapter);
    }

}
