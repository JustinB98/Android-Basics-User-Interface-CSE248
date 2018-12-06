package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.AutoCardTransactionsParser;
import behrman.justin.financialmanager.model.CardWrapper;
import behrman.justin.financialmanager.model.DataCollection;
import behrman.justin.financialmanager.model.ViewHistoryActivity;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ParseUtils;
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
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_GET_AUTO_TRANSACTIONS, request, new ParseFunctionsUtils.DataCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> response) {
                if (response != null) {
                    if (!ParseUtils.responseHasError(response)) {
                        DataCollection data = new AutoCardTransactionsParser().parse(response);
                        Log.i(LOG_TAG, "transactions: " + data);
                        ViewAutoHistoryActivity.super.setTransactionData(data);
                    } else {
                        Toast.makeText(ViewAutoHistoryActivity.this, R.string.card_changed_refreshing, Toast.LENGTH_SHORT).show();
                        CardWrapper.getInstance().refresh(ViewAutoHistoryActivity.this);
                        finish();
                    }
                }
            }
        }, this, LOG_TAG);
    }

    private HashMap<String, Object> generateRequestHashMap(String userId, int year, int month) {
        HashMap<String, Object> request = new HashMap<>(3);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_USER_ID_COLUMN, userId);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CARD_NAME_COLUMN, cardName);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_YEAR_COLUMN, year);
        request.put(StringConstants.MANUAL_CARD_TRANSACTIONS_MONTH_COLUMN, month);
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
