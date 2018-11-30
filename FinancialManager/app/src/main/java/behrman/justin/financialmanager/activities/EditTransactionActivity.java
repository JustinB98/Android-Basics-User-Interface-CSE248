package behrman.justin.financialmanager.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
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

    private ProgressBar progressBar;
    private Menu menu;
    private View root;
    private boolean running = false;
    private final int HEIGHT_ERROR = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        originalTransaction = (Transaction) getIntent().getSerializableExtra(StringConstants.TRANSACTION_KEY);
        root = getLayoutInflater().inflate(R.layout.activity_edit_transaction, null);
        setContentView(root);
        extractViews();
        initCalendarListener();
        setFieldsToTransactions();
        initTimeFields();
        initBtnClick();
        initSeeIfMenuIsNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        initSeeIfMenuIsNeeded();
        return true;
    }

    private void initSeeIfMenuIsNeeded() {
        int rootHeight = root.getMeasuredHeight();
        int buttonY = (int) addTransactionBtn.getY();
        Log.i(LOG_TAG, "rootHeight: " + rootHeight + ", buttonY: " + buttonY);
        Log.i(LOG_TAG, buttonY + HEIGHT_ERROR + " > " + rootHeight + " ?");
        if (buttonY + HEIGHT_ERROR > rootHeight) {
            Log.i(LOG_TAG, "menu item needed");
            addMenuItem();
        }
    }

    private void addMenuItem() {
        if (menu != null) {
            getMenuInflater().inflate(R.menu.add_item_menu, menu);
        }
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
        currencySpinner.setSelection(ProjectUtils.convertCurrencyCodeStringToIndex(this, originalTransaction.getCurrencyCode()));
        calendarView.setDate(originalTransaction.getDate().getTime());
    }

    private void initBtnClick() {
        addTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    progressBar.setVisibility(View.VISIBLE);
                    running = true;
                    editTransaction();
                }
            }
        });
    }

    private void editTransaction() {
        String newPlace = ProjectUtils.normalizeString(placeField);
        String newAmount = ProjectUtils.normalizeString(amountField);
        editTransaction(newPlace, newAmount);
    }

    private void editTransaction(String newPlace, String newAmount) {
        if (checkForValidFields(newPlace, newAmount)) {
            Transaction newTransaction = getTransaction(newPlace, newAmount);
            Log.i(LOG_TAG, "transaction: " + newTransaction);
            if (originalTransaction.equals(newTransaction)) {
                Log.i(LOG_TAG, "original transaction is the same as the new transaction, finishing activity...");
                finish();
                return;
            }
            sendToDatabase(newTransaction);
        } else {
            running = false;
            progressBar.setVisibility(View.GONE);
        }
    }

    private void sendToDatabase(final Transaction t) {
        HashMap<String, Object> params = getParams(t);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_EDIT_MANUAL_CARD, params, new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                Log.i(LOG_TAG, "returned with " + object);
                if (object != null) {
                    running = false;
                    progressBar.setVisibility(View.GONE);
                    if (ProjectUtils.deepEquals(object, StringConstants.SUCCESS)) {
                        Toast.makeText(EditTransactionActivity.this, R.string.transaction_edited_successfully, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }, this, LOG_TAG);
    }


    private HashMap<String, Object> getParams(Transaction t) {
        HashMap<String, Object> params = new HashMap<>(5);
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_PLACE_COLUMN, t.getPlace());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_AMOUNT_COLUMN, t.getAmount());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_CURRENCY_CODE_COLUMN, t.getCurrencyCode());
        params.put(StringConstants.MANUAL_CARD_TRANSACTIONS_DATE_COLUMN, t.getDate());
        params.put(StringConstants.OBJECT_ID_KEY, originalTransaction.getObjectId());
        return params;
    }

    private Transaction getTransaction(String newPlace, String newAmount) {
        double amount = Double.parseDouble(newAmount);
        String currencyCode = (String) currencySpinner.getSelectedItem();
        Date date = new GregorianCalendar(year, month, day).getTime();
        return new Transaction(newPlace, amount, date, currencyCode, originalTransaction.getObjectId());
    }

    private boolean checkForValidFields(String newPlace, String newAmount) {
        if (TextUtils.isEmpty(newPlace)) {
            Toast.makeText(this, R.string.transaction_place_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(newAmount)) {
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
        progressBar = findViewById(R.id.progress_bar);
    }

    public void menuAction(MenuItem item) {
        if (item.getItemId() == R.id.add_item) {
            editTransaction();
        }
    }
}
