package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.CardType;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewHistoryActivity extends AppCompatActivity {

    private final static String LOG_TAG = ViewHistoryActivity.class.getSimpleName() + "debug";

    private String cardName;
    private CardType cardType;

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        cardName = getIntent().getStringExtra(StringConstants.CARD_NAME);
        cardType = (CardType) getIntent().getSerializableExtra(StringConstants.CARD_TYPE);
        extractViews();
        getTransactions();
    }

    // TODO: 10/25/2018 make this activity into two different activities and make manual class query generic be from transactions
    private void getTransactions() {
        setToLoadView();
        ParseQuery<ParseObject> query = getQuery();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    showData(object);
                } else {
                    Log.i(LOG_TAG, "error: " + e.toString());
                }
            }
        });
    }

    private void showData(ParseObject object) {
        switch(cardType) {
            case MANUAL:
                setToManualData(object);
                break;
            case AUTO:
                setToAutoData(object);
                break;
            default:
                Log.i(LOG_TAG, "invalid card type: " + (cardType == null ? "null" : cardType.toString()));
        }
    }

    private void setToManualData(ParseObject card) {
        ParseRelation<ParseObject> transactions = card.getRelation(StringConstants.MANUAL_CARD_TRANSACTIONS);
        ParseQuery<ParseObject> query = transactions.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    collectManualTransactions(objects);
                } else {
                    Log.i(LOG_TAG, "error: " + e.toString());
                }
            }
        });
    }

    private void collectManualTransactions(List<ParseObject> objects) {
        Log.i(LOG_TAG, objects.size() + "");
        ArrayList<Transaction> transactions = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); ++i) {
            Transaction transaction = getManualTransaction(objects.get(i));
            transactions.add(transaction);
        }
        ArrayAdapter<Transaction> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactions);
        listView.setAdapter(adapter);
        setToListView();
    }

    private Transaction getManualTransaction(ParseObject obj) {
        String place = obj.getString(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE);
        double amount = obj.getDouble(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT);
        Date date = obj.getDate(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE);
        String currentCode = obj.getString(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE);
        return new Transaction(place, amount, date, currentCode);
    }

    private void setToAutoData(ParseObject card) {

    }

    // --------------------- START QUERIES ---------------------
    private ParseQuery<ParseObject> getQuery() {
        switch(cardType) {
            case MANUAL:
                return getManualQuery();
            case AUTO:
                return getAutoQuery();
            default:
                Log.i(LOG_TAG, "invalid card type: " + (cardType == null ? "null" : cardType.toString()));
                return null;
        }
    }


    private ParseQuery<ParseObject> getManualQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(StringConstants.MANUAL_CARD_CLASS);
        query.whereEqualTo(StringConstants.MANUAL_CARD_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(StringConstants.MANUAL_CARD_NAME, cardName);
        return query;
    }

    private ParseQuery<ParseObject> getAutoQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(StringConstants.AUTO_CARD_CLASS);
        query.whereEqualTo(StringConstants.AUTO_CARD_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(StringConstants.AUTO_CARD_NAME, cardName);
        return query;
    }

    // --------------------- END QUERIES ---------------------

    private void extractViews() {
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void setToLoadView() {
        listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void setToListView() {
        listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}
