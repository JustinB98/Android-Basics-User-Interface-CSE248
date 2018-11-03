package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
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

    private Card originalCard;

    private Transaction originalTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        originalTransaction = (Transaction) getIntent().getSerializableExtra(StringConstants.TRANSACTION_KEY);
        setContentView(R.layout.activity_edit_transaction);
        extractViews();
        originalCard = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        setFieldsToTransactions();
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
            sendToDatabase(newTransaction);
        }
    }

    private void sendToDatabase(Transaction t) {
        
    }

    private Transaction getTransaction() {
        String place = placeField.getText().toString();
        double amount = Double.parseDouble(amountField.getText().toString());
        String currencyCode = (String) currencySpinner.getSelectedItem();
        Date date = new GregorianCalendar(year, month, day).getTime();
        return new Transaction(place, amount, date, currencyCode);
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
