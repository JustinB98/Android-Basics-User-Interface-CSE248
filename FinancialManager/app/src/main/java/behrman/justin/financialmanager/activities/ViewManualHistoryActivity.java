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
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewManualHistoryActivity extends AppCompatActivity {

    private final static String LOG_TAG = ViewManualHistoryActivity.class.getSimpleName() + "debug";

    private String cardName;

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);
        cardName = getIntent().getStringExtra(StringConstants.CARD_NAME);
        extractViews();
        getTransactions();
    }

    private void getTransactions() {
        setToLoadView();
        ParseQuery<ParseObject> query = getQuery();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    setData(object);
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString());
                }
            }
        });
    }

    private void setData(ParseObject card) {
        ParseRelation<ParseObject> transactions = card.getRelation(StringConstants.MANUAL_CARD_TRANSACTIONS);
        ParseQuery<ParseObject> query = transactions.getQuery();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    collectTransactions(objects);
                } else {
                    Log.i(LOG_TAG, "error: " + e.toString());
                }
            }
        });
    }

    private void collectTransactions(List<ParseObject> objects) {
        Log.i(LOG_TAG, objects.size() + "");
        ArrayList<Transaction> transactions = new ArrayList<>(objects.size());
        for (int i = 0; i < objects.size(); ++i) {
            Transaction transaction = getTransaction(objects.get(i));
            transactions.add(transaction);
        }
        ArrayAdapter<Transaction> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactions);
        listView.setAdapter(adapter);
        setToListView();
    }

    private Transaction getTransaction(ParseObject obj) {
        String place = obj.getString(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE);
        double amount = obj.getDouble(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT);
        Date date = obj.getDate(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE);
        String currentCode = obj.getString(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE);
        return new Transaction(place, amount, date, currentCode);
    }

    private ParseQuery<ParseObject> getQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(StringConstants.MANUAL_CARD_CLASS);
        query.whereEqualTo(StringConstants.MANUAL_CARD_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(StringConstants.MANUAL_CARD_NAME, cardName);
        return query;
    }

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
