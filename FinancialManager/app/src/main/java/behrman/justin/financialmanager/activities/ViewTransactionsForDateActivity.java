package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.TransactionForSingleDayAdapter;
import behrman.justin.financialmanager.interfaces.OnTotalChange;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewTransactionsForDateActivity extends AppCompatActivity {

    private ListView listView;
    private List<Transaction> transactions;
    private TextView totalView;

    private boolean isManual;

    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions_for_date);
        extractViews();
        initPassedInValues();
        initTotal();
        setUp();
    }

    private void setUp() {
        totalView.setText(ProjectUtils.formatNumber(total));
        TransactionForSingleDayAdapter adapter = new TransactionForSingleDayAdapter(this, transactions, isManual, onRemove());
        listView.setAdapter(adapter);
    }

    private OnTotalChange onRemove() {
        return new OnTotalChange() {
            @Override
            public void onTotalChange(double removedTotal) {
                totalView.setText(ProjectUtils.formatNumber(total - removedTotal));
            }
        };
    }

    private void initPassedInValues() {
        transactions = (List<Transaction>) getIntent().getSerializableExtra(StringConstants.TRANSACTIONS_INTENT_KEY);
        Date date = (Date) getIntent().getSerializableExtra(StringConstants.DATE_KEY);
        getSupportActionBar().setTitle(ProjectUtils.getFullDate(this, date));
        isManual = getIntent().getBooleanExtra(StringConstants.IS_MANUAL_CARD_KEY, false);
    }

    private void initTotal() {
        for (Transaction t : transactions) {
            total += t.getAmount();
        }
    }

    private void extractViews() {
        listView = findViewById(R.id.transactions_list_view);
        totalView = findViewById(R.id.total_view);
    }

}
