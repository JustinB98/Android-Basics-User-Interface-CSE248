package behrman.justin.financialmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.TransactionForSingleDayAdapter;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewTransactionsForDateActivity extends AppCompatActivity {

    private ListView listView;
    private List<Transaction> transactions;

    private Transaction transactionBeingEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions_for_date);
        listView = findViewById(R.id.transactions_list_view);
        transactions = (List<Transaction>) getIntent().getSerializableExtra(StringConstants.TRANSACTIONS_INTENT_KEY);
        Date date = (Date) getIntent().getSerializableExtra(StringConstants.DATE_KEY);
        getSupportActionBar().setTitle(ProjectUtils.getFullDate(date));
        setUp();
        boolean isManual = getIntent().getBooleanExtra(StringConstants.IS_MANUAL_CARD_KEY, false);
        if (isManual) {
            initListViewItemListener();
        }
    }

    private void initListViewItemListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                transactionBeingEdited = (Transaction) parent.getItemAtPosition(position);
                switchToEditTransaction(transactionBeingEdited);
            }
        });
    }

    private void setUp() {
        TransactionForSingleDayAdapter adapter = new TransactionForSingleDayAdapter(this, transactions);
        listView.setAdapter(adapter);
    }

    private void switchToEditTransaction(Transaction t) {
        Intent intent = new Intent(this, EditTransactionActivity.class);
        intent.putExtra(StringConstants.TRANSACTION_KEY, t);
        startActivity(intent);
    }

}
