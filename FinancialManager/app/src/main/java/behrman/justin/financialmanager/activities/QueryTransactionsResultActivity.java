package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardsWIthTransactionsParser;
import behrman.justin.financialmanager.utils.ParseExceptionUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class QueryTransactionsResultActivity extends AppCompatActivity {

    private final static String LOG_TAG = QueryTransactionsResultActivity.class.getSimpleName() + "debug";

    private String place;
    private double minAmount, maxAmount;
    private int minMonth, minYear, maxMonth, maxYear;
    private List<Card> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_transactions_result);
        extractPassedInValues();
        sendRequestToServer();
    }

    private void sendRequestToServer() {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_QUERY_TRANSACTIONS, params(), new FunctionCallback<ArrayList<Object>>() {
            @Override
            public void done(ArrayList<Object> object, ParseException e) {
                if (e == null) {
                    Log.i(LOG_TAG, "returned: " + object);
                    CardsWIthTransactionsParser data = new CardsWIthTransactionsParser(object, cards.size());
                    Log.i(LOG_TAG, "cards: " + data.getGroupData());
                    Log.i(LOG_TAG, "transaction data: " + data.getChildData());
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                    ParseExceptionUtils.displayErrorMessage(e, QueryTransactionsResultActivity.this);
                }
            }
        });
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
        seperateCardFields(cardNames, cardTypes);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_CARD_NAME_LIST, cardNames);
        params.put(StringConstants.PARSE_CLOUD_PARAMETER_CARD_TYPE_LIST, cardTypes);
        return params;
    }

    private void seperateCardFields(ArrayList<String> cardNames, ArrayList<String> cardTypes) {
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

}
