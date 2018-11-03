package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.HashMap;

import behrman.justin.financialmanager.model.AutoCardTransactionsParser;
import behrman.justin.financialmanager.model.ViewHistoryActivity;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewAutoHistoryActivity extends ViewHistoryActivity {

    private final static String LOG_TAG = ViewAutoHistoryActivity.class.getSimpleName() + "debug";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getTransactions(int year, int month) {
        setData(year, month);
    }

    private void setData(int year, int month) {
        String userId = ParseUser.getCurrentUser().getObjectId();
        // int month = ProjectUtils.getCurrentMonth() + 1;
        // int day = ProjectUtils.getCurrentDay();
        // int year = ProjectUtils.getCurrentYear();
        HashMap<String, Object> request = generateRequestHashMap(userId, year, month);
        // sendRequest(jsonRequest);
        sendRequest(request);
    }

    private void sendRequest(HashMap<String, Object> request) {
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_AUTO_TRANSACTIONS_CLOUD_FUNCTION, request, new FunctionCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> response, ParseException e) {
                if (e == null) {
                    AutoCardTransactionsParser transactions = new AutoCardTransactionsParser(response);
                    Log.i(LOG_TAG, "transactions: " + transactions);
                    ViewAutoHistoryActivity.super.setTransactionData(transactions);
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString());
                }
            }
        });
    }

    private HashMap<String, Object> generateRequestHashMap(String userId, int year, int month) {
        HashMap<String, Object> request = new HashMap<>(3);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_USER_ID, userId);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CARD_NAME, cardName);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_YEAR, year);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_MONTH, month);
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
