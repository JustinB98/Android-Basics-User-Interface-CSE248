package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class AddManualTransactionActivity extends AppCompatActivity {

    public final static String LOG_TAG = AddManualTransactionActivity.class.getSimpleName() + "debug";

    private View transactionContainer;
    private Button addTransactionBtn;
    private EditText placeField, amountField;
    private Spinner currencySpinner;
    private CalendarView calendarView;

    private int month, day, year;

    private Card originalCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manual_transaction);
        originalCard = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        extractViews();
        initTodaysDate();
        initButton();
        initCalendarListener();
    }

    private void initTodaysDate() {
        year = ProjectUtils.getCurrentYear();
        month = ProjectUtils.getCurrentMonth() - 1; // method returns 1-12 but api uses 0-11
        day = ProjectUtils.getCurrentDay();
    }

    private void initCalendarListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                AddManualTransactionActivity.this.month = month;
                AddManualTransactionActivity.this.year = year;
                AddManualTransactionActivity.this.day = dayOfMonth;
            }
        });
    }

    private void initButton() {
        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = ProjectUtils.normalizeString(placeField);
                String amount = ProjectUtils.normalizeString(amountField);
                addTransaction(place, amount);
            }
        });
    }

    private void addTransaction(String place, String amount) {
        if (checkForValidFields(place, amount)) {
            Log.i(LOG_TAG, "transaction was valid");
            Transaction transaction = getTransaction(place, amount);
            Log.i(LOG_TAG, "transaction: " + transaction);
            saveTransaction(transaction);
        }
    }

    private void saveTransaction(Transaction transaction) {
        HashMap<String, Object> params = getParams(transaction);
        Log.i(LOG_TAG, "calling function with " + params);
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_ADD_MANUAL_TRANSACTION, params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                Log.i(LOG_TAG, "returned with " + object);
                if (e == null) {
                    if (object.trim().toLowerCase().equals("success")) {
                        Toast.makeText(AddManualTransactionActivity.this, R.string.added_transaction_successfully, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Log.i(LOG_TAG, "e: " + e.toString() + ", code: " + e.getCode());
                }
            }
        });
    }

    private HashMap<String, Object> getParams(Transaction t) {
        HashMap<String, Object> params = new HashMap<>(5);
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE_COLUMN, t.getPlace());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT_COLUMN, t.getAmount());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE_COLUMN, t.getCurrencyCode());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE_COLUMN, t.getDate());
        params.put(StringConstants.CARD_NAME_KEY, originalCard.getCardName());
        return params;
    }

    private Transaction getTransaction(String place, String amountStr) {
        double amount = Double.parseDouble(amountStr);
        String currencyCode = (String) currencySpinner.getSelectedItem();
        Date date = new GregorianCalendar(year, month, day).getTime();
        return new Transaction(place, amount, date, currencyCode);
    }

    private boolean checkForValidFields(String place, String amount) {
        if (TextUtils.isEmpty(place)) {
            Toast.makeText(this, R.string.transaction_place_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(amount)) {
            Toast.makeText(this, R.string.transaction_amount_empty, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void extractViews() {
        transactionContainer = findViewById(R.id.transaction_container);
        addTransactionBtn = findViewById(R.id.add_manual_transaction_btn);
        placeField = findViewById(R.id.place_input);
        amountField = findViewById(R.id.amount_input);
        currencySpinner = findViewById(R.id.currency_spinner);
        calendarView = findViewById(R.id.calendar_input);
    }

}
