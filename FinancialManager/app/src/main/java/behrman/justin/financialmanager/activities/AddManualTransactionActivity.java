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

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import behrman.justin.financialmanager.R;
import behrman.justin.financialmanager.model.Card;
import behrman.justin.financialmanager.model.CardWrapper;
import behrman.justin.financialmanager.model.Transaction;
import behrman.justin.financialmanager.utils.ParseFunctionsUtils;
import behrman.justin.financialmanager.utils.ProjectUtils;
import behrman.justin.financialmanager.utils.StringConstants;

public class AddManualTransactionActivity extends AppCompatActivity {

    public final static String LOG_TAG = AddManualTransactionActivity.class.getSimpleName() + "debug";

    private Button addTransactionBtn;
    private EditText placeField, amountField;
    private Spinner currencySpinner;
    private CalendarView calendarView;

    private ProgressBar progressBar;

    private int month, day, year;

    private Menu menu;

    private View root;

    private boolean running = false;
    private Card originalCard;

    private final int HEIGHT_ERROR = 40;

    private CalendarDay passedInDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = getLayoutInflater().inflate(R.layout.activity_add_manual_transaction, null);
        setContentView(root);
        originalCard = (Card) getIntent().getSerializableExtra(StringConstants.CARD_KEY);
        passedInDay = getIntent().getParcelableExtra(StringConstants.SELECTED_DATE_KEY);
        extractViews();
        initSelectedDate();
        initButton();
        initCalendarListener();
        initSeeIfMenuIsNeeded();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home && !running) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        Log.i(LOG_TAG, "on create options menu");
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

    public void menuAction(MenuItem item) {
        if (item.getItemId() == R.id.add_item) {
            addTransaction();
        }
    }

    // defaults to the current day
    private void initSelectedDate() {
        if (passedInDay == null || shouldChangePassedInDate()) {
            passedInDay = CalendarDay.today();
        }
        checkForInvalidDay();
        year = passedInDay.getYear();
        month = passedInDay.getMonth(); // returns 0-11 which works with the api
        day = passedInDay.getDay();
        calendarView.setDate(passedInDay.getDate().getTime());
    }

    private void checkForInvalidDay() {
        int day = passedInDay.getDay();
        if (day == -1) {
            passedInDay = CalendarDay.from(passedInDay.getYear(), passedInDay.getMonth(), 1);
        }
    }

    // if we're in the same month and year, and it's the first, which means no date is selected, then change the date
    // if the selected day is the first of the month, then don't change the date
    private boolean shouldChangePassedInDate() {
        int month = ProjectUtils.getCurrentMonth() - 1; // need 0-11
        int year = ProjectUtils.getCurrentYear();
        boolean isSameMonthAndYear = passedInDay.getMonth() == month && passedInDay.getYear() == year;
        return isSameMonthAndYear && passedInDay.getDay() == -1;
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
                addTransaction();
            }
        });
    }

    private void addTransaction() {
        if (!running) {
            running = true;
            progressBar.setVisibility(View.VISIBLE);
            String place = ProjectUtils.normalizeString(placeField);
            String amount = ProjectUtils.normalizeString(amountField);
            addTransaction(place, amount);
        }
    }

    private void addTransaction(String place, String amount) {
        if (checkForValidFields(place, amount)) {
            Log.i(LOG_TAG, "transaction was valid");
            Transaction transaction = getTransaction(place, amount);
            Log.i(LOG_TAG, "transaction: " + transaction);
            saveTransaction(transaction);
        } else {
            running = false;
            progressBar.setVisibility(View.GONE);
        }
    }

    private void saveTransaction(Transaction transaction) {
        HashMap<String, Object> params = getParams(transaction);
        ParseFunctionsUtils.callFunctionInBackgroundDisplayError(StringConstants.PARSE_CLOUD_FUNCTION_ADD_MANUAL_TRANSACTION, params, new ParseFunctionsUtils.DataCallback<String>() {
            @Override
            public void done(String object) {
                running = false;
                progressBar.setVisibility(View.GONE);
                Log.i(LOG_TAG, "returned with " + object);
                if (object != null) {
                    if (ProjectUtils.deepEquals(object, StringConstants.SUCCESS)) {
                        Toast.makeText(AddManualTransactionActivity.this, R.string.added_transaction_successfully, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (ProjectUtils.deepEquals(object, StringConstants.ERROR)) {
                        Toast.makeText(AddManualTransactionActivity.this, R.string.card_changed_refreshing, Toast.LENGTH_SHORT).show();
                        CardWrapper.getInstance().refresh(AddManualTransactionActivity.this);
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
        addTransactionBtn = root.findViewById(R.id.add_manual_transaction_btn);
        placeField = root.findViewById(R.id.place_input);
        amountField = root.findViewById(R.id.amount_input);
        currencySpinner = root.findViewById(R.id.currency_spinner);
        calendarView = root.findViewById(R.id.calendar_input);
        progressBar = root.findViewById(R.id.progress_bar);
    }

}
