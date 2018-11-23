package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import behrman.justin.financialmanager.R;

public class QueryTransactionActivity extends AppCompatActivity {

    private EditText placeField, minAmountField, maxAmountField, minYearField, maxYearField;
    private Spinner minMonthSpinner, maxMonthSpinner;
    private ListView listView;
    private ProgressBar progressBar;
    private View container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_transaction);
        extractViews();
        setToLoading();
        initClick();
        initGetCards();
    }

    private void initGetCards() {

    }

    private void initClick() {

    }

    private void setToLoading() {
        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setToView() {
        container.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void extractViews() {
        placeField = findViewById(R.id.place_input);
        minAmountField = findViewById(R.id.min_amount_input);
        maxAmountField = findViewById(R.id.max_amount_input);
        minMonthSpinner = findViewById(R.id.min_month_spinner);
        maxMonthSpinner = findViewById(R.id.max_month_spinner);
        minYearField = findViewById(R.id.min_year_input);
        maxYearField = findViewById(R.id.max_year_input);
        listView = findViewById(R.id.list_view);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.container);
    }

}
