package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

import behrman.justin.financialmanager.model.AutoCardTransactionsParser;
import behrman.justin.financialmanager.model.ViewHistoryActivity;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewAutoHistoryActivity extends ViewHistoryActivity {

    private final static String LOG_TAG = ViewAutoHistoryActivity.class.getSimpleName() + "debug";

    private ListView listView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTransactions();
    }

    private void getTransactions() {
        setToLoadView();
        setData();
    }

    private void setData() {
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
                    AutoCardTransactionsParser transactions = new AutoCardTransactionsParser(response);
                    transactions.listTransactions();
                    Log.i(LOG_TAG, transactions.toString());
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

    private String getDateAsString(int month, int day, int year) {
        String dayString = day + "";
        if (day < 10) {
            dayString = "0" + day;
        }
        String monthString = month + "";
        if (month < 10) {
            monthString = "0" + monthString;
        }
        return year + "-" + monthString + "-" + dayString;
    }

}
