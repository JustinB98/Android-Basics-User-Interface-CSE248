package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.CardsAndTransactionsAdapter;
import behrman.justin.financialmanager.interfaces.Retriable;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardsWithTransactionsParser;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ParseUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class MonthlyCalculationResultsActivity extends AppCompatActivity implements Retriable {

    public final static String LOG_TAG = MonthlyCalculationResultsActivity.class.getSimpleName() + "debug";

    private ExpandableListView listView;
    private TextView dateView, numberView;
    private ProgressBar progressBar;
    private View container;

    private int month, year;
    private List<Card> cards;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = getLayoutInflater().inflate(R.layout.activity_monthly_calculation_results, null);
        setContentView(root);
        getExtras();
        extractView();
        setToLoadView();
        update();
    }

    private void update() {
        ParseFunctionsUtils.callFunctionInBackgroundShowErrorScreen(StringConstants.PARSE_CLOUD_FUNCTION_CALCULATE_MONTHLY, params(), new ParseFunctionsUtils.DataCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> object) {
                if (object != null) {
                    Log.i(LOG_TAG, "object: " + object);
                    parseData(object);
                }
            }
        }, this, this, LOG_TAG);
    }

    private void parseData(HashMap<String, Object> response) {
        double total = ParseUtils.getTransactionAmount(response.get(StringConstants.TOTAL_KEY));
        ArrayList<Object> transactionData = (ArrayList<Object>) response.get(StringConstants.TRANSACTION_DATA_KEY);
        CardsWithTransactionsParser data = new CardsWithTransactionsParser(transactionData, cards.size());
        Log.i(LOG_TAG, "children: " + data.getChildData());
        Log.i(LOG_TAG, "group names: " + data.getGroupData());
        displayData(total, data);
    }

    private void displayData(double total, CardsWithTransactionsParser data) {
        numberView.setText(ProjectUtils.formatNumber(total));
        dateView.setText(month + "-" + year);
        CardsAndTransactionsAdapter adapter = new CardsAndTransactionsAdapter(this, data.getGroupData(), data.getChildData(), data.getTotals());
        listView.setAdapter(adapter);
        setToReady();
    }

    private HashMap<String, Object> params() {
        HashMap<String, Object> params = new HashMap<>(4);
        ArrayList<String> cardNames = new ArrayList<>(cards.size());
        ArrayList<String> cardTypes = new ArrayList<>(cards.size());
        separateCardList(cardNames, cardTypes);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_CARD_NAME_LIST, cardNames);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_CARD_TYPE_LIST, cardTypes);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_MONTH, month);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_YEAR, year);
        return params;
    }

    private void separateCardList(List<String> cardNames, List<String> cardTypes) {
        for (Card card : cards) {
            cardNames.add(card.getCardName());
            cardTypes.add(card.getCardType().toString());
        }
    }

    private void getExtras() {
        cards = (List<Card>) getIntent().getSerializableExtra(StringConstants.SELECTED_CARDS_KEY);
        year = getIntent().getIntExtra(StringConstants.YEAR_KEY, -1);
        month = getIntent().getIntExtra(StringConstants.MONTH_SPINNER_POSITION_KEY, -1);
        ++month; // was 0-11, need to change 1-12
    }

    private void setToLoadView() {
        container.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setToReady() {
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    private void extractView() {
        listView = findViewById(R.id.list_view);
        dateView = findViewById(R.id.date_view);
        numberView = findViewById(R.id.number_view);
        progressBar = findViewById(R.id.progress_bar);
        container = findViewById(R.id.view_container);
    }

    @Override
    public void retry() {
        setContentView(root);
        update();
    }
}
