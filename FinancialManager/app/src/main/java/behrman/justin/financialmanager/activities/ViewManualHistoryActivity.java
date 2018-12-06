package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.CardWrapper;
import behrman.justin.financialmanager.model.DataCollection;
import behrman.justin.financialmanager.model.ManualCardTransactionParser;
import behrman.justin.financialmanager.model.ViewHistoryActivity;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ParseUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class ViewManualHistoryActivity extends ViewHistoryActivity {

    private final static String LOG_TAG = ViewManualHistoryActivity.class.getSimpleName() + "debug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getTransactions(int year, int month) {
        getData(year, month);
    }

    private void getData(int year, int month) {
        HashMap<String, Object> params = getParameters(year, month);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_GET_MANUAL_TRANSACTIONS, params, new ParseFunctionsUtils.DataCallback<HashMap<String, Object>>() {
            @Override
            public void done(HashMap<String, Object> object) {
                Log.i(LOG_TAG, "object: " + (object == null ? "null" : object.toString()));
                if (object != null) {
                    // ManualCardTransactionParser cardTransactions = new ManualCardTransactionParser(object);
                    if (!ParseUtils.responseHasError(object)) {
                        DataCollection data = new ManualCardTransactionParser().parse(object);
                        // Log.i(LOG_TAG, "cardTransactions: " + cardTransactions);
                        ViewManualHistoryActivity.super.setTransactionData(data);
                    } else {
                        Toast.makeText(ViewManualHistoryActivity.this, R.string.card_changed_refreshing, Toast.LENGTH_SHORT).show();
                        CardWrapper.getInstance().refresh(ViewManualHistoryActivity.this);
                        finish();
                    }
                }
            }
        }, this, LOG_TAG);
    }

    private HashMap<String, Object> getParameters(int year, int month) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_YEAR_COLUMN, year);
        // month needs to be 1-12
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_MONTH_COLUMN, month); // need to offset by 1
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CARD_NAME_COLUMN, cardName);
        return params;
    }

}
