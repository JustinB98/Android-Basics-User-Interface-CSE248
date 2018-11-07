package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    private boolean isManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions_for_date);
        listView = findViewById(R.id.transactions_list_view);
        transactions = (List<Transaction>) getIntent().getSerializableExtra(StringConstants.TRANSACTIONS_INTENT_KEY);
        Date date = (Date) getIntent().getSerializableExtra(StringConstants.DATE_KEY);
        getSupportActionBar().setTitle(ProjectUtils.getFullDate(date));
        isManual = getIntent().getBooleanExtra(StringConstants.IS_MANUAL_CARD_KEY, false);
        setUp();
    }

    private void setUp() {
        TransactionForSingleDayAdapter adapter = new TransactionForSingleDayAdapter(this, transactions, isManual);
        listView.setAdapter(adapter);
    }

}
