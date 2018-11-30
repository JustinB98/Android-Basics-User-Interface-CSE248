package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.adapters.CardsAndTransactionsAdapter;
import behrman.justin.financialmanager.interfaces.Retriable;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardsWithTransactionsParser;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class QueryTransactionsResultActivity extends AppCompatActivity implements Retriable {

    private final static String LOG_TAG = QueryTransactionsResultActivity.class.getSimpleName() + "debug";

    private String place;
    private double minAmount, maxAmount;
    private int minMonth, minYear, maxMonth, maxYear;
    private List<Card> cards;

    private ExpandableListView listView;
    private ProgressBar progressBar;

    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = getLayoutInflater().inflate(R.layout.activity_query_transactions_result, null);
        setContentView(root);
        extractViews();
        extractPassedInValues();
        setToLoading();
        sendRequestToServer();
    }

    private void sendRequestToServer() {
        ParseFunctionsUtils.callFunctionInBackgroundShowErrorScreen(StringConstants.PARSE_CLOUD_FUNCTION_QUERY_TRANSACTIONS, params(), new ParseFunctionsUtils.DataCallback<ArrayList<Object>>() {
            @Override
            public void done(ArrayList<Object> object) {
                if (object != null) {
                    Log.i(LOG_TAG, "returned: " + object);
                    CardsWithTransactionsParser data = new CardsWithTransactionsParser(object, cards.size());
                    Log.i(LOG_TAG, "cards: " + data.getGroupData());
                    Log.i(LOG_TAG, "transaction data: " + data.getChildData());
                    CardsAndTransactionsAdapter adapter = new CardsAndTransactionsAdapter(QueryTransactionsResultActivity.this, data.getGroupData(), data.getChildData(), data.getTotals());
                    listView.setAdapter(adapter);
                    setToReady();
                }
            }
        }, this, this, LOG_TAG);
    }

    private void setToLoading() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    private void setToReady() {
        progressBar.setVisibility(View.GONE);
        listView.setVisibility(View.VISIBLE);
    }

    private HashMap<String, Object> params() {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put(StringConstants.PLACE_KEY, place);
        params.put(StringConstants.MIN_AMOUNT_KEY, minAmount);
        params.put(StringConstants.MAX_AMOUNT_KEY, maxAmount);
        params.put(StringConstants.MIN_MONTH_KEY, minMonth);
        params.put(StringConstants.MIN_YEAR_KEY, minYear);
        params.put(StringConstants.MAX_MONTH_KEY, maxMonth);
        params.put(StringConstants.MAX_YEAR_KEY, maxYear);
        ArrayList<String> cardNames = new ArrayList<>(cards.size());
        ArrayList<String> cardTypes = new ArrayList<>(cards.size());
        separateCardFields(cardNames, cardTypes);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_CARD_NAME_LIST, cardNames);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_CARD_TYPE_LIST, cardTypes);
        return params;
    }

    private void separateCardFields(ArrayList<String> cardNames, ArrayList<String> cardTypes) {
        for (Card c : cards) {
            cardNames.add(c.getCardName());
            cardTypes.add(c.getCardType().toString());
        }
    }

    private void extractPassedInValues() {
        place = getIntent().getStringExtra(StringConstants.PLACE_KEY);
        minAmount = getIntent().getDoubleExtra(StringConstants.MIN_AMOUNT_KEY, -1.0);
        maxAmount = getIntent().getDoubleExtra(StringConstants.MAX_AMOUNT_KEY, -1.0);
        minMonth = getIntent().getIntExtra(StringConstants.MIN_MONTH_KEY, -1);
        minYear = getIntent().getIntExtra(StringConstants.MIN_YEAR_KEY, -1);
        maxMonth = getIntent().getIntExtra(StringConstants.MAX_MONTH_KEY, -1);
        maxYear = getIntent().getIntExtra(StringConstants.MAX_YEAR_KEY, -1);
        cards = (List<Card>) getIntent().getSerializableExtra(StringConstants.CARDS_KEY);
    }

    private void extractViews() {
        listView = findViewById(R.id.list_view);
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    public void retry() {
        setContentView(root);
        sendRequestToServer();
    }
}
