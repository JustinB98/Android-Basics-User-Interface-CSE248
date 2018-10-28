package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.HashMap;

import behrman.justin.financialmanager.model.ManualCardTransactionParser;
import behrman.justin.financialmanager.model.ViewHistoryActivity;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewManualHistoryActivity extends ViewHistoryActivity {

    private final static String LOG_TAG = ViewManualHistoryActivity.class.getSimpleName() + "debug";

    private DatePicker datePicker;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getTransactions() {
        getData();
    }
    private void getData() {
        HashMap<String, Object> params = getParameters();
        ParseCloud.callFunctionInBackground(StringConstants.GET_MANUAL_TRANSACTIONS_FUNCTION, params, new FunctionCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> object, ParseException e) {
                if (e == null) {
                    ManualCardTransactionParser cardTransactions = new ManualCardTransactionParser(object);
                    Log.i(LOG_TAG, "cardTransactions: " + cardTransactions);
                    ViewManualHistoryActivity.super.setTransactionData(cardTransactions);
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString());
                }
            }
        });
    }

    private HashMap<String, Object> getParameters() {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_YEAR, ProjectUtils.getCurrentYear());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_MONTH, ProjectUtils.getCurrentMonth() + 1); // need to offset by 1
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CARD_NAME, cardName);
        return params;
    }

}
