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
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class EditTransactionActivity extends AppCompatActivity {

    public final static String LOG_TAG = EditTransactionActivity.class.getSimpleName() + "debug";

    private View transactionContainer;
    private Button addTransactionBtn;
    private EditText placeField, amountField;
    private Spinner currencySpinner;
    private CalendarView calendarView;

    private int month, day, year;

    private Transaction originalTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        originalTransaction = (Transaction) getIntent().getSerializableExtra(StringConstants.TRANSACTION_KEY);
        setContentView(R.layout.activity_edit_transaction);
        extractViews();
        initCalendarListener();
        setFieldsToTransactions();
        initTimeFields();
        initBtnClick();
    }

    private void initTimeFields() {
        String date = ProjectUtils.convertDateToString(originalTransaction.getDate());
        String[] dateTokens = date.split("-");
        year = Integer.parseInt(dateTokens[0]);
        month = Integer.parseInt(dateTokens[1]) - 1; // going from 1-12 to 0-11
        day = Integer.parseInt(dateTokens[2]);
    }

    private void initCalendarListener() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                EditTransactionActivity.this.month = month;
                EditTransactionActivity.this.year = year;
                EditTransactionActivity.this.day = dayOfMonth;
            }
        });
    }

    private void setFieldsToTransactions() {
        placeField.setText(originalTransaction.getPlace());
        amountField.setText(originalTransaction.getAmount() + "");
        currencySpinner.setSelection(ProjectUtils.convertCurrencyCodeStringToIndex(originalTransaction.getCurrencyCode(), this));
        calendarView.setDate(originalTransaction.getDate().getTime());
    }

    private void initBtnClick() {
        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTransaction();
            }
        });
    }

    private void editTransaction() {
        if (checkForValidFields()) {
            Transaction newTransaction = getTransaction();
            Log.i(LOG_TAG, "transaction: " + newTransaction);
            sendToDatabase(newTransaction);
        }
    }

    private void sendToDatabase(final Transaction t) {
        HashMap<String, Object> params = getParams(t);
        ParseCloud.callFunctionInBackground(StringConstants.PARSE_CLOUD_FUNCTION_EDIT_MANUAL_CARD, params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                Log.i(LOG_TAG, "returned with " + object);
                if (e == null) {
                    if (ProjectUtils.deepEquals(object, "success")) {
                        Toast.makeText(EditTransactionActivity.this, "success!", Toast.LENGTH_SHORT).show();
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
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE, t.getPlace());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT, t.getAmount());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE, t.getCurrencyCode());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE, t.getDate());
        params.put(StringConstants.OBJECT_ID_KEY, originalTransaction.getObjectId());
        return params;
    }

    private Transaction getTransaction() {
        String place = placeField.getText().toString();
        double amount = Double.parseDouble(amountField.getText().toString());
        String currencyCode = (String) currencySpinner.getSelectedItem();
        Date date = new GregorianCalendar(year, month, day).getTime();
        return new Transaction(place, amount, date, currencyCode, originalTransaction.getObjectId());
    }

    private boolean checkForValidFields() {
        if (TextUtils.isEmpty(placeField.getText())) {
            Toast.makeText(this, "place cannot be empty", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "place is empty");
            return false;
        } else if (TextUtils.isEmpty(amountField.getText())) {
            Toast.makeText(this, "amount cannot be empty", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "amount is empty");
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
