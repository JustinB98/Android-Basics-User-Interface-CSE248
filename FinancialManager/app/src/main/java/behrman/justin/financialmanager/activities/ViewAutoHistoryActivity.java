package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewAutoHistoryActivity extends AppCompatActivity {

    private final static String LOG_TAG = ViewAutoHistoryActivity.class.getSimpleName() + "debug";

    private String cardName;

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        String userId = ParseUser.getCurrentUser().getObjectId();
        int month = ProjectUtils.getCurrentMonth() + 1;
        int day = ProjectUtils.getCurrentDay();
        int year = ProjectUtils.getCurrentYear();
        String startDate = getDateAsString(month, 1, year);
        String endDate = getDateAsString(month, day, year);
        HashMap<String, Object> request = generateRequestHashMap(userId, startDate, endDate);
        // sendRequest(jsonRequest);
        sendRequest(request);
    }

    private void sendRequest(HashMap<String, Object> request) {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_TRANSACTIONS_CLOUD_FUNCTION, request, new FunctionCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> response, ParseException e) {
                if (e == null) {
                    Log.i(LOG_TAG, "response: " + (response == null ? "null" : response.toString()));
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString());
                }
            }
        });
    }

    private HashMap<String, Object> generateRequestHashMap(String userId, String startDate, String endDate) {
        HashMap<String, Object> request = new HashMap<>(3);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_USER_ID, userId);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CARD_NAME, cardName);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_START_DATE, startDate);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_END_DATE, endDate);
        return request;
    }

    private String generateJSON(String userId, String startDate, String endDate) {
        String userIdObj = ProjectUtils.convertToJSON(StringConstants.MANUAL_CARD_TRANSACTIONS_USER_ID, userId);
        String cardNameObj = ProjectUtils.convertToJSON(StringConstants.MANUAL_CARD_TRANSACTIONS_CARD_NAME, cardName);
        String startDateObj = ProjectUtils.convertToJSON(StringConstants.MANUAL_CARD_TRANSACTIONS_START_DATE, startDate);
        String endDateObj = ProjectUtils.convertToJSON(StringConstants.MANUAL_CARD_TRANSACTIONS_END_DATE, endDate);
        return ProjectUtils.turnToJSONObject(userIdObj, cardNameObj, startDateObj, endDateObj);
    }

    private String getDateAsString(int month, int day, int year) {
        return year + "-" + month + "-" + day;
    }

    private ParseQuery<ParseObject> getQuery() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(StringConstants.AUTO_CARD_CLASS);
        query.whereEqualTo(StringConstants.AUTO_CARD_OWNER, ParseUser.getCurrentUser());
        query.whereEqualTo(StringConstants.AUTO_CARD_NAME, cardName);
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
